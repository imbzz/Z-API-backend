package com.zapi.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.zapi.springbootinit.model.entity.InterfaceInfo;
import com.zapi.springbootinit.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;


/**
* @author 86188
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-11-12 17:22:45
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);


    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);
}
