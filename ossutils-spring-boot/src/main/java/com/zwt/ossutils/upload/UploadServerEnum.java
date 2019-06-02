package com.zwt.ossutils.upload;

import java.util.Arrays;

/**
 * @author zwt
 * @detail 服务器枚举
 * @date 2019/4/30
 * @since 1.0
 */
public enum  UploadServerEnum {
    /**
     * 阿里云OSS
     */
    ALIOSS("aliyun_oss","阿里云OSS"),
    /**
     * 亚马逊s3
     */
    AMAZON("amazon_s3","亚马逊S3"),
    /**
     * 微软azure
     */
    AZURE("azure","微软Azure"),
    /**
     * 腾讯云cos
     */
    TENCENTCOS("tencent_cos","腾讯云cos");

    private String value;
    private String desc;

    UploadServerEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取一个枚举
     * @param value
     * @return
     */
    public static UploadServerEnum getEnum(String value){
        return Arrays.stream(UploadServerEnum.values()).filter(e->e.value.equals(value)).findFirst().get();
    }
}
