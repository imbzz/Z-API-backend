package com.zapi.zapicommon.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author imbzz
 * @Date 2023/11/12 18:04
 */
public enum InterfaceInfoEnum {
    OPEN("开启", 1),
    CLOSE("关闭", 0);
    
    private final String text;

    private final Integer value;

    InterfaceInfoEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static InterfaceInfoEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (InterfaceInfoEnum anEnum : InterfaceInfoEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
