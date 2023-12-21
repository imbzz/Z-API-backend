package com.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.project.common.ErrorCode;
import com.project.constant.CommonConstant;
import com.project.exception.BusinessException;
import com.project.mapper.UserInterfaceInfoMapper;
import com.project.model.dto.userinterfaceInfo.UserInterfaceInfoQueryRequest;

import com.project.model.vo.UserInterfaceInfoVO;
import com.project.service.UserInterfaceInfoService;
import com.zapi.zapicommon.model.entity.UserInterfaceInfo;
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


    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 判断
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);

//        updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }
}




