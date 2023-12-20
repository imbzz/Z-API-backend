package com.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.model.dto.userinterfaceInfo.UserInterfaceInfoQueryRequest;
import com.project.model.entity.UserInterfaceInfo;
import com.project.model.vo.UserInterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 86188
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-12-04 22:41:49
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest interfaceInfoQueryRequest);

    Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> interfaceInfoPage, HttpServletRequest request);

    void validUserInterfaceInfo(UserInterfaceInfo interfaceInfo, boolean add);

    UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo interfaceInfo, HttpServletRequest request);

    boolean invokeCount(long interfaceInfoId, long userId);
}
