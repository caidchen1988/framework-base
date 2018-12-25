package com.zwt.config.vo;

/**
 * @author zwt
 * @detail 缓存节点bean
 * @date 2018/9/5
 * @since 1.0
 */
public class CacheNodeVo {
    private String key;
    private String value;

    public CacheNodeVo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
