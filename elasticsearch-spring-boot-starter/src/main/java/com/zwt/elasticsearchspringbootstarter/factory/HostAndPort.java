package com.zwt.elasticsearchspringbootstarter.factory;

/**
 * @author zwt
 * @detail 配置类
 * @date 2018/8/30
 * @since 1.0
 */
public class HostAndPort {
    private String ip;
    private Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public HostAndPort(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }
}
