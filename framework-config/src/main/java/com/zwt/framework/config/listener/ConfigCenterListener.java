package com.zwt.framework.config.listener;

/**
 * @author zwt
 * @detail 配置中心监听接口
 * @date 2018/9/5
 * @since 1.0
 */
public interface ConfigCenterListener {
    /**
     * 配置的key有变化触发事件
     * @param key
     * @param value
     */
    public void onChange(String key, String value) ;
}
