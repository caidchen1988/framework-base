package com.zwt.framework.redis.factory;

/**
 * @author zwt
 * @detail Redis部分常量
 * @date 2018/9/3
 * @since 1.0
 */
public class RedisConstants {
    /**Redis单机模式*/
    public final static int REDIS_MODE_SINGLE=1;
    /**Redis集群模式*/
    public final static int REDIS_MODE_CLUSTER=2;
    /**REDIS默认配置文件*/
    public final static String DEFAULT_PROPERTIES_PATH="property/redis-config.properties";
}
