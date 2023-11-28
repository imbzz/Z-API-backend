package com.imbzz.springbootinit.common;

/**
 * 返回工具类
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> com.imbzz.springbootinit.common.BaseResponse<T> success(T data) {
        return new com.imbzz.springbootinit.common.BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static com.imbzz.springbootinit.common.BaseResponse error(ErrorCode errorCode) {
        return new com.imbzz.springbootinit.common.BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static com.imbzz.springbootinit.common.BaseResponse error(int code, String message) {
        return new com.imbzz.springbootinit.common.BaseResponse(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static com.imbzz.springbootinit.common.BaseResponse error(ErrorCode errorCode, String message) {
        return new com.imbzz.springbootinit.common.BaseResponse(errorCode.getCode(), null, message);
    }
}
