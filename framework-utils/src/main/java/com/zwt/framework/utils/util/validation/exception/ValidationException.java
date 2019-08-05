package com.zwt.framework.utils.util.validation.exception;

import com.zwt.framework.utils.util.validation.constant.ReturnCodeEnum;

/**
 * @author zwt
 * @detail 校验异常帮助类
 * @date 2019/5/7
 * @since 1.0
 */
public class ValidationException extends RuntimeException {

    private String code = "";
    private String msg = "";

    public ValidationException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ValidationException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public ValidationException(final Exception cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ValidationException(ReturnCodeEnum returnCodeEnum){
        super(returnCodeEnum.getMsg());
        this.code = returnCodeEnum.getCode();
        this.msg = returnCodeEnum.getMsg();
    }
}
