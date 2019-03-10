package com.zwt.configcenterserver.vo;

/**
 * @author zwt
 * @detail
 * @date 2018/12/25
 * @since 1.0
 */
public class DataVo {
    private String value;
    private String describe;
    private String updateTime;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public DataVo(String value, String describe, String updateTime) {
        this.value = value;
        this.describe = describe;
        this.updateTime = updateTime;
    }

    public DataVo() {
    }
}
