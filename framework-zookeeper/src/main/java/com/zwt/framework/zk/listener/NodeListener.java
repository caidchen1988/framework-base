package com.zwt.framework.zk.listener;

import java.util.List;

/**
 * @author zwt
 * @detail 节点监听接口，获得该节点的变化事件
 * @date 2018/9/4
 * @since 1.0
 */
public interface NodeListener {
    /**
     * 节点变化事件
     * @param path
     * @param nodes
     */
    void nodeChanged(String path, List<String> nodes);
    /**
     * 节点被删除事件
     * @param path
     */
    void nodeDelete(String path);
    /**
     * 节点数据有变化事件
     * @param path
     * @param data
     */
    void dataChanged(String path, byte[] data);

}
