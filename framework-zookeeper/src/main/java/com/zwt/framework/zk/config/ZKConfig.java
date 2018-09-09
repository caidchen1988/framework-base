package com.zwt.framework.zk.config;

/**
 * @author zwt
 * @detail ZK连接配置
 * @date 2018/9/4
 * @since 1.0
 */
public class ZKConfig {
    /**
     * zk连接串，格式host1:port1,host2:port2...
     */
    private String connectString;
    /**
     * 命名空间，用于业务隔离
     */
    private String nameSpace;
    /**
     * 重试间隔初始时间，每一次重试间隔停顿的时间逐渐增加
     */
    private int retryInterval = 1000;
    /**
     * 连接创建超时时间（单位:ms）
     */
    private int connectTimeoutMs = 60000;
    /**
     * 会话超时时间（单位:ms）
     */
    private int sessionTimeoutMs = 60000;

    public ZKConfig(String connectString, String nameSpace) {
        this.connectString = connectString;
        this.nameSpace = nameSpace;
    }

    public ZKConfig(String connectString, String nameSpace, int retryInterval, int connectTimeoutMs, int sessionTimeoutMs) {
        this.connectString = connectString;
        this.nameSpace = nameSpace;
        this.retryInterval = retryInterval;
        this.connectTimeoutMs = connectTimeoutMs;
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public ZKConfig() {
    }
}
