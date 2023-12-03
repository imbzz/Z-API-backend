package com.imbzz.springbootinit.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author imbzz
 * @Date 2023/12/2 15:04
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = -6400818744589586916L;

}
