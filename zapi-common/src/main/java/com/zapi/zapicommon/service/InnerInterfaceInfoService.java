package com.zapi.zapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.zapicommon.model.entity.InterfaceInfo;

/**
* @author 86188
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-11-12 17:22:45
*/
public interface InnerInterfaceInfoService {

    /**
     * 查询接口是否存在
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
