package com.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.zapicommon.model.entity.InterfaceInfo;
import com.zapi.zapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 86188
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-12-04 22:41:49
* @Entity com.entity.model.springbootinit.zapi.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

//    select interfaceInfoId, sum(totalNum) as totalNum
//    from user_interface_info
//    group by interfaceInfoId
//    order by totalNum desc
//    limit 3;
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




