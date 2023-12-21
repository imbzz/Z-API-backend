package com.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.common.ErrorCode;
import com.project.exception.BusinessException;
import com.project.mapper.InterfaceInfoMapper;
import com.project.service.InterfaceInfoService;
import com.zapi.zapicommon.model.entity.InterfaceInfo;
import com.zapi.zapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.prefs.BackingStoreException;

/**
 * @author imbzz
 * @Date 2023/12/21 21:55
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoService;
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if(StringUtils.isBlank(path) || StringUtils.isBlank(method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", path);
        queryWrapper.eq("method", method);
        return interfaceInfoService.selectOne(queryWrapper);
    }
}
