package com.zapi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.zapi.springbootinit.model.entity.UserInterfaceInfo;

import com.zapi.springbootinit.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author 86188
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-12-04 22:41:49
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

}




