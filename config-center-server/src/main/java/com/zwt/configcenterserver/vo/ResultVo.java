package com.zwt.configcenterserver.vo;

/**
 * @author zwt
 * @detail
 * @date 2018/12/25
 * @since 1.0
 */
public class ResultVo {
    private String code;
    private String message;
    private DataVo data;

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

    public DataVo getData() {
        return data;
    }

    public void setData(DataVo data) {
        this.data = data;
    }

    public ResultVo(String code, String message, DataVo data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultVo() {
    }
}
