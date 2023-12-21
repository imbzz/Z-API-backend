package com.project.service.impl.inner;

import com.project.service.UserInterfaceInfoService;
import com.zapi.zapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author imbzz
 * @Date 2023/12/21 21:56
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
