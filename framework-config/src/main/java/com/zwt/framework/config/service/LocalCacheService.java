package com.zwt.framework.config.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zwt.framework.config.listener.ConfigCenterListenerAdapter;
import com.zwt.framework.config.vo.CacheNodeVo;
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
    private static final ConcurrentHashMap<String, CacheNodeVo> LOCAL_CONFIG_CACHE_MAP = new ConcurrentHashMap<>();
    private static Thread refreshThread;
    private static boolean refreshThreadStop = false;
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("framework-config-%d").setDaemon(true).build();
    private static ExecutorService singleThreadPool = Executors.newFixedThreadPool(1, namedThreadFactory);
    public static void init() {
        // refresh thread
        singleThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                while (!refreshThreadStop) {
                    try {
                        TimeUnit.SECONDS.sleep(60);
                        reloadAll();
                        log.debug("framework-config, refresh thread reloadAll success.");
                    } catch (Exception e) {
                        log.error("framework-config, refresh thread error.");
                        log.error(e.getMessage(), e);
                    }
                }
                log.info("framework-config, refresh thread stoped.");
            }
        });
        log.info("framework-conf LocalCacheService init success.");
    }
    public static String get(String key) {
        CacheNodeVo cacheNodeVo = LOCAL_CONFIG_CACHE_MAP.get(key);
        if (cacheNodeVo != null) {
            return cacheNodeVo.getValue();
        }
        return null;
    }
    public static void put(String key, String value) {
        LOCAL_CONFIG_CACHE_MAP.put(key, new CacheNodeVo(key, value));
    }
    public static void remove(String key) {
        LOCAL_CONFIG_CACHE_MAP.remove(key);
    }
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
