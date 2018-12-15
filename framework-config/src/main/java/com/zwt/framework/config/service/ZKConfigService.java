package com.zwt.framework.config.service;

import com.zwt.framework.config.env.ConfigCenterEnv;
import com.zwt.framework.config.listener.ConfigCenterListenerAdapter;
import com.zwt.framework.config.path.ZKPathMgr;
import com.zwt.framework.zk.client.impl.CuratorZKClient;
import com.zwt.framework.zk.config.ZKConfig;
import com.zwt.framework.zk.constants.ZKConstants;
import com.zwt.framework.zk.listener.ConnectionListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
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
public class ZKConfigService {
    private static final Logger log= LoggerFactory.getLogger(ZKConfigService.class);
    private static CuratorZKClient zkClient = null;
    private final static String ZK_CONFIG_ROOT_PATH = "/config";
    public static void init() {
        ZKConfig zkConfig = new ZKConfig();
        zkConfig.setConnectString(ConfigCenterEnv.ZK_ADDRESS);
        zkConfig.setNameSpace(ConfigCenterEnv.ZK_NAMESPACE);
        zkConfig.setNameSpace(ConfigCenterEnv.ZK_NAMESPACE);
        zkClient = new CuratorZKClient(zkConfig);
        zkClient.addConnectionListener(new ConnectionListener() {
            @Override
            public void stateChanged(ZKConstants.State state) {
                log.debug("ZKConfigService connectionListener state：" + state);
                if (state == ZKConstants.State.CONNECTED || state == ZKConstants.State.RECONNECTED) {
                    log.info("ZKConfigService zookeeper is connected...");
                }
            }
        });
        zkClient.start();
        try {
            PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient.getCuratorFramework(), ZK_CONFIG_ROOT_PATH, true);
            PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client1, PathChildrenCacheEvent event) throws Exception {
                    log.debug("pathChildrenCacheListener 事件类型：" + event.getType());
                    ChildData data = event.getData();
                    if(data!=null){
                        String dataStr = new String(data.getData(), "UTF-8");
                        String key = StringUtils.substringAfterLast(data.getPath(), "/");
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
                }
            };
            pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("framework-conf ZKConfigService init success.");
    }

    public static String getKey(String key) {
        return zkClient.getStringData(ZKPathMgr.joinPath(ZK_CONFIG_ROOT_PATH, key));
    }

    public static Map<String, String> getAllKey() {
        Map<String, String> zkConfigMap = new HashMap<>();
        List<String> nodes = zkClient.getNodes(ZK_CONFIG_ROOT_PATH);
        for(String node : nodes) {
            zkConfigMap.put(node, zkClient.getStringData(node));
        }
        return zkConfigMap;
    }
}
