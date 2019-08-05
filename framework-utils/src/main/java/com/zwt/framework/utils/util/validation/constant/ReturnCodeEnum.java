package com.zwt.framework.utils.util.validation.constant;

import java.util.Arrays;

/**
 * @author zwt
 * @detail 返回码枚举
 * @date 2019/8/5
 * @since 1.0
 */
public enum ReturnCodeEnum {
    SUCCESS("0000","成功"),
    PARAMER_ERROR("1000","参数错误"),
    SYSTEM_ERROR("9999","系统异常");

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ReturnCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ReturnCodeEnum getEnum(String code){
       return Arrays.stream(ReturnCodeEnum.values()).filter(e -> e.code.equals(code)).findFirst().orElse(null);
    }
}
