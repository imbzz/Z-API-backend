package com.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.common.ErrorCode;
import com.project.controller.UserController;
import com.project.exception.BusinessException;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import com.zapi.zapicommon.model.entity.User;
import com.zapi.zapicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author imbzz
 * @Date 2023/12/21 21:56
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUsser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
