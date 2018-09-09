package com.zwt.framework.redis.factory;

/**
 * @author zwt
 * @detail Redis基本配置Bean
 * @date 2018/9/3
 * @since 1.0
 */
public class RedisConfiguration {
    /**
     * 本地属性文件路径.
     */
    private String localPropertiesPath;

    /**
     * redis模式，1：单机模式（默认）、2：集群模式.
     */
    private int mode;

    /**
     * ip 及端口地址 ，分号分隔地址 示例：127.0.0.1:6379;127.0.0.1:6380;127.0.0.1:6381;127.0.0.1:6382;127.0.0.1:6383;127.0.0.1:6384
     */
    private String address;

    /**
     * 获取连接时的最大等待毫秒数
     */
    private long maxWaitMillis=5000;
    /**
     * 最大连接数
     */
    private int maxTotal=512;

    /**
     * 最小空闲
     */
    private int minIdle=10;
    /**
     * 最大空闲
     */
    private int maxIdle=64;
    /**
     * 连接超时时间
     */
    private int timeout=300000;
    /**
     * 超时最大重试次数
     */
    private int maxRedirections=6;
    /**
     * 使用数据库，集群模式只能使用database0
     */
    private int database=0;
    /**
     * 获取  本地属性文件路径.
     * @return localPropertiesPath
     */
    public String getLocalPropertiesPath() {
        return localPropertiesPath;
    }
    /**
     * 设置 本地属性文件路径.
     * @param localPropertiesPath
     */
    public void setLocalPropertiesPath(String localPropertiesPath) {
        this.localPropertiesPath = localPropertiesPath;
    }
    /**
     * 获取  ip及端口地址，分号分隔地址示例：127.0.0.1:6379;127.0.0.1:6380;127.0.0.1:6381;127.0.0.1:6382;127.0.0.1:6383;127.0.0.1:6384
     * @return address
     */
    public String getAddress() {
        return address;
    }
    /**
     * 设置 ip及端口地址，分号分隔地址示例：127.0.0.1:6379;127.0.0.1:6380;127.0.0.1:6381;127.0.0.1:6382;127.0.0.1:6383;127.0.0.1:6384
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * 获取  获取连接时的最大等待毫秒数
     * @return maxWaitMillis
     */
    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }
    /**
     * 设置 获取连接时的最大等待毫秒数
     * @param maxWaitMillis
     */
    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }
    /**
     * 获取  最大连接数
     * @return maxTotal
     */
    public int getMaxTotal() {
        return maxTotal;
    }
    /**
     * 设置 最大连接数
     * @param maxTotal
     */
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
    /**
     * 获取  最小空闲
     * @return minIdle
     */
    public int getMinIdle() {
        return minIdle;
    }
    /**
     * 设置 最小空闲
     * @param minIdle
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }
    /**
     * 获取  最大空闲
     * @return maxIdle
     */
    public int getMaxIdle() {
        return maxIdle;
    }
    /**
     * 设置 最大空闲
     * @param maxIdle
     */
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }
    /**
     * 获取  连接超时时间
     * @return timeout
     */
    public int getTimeout() {
        return timeout;
    }
    /**
     * 设置 连接超时时间
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    /**
     * 获取  超时最大重试次数
     * @return maxRedirections
     */
    public int getMaxRedirections() {
        return maxRedirections;
    }
    /**
     * 设置 超时最大重试次数
     * @param maxRedirections
     */
    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }
    /**
     * 获取  使用数据库，集群模式只能使用database0
     * @return 使用数据库，集群模式只能使用database0
     */
    public int getDatabase() {
        return database;
    }
    /**
     * 设置 使用数据库，集群模式只能使用database0
     * @param  database 使用数据库，集群模式只能使用database0
     */
    public void setDatabase(int database) {
        this.database = database;
    }
    /**
     * 获取  redis模式，1：单机模式（默认）、2：集群模式.
     * @return mode
     */
    public int getMode() {
        return mode;
    }
    /**
     * 设置 redis模式，1：单机模式（默认）、2：集群模式.
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RedisConfiguration [localPropertiesPath=");
        builder.append(localPropertiesPath);
        builder.append(", mode=");
        builder.append(mode);
        builder.append(", address=");
        builder.append(address);
        builder.append(", maxWaitMillis=");
        builder.append(maxWaitMillis);
        builder.append(", maxTotal=");
        builder.append(maxTotal);
        builder.append(", minIdle=");
        builder.append(minIdle);
        builder.append(", maxIdle=");
        builder.append(maxIdle);
        builder.append(", timeout=");
        builder.append(timeout);
        builder.append(", maxRedirections=");
        builder.append(maxRedirections);
        builder.append(", database=");
        builder.append(database);
        builder.append("]");
        return builder.toString();
    }


}
