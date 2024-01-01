package com.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.annotation.AuthCheck;
import com.project.common.BaseResponse;
import com.project.common.ErrorCode;
import com.project.common.ResultUtils;
import com.project.exception.BusinessException;
import com.project.mapper.UserInterfaceInfoMapper;
import com.project.model.vo.InterfaceInfoVO;
import com.project.service.InterfaceInfoService;
import com.zapi.zapicommon.model.entity.InterfaceInfo;
import com.zapi.zapicommon.model.entity.UserInterfaceInfo;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author imbzz
 * @Date 2024/1/1 22:22
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private  InterfaceInfoService interfaceInfoService;
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo(){
        //查询调用次数最多的接口信息
        List<UserInterfaceInfo> listUserInterfaceInfo = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        //将接口信息按id分钟，便于查询
        Map<Long, List<UserInterfaceInfo>> interfaceInfoCollect = listUserInterfaceInfo.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        //根据id查询接口信息
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",interfaceInfoCollect.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        //判断是否空
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据为空");
        }
        //遍历创建vo响应对象
        List<InterfaceInfoVO> interfaceInfoVoList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            //将接口调用信息放入vo中
            int totalNum = interfaceInfoCollect.get(interfaceInfo.getId()).get(0).getTotalNum();
            //负重
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(interfaceInfoVoList);
    }
}
