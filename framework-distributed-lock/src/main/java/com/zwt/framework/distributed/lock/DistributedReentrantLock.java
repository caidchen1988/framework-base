package com.zwt.framework.distributed.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author zwt
 * @detail 分布式可重入锁
 * @date 2018/12/28
 * @since 1.0
 */
public interface DistributedReentrantLock {

    /**
     * 获得锁
     * @return
     * @throws InterruptedException
     */
    boolean tryLock() throws InterruptedException;

    /**
     * 获得锁
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * 解除锁
     */
    void unlock();
}
