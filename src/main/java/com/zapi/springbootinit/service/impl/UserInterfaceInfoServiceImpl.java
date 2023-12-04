package com.zapi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.zapi.springbootinit.common.ErrorCode;
import com.zapi.springbootinit.constant.CommonConstant;
import com.zapi.springbootinit.exception.BusinessException;
import com.zapi.springbootinit.exception.ThrowUtils;
import com.zapi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.zapi.springbootinit.model.dto.userinterfaceInfo.UserInterfaceInfoQueryRequest;
import com.zapi.springbootinit.model.entity.UserInterfaceInfo;

import com.zapi.springbootinit.model.vo.UserInterfaceInfoVO;
import com.zapi.springbootinit.service.UserInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86188
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-12-04 22:41:49
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    /**
     * 查询条件
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();

        //查询条件
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),sortField);
        return queryWrapper;
    }

    @Override
    public Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> userInterfaceInfoPage, HttpServletRequest request) {
        //todo 判断登录
        //脱敏
        Page<UserInterfaceInfoVO> pageVO = new Page<>();
        List<UserInterfaceInfo> records = userInterfaceInfoPage.getRecords();
        List<UserInterfaceInfoVO> collect = records.stream().map(record -> {
            //todo 脱敏处理
            UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
            BeanUtils.copyProperties(record, userInterfaceInfoVO);
            return userInterfaceInfoVO;
        }).collect(Collectors.toList());
        pageVO.setRecords(collect);
        pageVO.setTotal(userInterfaceInfoPage.getTotal());
        pageVO.setCurrent(userInterfaceInfoPage.getCurrent());
        return pageVO;
    }

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {

        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，参数不能为空

        if (add) {
            if(userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }
        if(userInterfaceInfo.getLeftNum() < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余调用次数不能小于0");
        }
    }

    @Override
    public UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo interfaceInfo, HttpServletRequest request) {
        UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, userInterfaceInfoVO);
        return userInterfaceInfoVO;
    }
}




