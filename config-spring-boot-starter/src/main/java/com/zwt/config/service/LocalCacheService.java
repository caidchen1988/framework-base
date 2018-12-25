package com.zwt.config.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zwt.config.listener.ConfigCenterListenerAdapter;
import com.zwt.config.vo.CacheNodeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.*;

/**
 * @author zwt
 * @detail 本地缓存服务
 * @date 2018/9/5
 * @since 1.0
 */
public class LocalCacheService {
    private static final Logger log= LoggerFactory.getLogger(LocalCacheService.class);
    //配置缓存信息map
    private static final ConcurrentHashMap<String, CacheNodeVo> LOCAL_CONFIG_CACHE_MAP = new ConcurrentHashMap<>();
    //刷新线程状态
    private static boolean refreshThreadStop = false;
    //一个单线程的线程池（刷新缓存使用）
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("spring-boot-config-%d").setDaemon(true).build();
    private static ExecutorService singleThreadPool = Executors.newFixedThreadPool(1, namedThreadFactory);
    //一分钟刷新一次
    public static void init() {
        // refresh thread
        singleThreadPool.submit(()->{
            while (!refreshThreadStop) {
                try {
                    TimeUnit.SECONDS.sleep(60);
                    reloadAll();
                    log.debug("spring-boot-config, refresh thread reloadAll success.");
                } catch (Exception e) {
                    log.error("spring-boot-config, refresh thread error.");
                    log.error(e.getMessage(), e);
                }
            }
            log.info("spring-boot-config, refresh thread stopped.");
        });
        log.info("spring-boot-config LocalCacheService init success.");
    }

    /**
     * 通过key获取值
     * @param key
     * @return
     */
    public static String get(String key) {
        CacheNodeVo cacheNodeVo = LOCAL_CONFIG_CACHE_MAP.get(key);
        if (cacheNodeVo != null) {
            return cacheNodeVo.getValue();
        }
        return null;
    }

    /**
     * 放入值
     * @param key
     * @param value
     */
    public static void put(String key, String value) {
        LOCAL_CONFIG_CACHE_MAP.put(key, new CacheNodeVo(key, value));
    }

    /**
     * 移除某个值
     * @param key
     */
    public static void remove(String key) {
        LOCAL_CONFIG_CACHE_MAP.remove(key);
    }

    /**
     * 重新加载全部
     */
    private static void reloadAll() {
        Set<String> keySet = LOCAL_CONFIG_CACHE_MAP.keySet();
        if (keySet.size() > 1) {
            for(String key : keySet) {
                String zkValue = ZKConfigService.getKey(key);
                CacheNodeVo cacheNodeVo = LOCAL_CONFIG_CACHE_MAP.get(key);
                if (cacheNodeVo != null && cacheNodeVo.getValue() != null && cacheNodeVo.getValue().equals(zkValue)) {
                    log.debug("refresh key:{} no changed ", key);
                } else {
                    LOCAL_CONFIG_CACHE_MAP.put(key, new CacheNodeVo(key, zkValue));
                    ConfigCenterListenerAdapter.onChange(key, zkValue);
                }
            }
        }
    }
}
