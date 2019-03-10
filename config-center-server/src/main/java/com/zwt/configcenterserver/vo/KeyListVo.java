package com.zwt.configcenterserver.vo;

import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/12/25
 * @since 1.0
 */
public class KeyListVo {
    private String code;
    private String message;
    private List<String> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public KeyListVo(String code, String message, List<String> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public KeyListVo() {
    }
}
