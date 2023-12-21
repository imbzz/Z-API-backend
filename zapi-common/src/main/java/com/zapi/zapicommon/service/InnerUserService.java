package com.zapi.zapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.zapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface InnerUserService  {

    /**
     * 数据库中查询是否给用户分配秘钥
     */
    User getInvokeUsser(String accessKey);
}
