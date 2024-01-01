package com.project.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.zapi.zapicommon.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author imbzz
 * @Date 2023/11/18 20:59
 */
@Data
public class InterfaceInfoVO extends InterfaceInfo {

    /**
     * 调用次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;

}
