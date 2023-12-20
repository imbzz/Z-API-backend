package com.project.model.dto.userinterfaceInfo;

import com.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author imbzz
 * @Date 2023/11/12 17:42
 */
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -2795430563099355649L;
    /**
     * 主键
     */
    private Long id;

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

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

}
