package com.zapi.springbootinit.model.dto.userinterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author imbzz
 * @Date 2023/11/12 17:40
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {


    private static final long serialVersionUID = -2217213628431189228L;
    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

}
