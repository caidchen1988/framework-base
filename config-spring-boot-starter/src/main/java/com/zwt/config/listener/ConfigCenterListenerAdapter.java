package com.zwt.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zwt
 * @detail 配置中心监听Adapter
 * @date 2018/9/5
 * @since 1.0
 */
public class ConfigCenterListenerAdapter {
    private static Logger log= LoggerFactory.getLogger(ConfigCenterListenerAdapter.class);

    private static ConcurrentHashMap<String, List<ConfigCenterListener>> someKeyListenerMap = new ConcurrentHashMap<>();
    private static List<ConfigCenterListener> allKeyListeners = new CopyOnWriteArrayList<>();
    public static boolean addListener(String key, ConfigCenterListener configCenterListener) {
        if (configCenterListener == null) {
            return false;
        }
        if (key == null || key.trim().length() == 0) {
            allKeyListeners.add(configCenterListener);
            return true;
        } else {
            List<ConfigCenterListener> listeners = someKeyListenerMap.get(key);
            if (listeners == null) {
                listeners = new ArrayList<>();
                someKeyListenerMap.put(key, listeners);
            }
            listeners.add(configCenterListener);
            return true;
        }
    }
    public static void onChange(String key, String value) {
        if (key == null || key.trim().length() == 0) {
            return;
        }
        List<ConfigCenterListener> keyListeners = someKeyListenerMap.get(key);
        if (keyListeners != null && keyListeners.size() > 0) {
            for(ConfigCenterListener listener : keyListeners) {
                try {
                    listener.onChange(key, value);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        if (allKeyListeners.size() > 0) {
            for(ConfigCenterListener confListener : allKeyListeners) {
                try {
                    confListener.onChange(key, value);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
