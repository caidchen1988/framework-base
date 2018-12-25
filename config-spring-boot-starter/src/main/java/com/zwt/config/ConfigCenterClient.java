package com.zwt.config;

import com.zwt.config.exception.ConfigCenterException;
import com.zwt.config.listener.ConfigCenterListener;
import com.zwt.config.listener.ConfigCenterListenerAdapter;
import com.zwt.config.service.LocalCacheService;
import com.zwt.config.service.ZKConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zwt
 * @detail 配置中心客户端
 * @date 2018/9/5
 * @since 1.0
 */
public class ConfigCenterClient {

    private static final Logger log= LoggerFactory.getLogger(ConfigCenterClient.class);
    static {
        LocalCacheService.init();
        ZKConfigService.init();
    }
    public static String getString(String key, String defaultValue) {
        //查询本地缓存
        String value = LocalCacheService.get(key);
        if (value != null) {
            log.debug("get config {} from cache",key);
            return value;
        }

        //没有命中，查询zk中的值，并加入到缓存中，并加watcher
        value = ZKConfigService.getKey(key);
        if (value != null) {
            log.debug("get config {} from zookeeper",key);
            LocalCacheService.put(key,value);
            return value;
        }
        return defaultValue;
    }

    public static String getString(String key) {
        return getString(key, null);
    }
    private static void checkNull(String key,String value) {
        if (value == null) {
            throw new ConfigCenterException(String.format("config key [%s] does not exist",key));
        }
    }
    public static long getLong(String key) {
        String value = getString(key, null);
        checkNull(key, value);
        return Long.valueOf(value);
    }

    public static int getInt(String key) {
        String value = getString(key, null);
        checkNull(key, value);
        return Integer.valueOf(value);
    }

    public static boolean getBoolean(String key) {
        String value = getString(key, null);
        checkNull(key, value);
        return Boolean.valueOf(value);
    }

    public static boolean addListener(String key, ConfigCenterListener configCenterListener){
        return ConfigCenterListenerAdapter.addListener(key, configCenterListener);
    }
}
