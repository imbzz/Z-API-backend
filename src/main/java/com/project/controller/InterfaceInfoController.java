package com.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.project.annotation.AuthCheck;
import com.project.common.*;
import com.project.constant.UserConstant;
import com.project.exception.BusinessException;
import com.project.exception.ThrowUtils;
import com.project.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.project.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.project.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.project.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.project.model.enums.isOpenEnum;
import com.project.model.vo.InterfaceInfoVO;
import com.project.service.InterfaceInfoService;
import com.project.service.UserService;
import com.imbzz.zapiclientstarter.client.ZApiClient;
import com.zapi.zapicommon.model.entity.InterfaceInfo;
import com.zapi.zapicommon.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口管理
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private ZApiClient zApiClient;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }




    /**
     * 上线接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/onLine")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onLineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if(idRequest == null || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        //1.校验接口是否是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        //2。根据id查询接口的信息
        if (oldInterfaceInfo == null){
            //3。如果为null 抛出异常
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //4.判断接口是否可以调用
        com.imbzz.zapiclientstarter.model.User user = new com.imbzz.zapiclientstarter.model.User();
        //设置对象为test
        user.setUserName("test");
        //通过Post方法的getUserByPost方法传入user对象
        String userNameByPost = zApiClient.getUserNameByPost(user);
        //判断是否为null
        if(StringUtils.isBlank(userNameByPost)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败");
        }
        //创建一个InterfaceInfo对象
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        //根据id获取设置状态
        interfaceInfo.setId(id);
        //修改状态
        interfaceInfo.setStatus(isOpenEnum.OPEN.getValue());
        //调用updateById 更新
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }



    /**
     * 下线接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offLine")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offLineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request) {
        if(idRequest == null || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        //1.校验接口是否是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        //2。根据id查询接口的信息
        if (oldInterfaceInfo == null){
            //3。如果为null 抛出异常
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //创建一个InterfaceInfo对象
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        //根据id获取设置状态
        interfaceInfo.setId(id);
        //修改状态
        interfaceInfo.setStatus(isOpenEnum.CLOSE.getValue());
        //调用updateById 更新
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 测试调用接口(提供给用户测试)
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
// 这里给它新封装一个参数InterfaceInfoInvokeRequest
// 返回结果把对象发出去就好了，因为不确定接口的返回值到底是什么
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        // 检查请求对象是否为空或者接口id是否小于等于0
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取接口id
        long id = interfaceInfoInvokeRequest.getId();
        // 获取用户请求参
         String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 检查接口状态是否为下线状态
        if (oldInterfaceInfo.getStatus() == isOpenEnum.CLOSE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        //获取用户的accessKey 和 secretKey
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        //解析传输过来的参数
        Gson gson = new Gson();
        com.imbzz.zapiclientstarter.model.User user = gson.fromJson(userRequestParams, com.imbzz.zapiclientstarter.model.User.class);
        //创建zApiClient对象
        ZApiClient zApiClientTemp = new ZApiClient(accessKey,secretKey);
        //调用远程接口
        String userNameByPost = zApiClientTemp.getUserNameByPost(user);
        return ResultUtils.success(userNameByPost);

    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVO(interfaceInfo, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
            HttpServletRequest request) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
            HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        interfaceInfoQueryRequest.setUserId(loginUser.getId());
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }

    // endregion

//    /**
//     * 分页搜索（从 ES 查询，封装类）
//     *
//     * @param interfaceInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<InterfaceInfoVO>> searchInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
//            HttpServletRequest request) {
//        long size = interfaceInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.searchFromEs(interfaceInfoQueryRequest);
//        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
//    }


}
