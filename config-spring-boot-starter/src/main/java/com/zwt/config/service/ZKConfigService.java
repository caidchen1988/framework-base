package com.zwt.config.service;

import com.zwt.config.autoconfigure.ConfigCenterAutoConfig;
import com.zwt.config.listener.ConfigCenterListenerAdapter;
import com.zwt.config.util.ConfigUtil;
import com.zwt.framework.zk.client.impl.CuratorZKClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zwt
 * @detail zookeeper Service
 * @date 2018/9/5
 * @since 1.0
 */
public class ZKConfigService{
    private static final Logger log= LoggerFactory.getLogger(ZKConfigService.class);
    private static CuratorZKClient zkClient = null;
    final static String configRootPath = ConfigUtil.getConfigCenterPath();

    /**
     * ZKService初始化
     */
    public static void init() {
        //拿到zkClient
        zkClient = ConfigCenterAutoConfig.getBean(CuratorZKClient.class);
        try {
            PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient.getCuratorFramework(), configRootPath, true);
            PathChildrenCacheListener pathChildrenCacheListener = (client,event) -> {
                log.debug("pathChildrenCacheListener eventType：" + event.getType());
                ChildData data = event.getData();
                if(data!=null){
                    String dataStr = new String(data.getData(), "UTF-8");
                    String key = StringUtils.substringAfterLast(data.getPath(), ConfigUtil.SEP_STRING);
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            LocalCacheService.put(key,dataStr);
                            break;
                        case CHILD_UPDATED:
                            LocalCacheService.put(key,dataStr);
                            ConfigCenterListenerAdapter.onChange(key,dataStr);
                            break;
                        case CHILD_REMOVED:
                            LocalCacheService.remove(key);
                            break;
                        default:
                            break;
                    }
                }
            };
            pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("spring-boot-config ZKConfigService init success.");
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public static String getKey(String key) {
        return zkClient.getStringData(ConfigUtil.joinPath(configRootPath, key));
    }

    /**
     * 获取全部数据
     * @return
     */
    public static Map<String, String> getAllKey() {
        Map<String, String> zkConfigMap = new HashMap<>();
        List<String> nodes = zkClient.getNodes(configRootPath);
        for(String node : nodes) {
            zkConfigMap.put(node, zkClient.getStringData(node));
        }
        return zkConfigMap;
    }
}
