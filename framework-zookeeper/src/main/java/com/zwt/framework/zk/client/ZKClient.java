package com.zwt.framework.zk.client;

import com.zwt.framework.zk.listener.ConnectionListener;
import com.zwt.framework.zk.listener.NodeListener;

import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/9/4
 * @since 1.0
 */
public interface ZKClient {
    /**
     * 连接zk
     */
    void start();
    /**
     * 获取zk连接状态
     * @return
     */
    boolean isConnected();
    /**
     * 关闭zk连接
     */
    void close();
    /**
     * 创建持久化节点，如果父节点不存在，会自动创建父节点
     * @param path
     */
    void createNode(String path) ;
    /**
     * 创建持久化节点，如果父节点不存在，会自动创建父节点
     * @param path
     * @param data
     */
    void createNode(String path, byte[] data);
    /**
     * 创建持久化节点，如果父节点不存在，会自动创建父节点
     * @param path
     * @param data
     */
    public void createNode(String path, String data);
    /**
     * 创建或更新持久化节点，如果父节点不存在，会自动创建父节点
     * @param path
     * @param data
     */
    public void createOrUpdateNode(String path, byte[] data);
    /**
     * 创建或更新持久化节点，如果父节点不存在，会自动创建父节点
     * @param path
     * @param data
     */
    public void createOrUpdateNode(String path, String data);
    /**
     * 创建临时节点，如果父节点不存在，会自动创建父节点
     * @param path
     */
    void createEphemeralNode(String path);
    /**
     * 创建临时节点，如果父节点不存在，会自动创建父节点
     * @param path
     * @param data
     */
    void createEphemeralNode(String path, byte[] data);

    /**
     * 创建序列节点(持久化)，如果父节点不存在，会自动创建父节点
     * @param path
     */
    String createSequenceNode(String path) ;
    /**
     * 创建序列节点(持久化)，如果父节点不存在，会自动创建父节点
     * @param path
     * @param data
     */
    String createSequenceNode(String path, byte[] data);
    /**
     * 创建序列节点(临时)，如果父节点不存在，会自动创建父节点
     * @param path
     */
    String createSequenceEphemeralNode(String path);
    /**
     * 创建序列节点(临时)，如果父节点不存在，会自动创建父节点
     * @param path
     * @param data
     */
    String createSequenceEphemeralNode(String path,byte[] data);
    /**
     * 删除节点
     * @param path
     */
    void deleteNode(String path);

    /**
     * 删除节点，并删除其下子节点
     * @param path
     */
    void deleteNodeWithChildren(String path);
    /**
     * 获得节点列表
     * @param path
     * @return
     */
    List<String> getNodes(String path);
    /**
     * 获取节点内容
     * @param path
     * @return
     */
    byte[] getData(String path);
    /**
     * 获取节点字符串内容
     * @param path
     * @return
     */
    String getStringData(String path);
    /**
     * 获取节点Long内容
     * @param path
     * @return
     */
    Long getLongData(String path) ;
    /**
     * 获取节点Integer内容
     * @param path
     * @return
     */
    Integer getIntegerData(String path) ;
    /**
     * 获取节点Boolean内容
     * @param path
     * @return
     */
    Boolean getBooleanData(String path) ;
    /**
     * 设置节点内容
     * @param path
     * @param bytes
     */
    void setData(String path, byte[] bytes);
    /**
     * 设置节点内容
     * @param path
     * @param dataStr
     */
    void setData(String path, String dataStr);
    /**
     * 添加节点监听器
     * @param path
     * @param listener
     */
    void addNodeListener(String path, NodeListener listener);
    /**
     * 移除节点监听器
     * @param path
     * @param listener
     */
    void removeNodeListener(String path, NodeListener listener);
    /**
     * 添加zk连接的监听器
     * @param listener
     */
    void addConnectionListener(ConnectionListener listener);
    /**
     * 移除zk连接状态的监听器
     * @param listener
     */
    void removeConnectionListener(ConnectionListener listener);
    /**
     * 检查节点是否存在
     * @param path
     * @return
     */
    boolean checkNodeExist(String path);
}
