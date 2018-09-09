package com.zwt.framework.zk.client.impl;

import com.zwt.framework.zk.client.ZKClient;
import com.zwt.framework.zk.config.ZKConfig;
import com.zwt.framework.zk.listener.ConnectionListener;
import com.zwt.framework.zk.listener.NodeListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author zwt
 * @detail zookeeper客户端
 * @date 2018/9/4
 * @since 1.0
 */
public class CuratorZKClient implements ZKClient {

    private static final Logger log= LoggerFactory.getLogger(CuratorZKClient.class);

    private CuratorFramework client;
    private volatile boolean closed = false;
    private ZKConfig zkConfig;
    private Set<ConnectionListener> connectionListeners = new CopyOnWriteArraySet<>();
    private final ConcurrentMap<String, ConcurrentMap<NodeListener, CuratorWatcher>> nodeListeners = new ConcurrentHashMap<>();
    public CuratorZKClient(ZKConfig zkConfig) {
        this.zkConfig = zkConfig;
    }
    public CuratorFramework getCuratorFramework() {
        return client;
    }


    @Override
    public void start() {
        if (zkConfig == null || StringUtils.isBlank(zkConfig.getConnectString())) {
            throw new RuntimeException("zk连接信息不能为空");
        }
        if (StringUtils.isBlank(zkConfig.getNameSpace())) {
            throw new RuntimeException("zk命名空间不能为空");
        }
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConfig.getConnectString())
                .namespace(zkConfig.getNameSpace())
                //重试指定的次数, 且每一次重试之间停顿的时间逐渐增加
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getRetryInterval(), Integer.MAX_VALUE))
                .connectionTimeoutMs(zkConfig.getConnectTimeoutMs())
                .sessionTimeoutMs(zkConfig.getSessionTimeoutMs());
        client = builder.build();
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState zkConnectionState) {
                ConnectionListener.State state = toConnectionListenerState(zkConnectionState);
                if (state != null) {
                    for(ConnectionListener connectionListener : connectionListeners) {
                        connectionListener.stateChanged(state);
                    }
                }
            }
            private ConnectionListener.State toConnectionListenerState(ConnectionState zkConnectionState) {
                switch (zkConnectionState) {
                    case LOST:
                        return ConnectionListener.State.DISCONNECTED;
                    case SUSPENDED:
                        return ConnectionListener.State.DISCONNECTED;
                    case CONNECTED:
                        return ConnectionListener.State.CONNECTED;
                    case RECONNECTED:
                        return ConnectionListener.State.RECONNECTED;
                    default:
                        return null;
                }
            }
        });
        client.start();
    }

    @Override
    public boolean isConnected() {
        return client != null && client.getZookeeperClient().isConnected();
    }

    @Override
    public void close() {
        if (closed || client == null) {
            return;
        }
        synchronized (this) {
            try {
                client.close();
                closed = true;
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        }
    }

    @Override
    public void createNode(String path) {
        createNode(path, new byte[0]);
    }

    @Override
    public void createNode(String path, byte[] data) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data);
        } catch (KeeperException.NodeExistsException e) {
            log.warn(String.format("create node is exist:%s", path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void createNode(String path, String data) {
        try {
            createNode(path, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void createOrUpdateNode(String path, byte[] data) {
        try {
            if(checkNodeExist(path)){
                setData(path,data);
            }else{
                createNode(path, data);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void createOrUpdateNode(String path, String data) {
        try {
            createOrUpdateNode(path, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void createEphemeralNode(String path) {
        createEphemeralNode(path, new byte[0]);
    }

    @Override
    public void createEphemeralNode(String path, byte[] data) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data);
        } catch (KeeperException.NodeExistsException e) {
            log.warn(String.format("create node is exist:%s", path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String createSequenceNode(String path) {
        return createSequenceNode(path, new byte[0]);
    }

    @Override
    public String createSequenceNode(String path, byte[] data) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, data);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String createSequenceEphemeralNode(String path) {
        return createSequenceEphemeralNode(path, new byte[0]);
    }

    @Override
    public String createSequenceEphemeralNode(String path, byte[] data) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, data);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteNode(String path) {
        try {
            client.delete().guaranteed().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            log.warn(String.format("delete node not exist:%s", path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteNodeWithChildren(String path) {
        try {
            client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            log.warn(String.format("delete node not exist:%s", path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getNodes(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData(String path) {
        try {
            return client.getData().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String getStringData(String path) {
        try {
            return new String(client.getData().forPath(path), "UTF-8");
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Long getLongData(String path) {
        try {
            String stringData = getStringData(path);
            return (stringData==null?null:Long.valueOf(stringData));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Integer getIntegerData(String path) {
        try {
            String stringData = getStringData(path);
            return (stringData==null?null:Integer.valueOf(stringData));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean getBooleanData(String path) {
        try {
            String stringData = getStringData(path);
            return (stringData==null?null:Boolean.valueOf(stringData));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void setData(String path, byte[] bytes) {
        try {
            client.setData().forPath(path, bytes);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void setData(String path, String dataStr) {
        try {
            client.setData().forPath(path, dataStr.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void addNodeListener(String path, NodeListener listener) {
        ConcurrentMap<NodeListener, CuratorWatcher> listeners = nodeListeners.get(path);
        if (listeners == null) {
            nodeListeners.putIfAbsent(path, new ConcurrentHashMap<NodeListener, CuratorWatcher>());
            listeners = nodeListeners.get(path);
        }
        CuratorWatcher watcher = listeners.get(listener);
        if (watcher == null) {
            listeners.putIfAbsent(listener, new CuratorWatcherImpl(listener));
            watcher = listeners.get(listener);
        }
        addChildrenCuratorWatcher(path, watcher);
    }

    @Override
    public void removeNodeListener(String path, NodeListener listener) {
        ConcurrentMap<NodeListener, CuratorWatcher> listeners = nodeListeners.get(path);
        if (listeners != null) {
            CuratorWatcher watcher = listeners.remove(listener);
            if (watcher != null) {
                ((CuratorWatcherImpl) watcher).unWatch();
            }
        }
    }

    @Override
    public void addConnectionListener(ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    @Override
    public void removeConnectionListener(ConnectionListener listener) {
        connectionListeners.remove(listener);
    }

    @Override
    public boolean checkNodeExist(String path) {
        boolean exits = false;
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat != null) {
                exits = true;
            }
        } catch (Exception e) {
        }
        return exits;
    }


    /**
     * 开启事务
     */
    public CuratorTransaction startTransaction() {
        return client.inTransaction();
    }
    /**
     * 事务中添加create操作
     */
    public CuratorTransactionFinal addCreateToTransaction(CuratorTransaction transaction, String path) throws Exception {
        return transaction.create().forPath(path, new byte[0]).and();
    }
    /**
     * 事务中添加delete操作
     */
    public CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction transaction, String path) throws Exception {
        return transaction.delete().forPath(path).and();
    }
    /**
     * 事务中添加seData操作
     */
    public CuratorTransactionFinal addSetDataToTransaction(CuratorTransaction transaction, String path, byte[] data) throws Exception {
        return transaction.setData().forPath(path, data).and();
    }
    /**
     * 提交事务
     */
    public Collection<CuratorTransactionResult> commitTransaction(CuratorTransactionFinal transaction) throws Exception {
        return transaction.commit();
    }

    /**
     * 增加目录watcher
     *
     * @param path
     * @param watcher
     */
    private void addChildrenCuratorWatcher(final String path, CuratorWatcher watcher) {
        try {
            // TODO fixme, both getData() & getChildren() watch will trigger twice NodeDeleted event bugs
            client.getData().usingWatcher(watcher).forPath(path);
            client.getChildren().usingWatcher(watcher).forPath(path);
        } catch (KeeperException.NoNodeException e) {
            log.warn(String.format("add watcher node not exist:%s", path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private class CuratorWatcherImpl implements CuratorWatcher {
        private volatile NodeListener listener;
        public CuratorWatcherImpl(NodeListener listener) {
            this.listener = listener;
        }
        public void unWatch() {
            this.listener = null;
        }
        @Override
        public void process(WatchedEvent event) throws Exception {
            if (listener != null) {
                log.debug(event.getPath() + " with event " + event.getType());
                switch (event.getType()) {
                    case NodeDataChanged:
                        try {
                            byte[] data = client.getData().usingWatcher(this).forPath(event.getPath());
                            log.debug(event.getPath() + " data after change: " + new String(data));
                            listener.dataChanged(event.getPath(), data);
                        } catch (Exception e) {
                            log.warn(e.getMessage(), e);
                        }
                        break;
                    case NodeDeleted:
                    case NodeCreated:
                        log.error(event.getPath());
                    case NodeChildrenChanged:
                        try {
                            if (event.getType().equals(Watcher.Event.EventType.NodeDeleted)) {
                                listener.nodeDelete(event.getPath());
                            } else {
                                List<String> nodes = getNodes(event.getPath());
                                if (nodes != null) {
                                    client.getChildren().usingWatcher(this).forPath(event.getPath());
                                }
                                //监控子节点数据变化
                                //for(String node : nodes) {
                                //    client.getData().usingWatcher(this).forPath( ZKPathMgr.joinPath(event.getPath(),node));
                                //}
                                log.debug(event.getPath() + " nodes after change: " + nodes);
                                listener.nodeChanged(event.getPath(), nodes);
                            }
                        } catch (KeeperException.NoNodeException e) {
                            log.warn(e.getMessage());
                        } catch (Exception e) {
                            log.warn(e.getMessage(), e);
                        }
                        break;
                    case None:
                    default:
                        break;
                }
            }
        }
    }
}
