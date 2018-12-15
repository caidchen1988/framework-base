package com.zwt.framework.zk.client.impl;

import com.zwt.framework.zk.client.ZKClient;
import com.zwt.framework.zk.config.ZKConfig;
import com.zwt.framework.zk.constants.ZKConstants;
import com.zwt.framework.zk.listener.ConnectionListener;
import com.zwt.framework.zk.listener.NodeListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author zwt
 * @detail
 * @date 2018/12/12
 * @since 1.0
 */
public class BaseZKClient implements ZKClient {

    private static final Logger log= LoggerFactory.getLogger(BaseZKClient.class);

    private ZooKeeper client;
    private volatile boolean closed = false;
    private ZKConfig zkConfig;
    private Set<ConnectionListener> connectionListeners = new CopyOnWriteArraySet<>();
    private final ConcurrentMap<String, ConcurrentMap<NodeListener, Watcher>> nodeListeners = new ConcurrentHashMap<>();
    public BaseZKClient(ZKConfig zkConfig) {
        this.zkConfig = zkConfig;
    }
    public ZooKeeper getZooKeeper() {
        return client;
    }

    @Override
    public void start() {
        if(client!=null){
            return;
        }
        if (zkConfig == null || StringUtils.isBlank(zkConfig.getConnectString())) {
            throw new RuntimeException("zk连接信息不能为空");
        }
        if (StringUtils.isBlank(zkConfig.getNameSpace())) {
            throw new RuntimeException("zk命名空间不能为空");
        }

        try{
            client = new ZooKeeper(zkConfig.getConnectString(),zkConfig.getSessionTimeoutMs(),new DefaultWatcher());
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 默认watcher
     */
    private class DefaultWatcher implements Watcher{
        @Override
        public void process(WatchedEvent event) {
            switch (event.getState()){
                case SyncConnected:
                    log.info("zookeeper connected");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean isConnected() {
        return client!=null && client.getState().isConnected();
    }

    @Override
    public void close() {
        if(closed || client ==null){
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
            client.create(path,data,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
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
            client.create(path,data,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
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
            return client.create(path,data,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
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
            return client.create(path,data,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteNode(String path) {
        try {
            client.delete(path,0);
        } catch (KeeperException.NoNodeException e) {
            log.warn(String.format("delete node not exist:%s", path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteNodeWithChildren(String path) {
        try {
            List<String> list=client.getChildren(path,false);
            if(list!=null && list.size()>0){
                for(String str:list){
                    client.delete(str,0);
                }
            }
        } catch (KeeperException.NoNodeException e) {
            log.warn(String.format("delete node not exist:%s", path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getNodes(String path) {
        try {
            return client.getChildren(path,true);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData(String path) {
        try {
            Stat stat = new Stat();
            return client.getData(path,true,stat);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String getStringData(String path) {
        try {
            Stat stat = new Stat();
            return new String(client.getData(path,true,stat), "UTF-8");
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
            client.setData(path,bytes,0);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void setData(String path, String dataStr) {
        try {
            client.setData(path, dataStr.getBytes("UTF-8"),0);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void addNodeListener(String path, NodeListener listener) {
        ConcurrentMap<NodeListener, Watcher> listeners = nodeListeners.get(path);
        if (listeners == null) {
            nodeListeners.putIfAbsent(path, new ConcurrentHashMap<NodeListener, Watcher>());
            listeners = nodeListeners.get(path);
        }
        Watcher watcher = listeners.get(listener);
        if (watcher == null) {
            listeners.putIfAbsent(listener, new BaseWatcherImpl(listener));
            watcher = listeners.get(listener);
        }
        addChildrenCuratorWatcher(path, watcher);
    }

    @Override
    public void removeNodeListener(String path, NodeListener listener) {
        ConcurrentMap<NodeListener, Watcher> listeners = nodeListeners.get(path);
        if (listeners != null) {
            Watcher watcher = listeners.remove(listener);
            if (watcher != null) {
                ((BaseWatcherImpl) watcher).unWatch();
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
            Stat stat = client.exists(path,false);
            if (stat != null) {
                exits = true;
            }
        } catch (Exception e) {
        }
        return exits;
    }

    private void addChildrenCuratorWatcher(final String path, Watcher watcher) {
        try {
            // TODO fixme, both getData() & getChildren() watch will trigger twice NodeDeleted event bugs
            client.getData(path,watcher,new Stat());
            client.getChildren(path,watcher);
        } catch (KeeperException.NoNodeException e) {
            log.warn(String.format("add watcher node not exist:%s", path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private class BaseWatcherImpl implements Watcher {
        private volatile NodeListener listener;
        public BaseWatcherImpl(NodeListener listener) {
            this.listener = listener;
        }
        public void unWatch() {
            this.listener = null;
        }
        @Override
        public void process(WatchedEvent event){
            if (listener != null) {
                log.debug(event.getPath() + " with event " + event.getType());
                switch (event.getType()) {
                    case NodeDataChanged:
                        try {
                            byte[] data = client.getData(event.getPath(),false,new Stat());
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
                            if (event.getType().equals(Event.EventType.NodeDeleted)) {
                                listener.nodeDelete(event.getPath());
                            } else {
                                List<String> nodes = getNodes(event.getPath());
                                if (nodes != null) {
                                    client.getChildren(event.getPath(),false);
                                }
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
