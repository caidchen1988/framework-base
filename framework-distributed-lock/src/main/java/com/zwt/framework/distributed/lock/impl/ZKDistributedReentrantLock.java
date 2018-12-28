package com.zwt.framework.distributed.lock.impl;

import com.zwt.framework.distributed.lock.DistributedReentrantLock;
import com.zwt.framework.zk.client.impl.CuratorZKClient;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zwt
 * @detail 分布式锁Zookeeper实现
 * @date 2018/12/28
 * @since 1.0
 */
public class ZKDistributedReentrantLock implements DistributedReentrantLock {

    private static final Logger logger = LoggerFactory.getLogger(ZKDistributedReentrantLock.class);

    /**
     * 线程池
     */
    private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(
            10,
            new BasicThreadFactory.Builder().namingPattern("scheduled-pool-%d").build()
    );
    /**
     * 所有锁的根节点
     */
    public static final String ROOT_PATH = "/LOCK/";

    /**
     * 每次延迟清理PERSISTENT节点的时间  毫秒
     */
    private static final long DELAY_TIME_FOR_CLEAN = 1000;

    /**
     * zk 共享锁实现
     */
    private InterProcessMutex interProcessMutex;

    /**
     * 锁的ID,对应zk一个PERSISTENT节点,下挂EPHEMERAL节点.
     */
    private String path;

    /**
     * zk的客户端
     */
    private CuratorFramework client;

    private volatile boolean isLockSuccess;

    public ZKDistributedReentrantLock(CuratorFramework client, String lockId) {
        this.client = client;
        this.path = ROOT_PATH + lockId;
        interProcessMutex = new InterProcessMutex(client, this.path);
    }

    public ZKDistributedReentrantLock(CuratorZKClient zkClient, String lockId) {
        this.client = zkClient.getCuratorFramework();
        this.path = ROOT_PATH + lockId;
        interProcessMutex = new InterProcessMutex(client, this.path);
    }
    /**
     * 获取锁
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock() throws InterruptedException {
        return tryLock(-1, null);
    }

    /**
     * 获取锁
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        try {
            isLockSuccess = interProcessMutex.acquire(timeout, unit);
            logger.debug("{} lock result:{}",this.path,isLockSuccess);
            return isLockSuccess;
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }
    
    /**
     * 释放锁
     */
    @Override
    public void unlock() {
        if(isLockSuccess) {
            try {
                isLockSuccess = false;
                interProcessMutex.release();
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            } finally {
                executorService.schedule(new Cleaner(client, path), DELAY_TIME_FOR_CLEAN, TimeUnit.MILLISECONDS);
            }
            logger.debug("{} success unlock.",this.path);
        }
    }

    static class Cleaner implements Runnable {
        private CuratorFramework client;
        private String path;

        public Cleaner(CuratorFramework client, String path) {
            this.client = client;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                List list = client.getChildren().forPath(path);
                if (list == null || list.isEmpty()) {
                    client.delete().forPath(path);
                }
            } catch (KeeperException.NoNodeException | KeeperException.NotEmptyException e1) {
                //nothing
            } catch (Exception e) {
                //准备删除时,正好有线程创建锁
                logger.error(e.getMessage(), e);
            }
        }
    }
}
