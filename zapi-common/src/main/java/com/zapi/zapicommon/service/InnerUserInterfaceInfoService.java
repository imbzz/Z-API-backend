package com.zapi.zapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.zapicommon.model.entity.UserInterfaceInfo;


/**
* @author 86188
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-12-04 22:41:49
*/
public interface InnerUserInterfaceInfoService {



    /**
     * 调用次数+1
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
