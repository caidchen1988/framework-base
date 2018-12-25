package com.zwt.config.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zwt
 * @detail 配置中心配置
 * @date 2018/12/21
 * @since 1.0
 */
@ConfigurationProperties("spring.zookeeper.config-center")
public class ConfigCenterConfiguration {
    //zk地址
    private String zkAddress;
    //业务名称
    private String sysName;
    //连接超时时间
    private Integer connectTimeoutMs = 60000;
    //session过期时间
    private Integer sessionTimeoutMs = 60000;
    //重试间隔
    private Integer retryInterval = 1000;

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public Integer getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(Integer connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public Integer getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }
}
