package com.zwt.framework.distributed.lock.impl;

import com.zwt.framework.redis.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * redis 实现的分布式锁
 */
public class RedisDistributedLock {

    //锁前缀
    private final static String LOCK_PREFIX = "REDIS_LOCK_";

    /**
     * 尝试获得锁
     *
     * @param redisUtil   redis
     * @param key         key
     * @param lockTimeOut 超时时间(毫秒)
     * @return 大于 0  获得到锁并,等于0获取锁失败
     */
    public long tryLock(RedisUtil redisUtil, String key, long lockTimeOut) {
        key = LOCK_PREFIX + key;
        long expireTime = 0;
        expireTime = System.currentTimeMillis() + lockTimeOut + 1;
        if (redisUtil.setStringIfNotExists(key, String.valueOf(expireTime)) == 1) {
            return expireTime;
        } else {
            String curLockTimeStr = redisUtil.getString(key);
            //判断是否过期
            if (StringUtils.isBlank(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                expireTime = System.currentTimeMillis() + lockTimeOut + 1;
                curLockTimeStr = redisUtil.getSet(key, String.valueOf(expireTime));
                //仍然过期,则得到锁
                if (StringUtils.isBlank(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                    return expireTime;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }

    /**
     * 一直等待获得锁
     *
     * @param redisUtil   redis
     * @param key         key
     * @param lockTimeOut 超时时间(毫秒)
     * @param perSleep    获得锁循环等待休眠时间
     * @return 大于 0  获得到锁并,等于0获取锁失败
     * @throws InterruptedException
     */
    public long lock(RedisUtil redisUtil, String key, long lockTimeOut, long perSleep) throws InterruptedException {
        key = LOCK_PREFIX + key;
        long starttime = System.currentTimeMillis();
        long sleep = (perSleep == 0 ? lockTimeOut / 10 : perSleep);
        //得到锁后设置的过期时间，未得到锁返回0
        long expireTime = 0;
        for (; ; ) {
            expireTime = System.currentTimeMillis() + lockTimeOut + 1;
            if (redisUtil.setStringIfNotExists(key, String.valueOf(expireTime)) == 1) {
                //得到了锁返回
                return expireTime;
            } else {
                String curLockTimeStr = redisUtil.getString(key);
                //判断是否过期
                if (StringUtils.isBlank(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                    expireTime = System.currentTimeMillis() + lockTimeOut + 1;
                    curLockTimeStr = redisUtil.getSet(key, String.valueOf(expireTime));
                    //仍然过期,则得到锁
                    if (StringUtils.isBlank(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                        return expireTime;
                    } else {
                        Thread.sleep(sleep);
                    }
                } else {
                    Thread.sleep(sleep);
                }
            }
            if (lockTimeOut > 0 && ((System.currentTimeMillis() - starttime) >= lockTimeOut)) {
                expireTime = 0;
                return expireTime;
            }
        }
    }

    /**
     * 释放锁
     *
     * @param redisUtil  redis
     * @param key        key
     * @param expireTime 超时时间
     */
    public void unlock(RedisUtil redisUtil, String key, long expireTime) {
        key = LOCK_PREFIX + key;
        if (System.currentTimeMillis() - expireTime > 0) {
            return;
        }
        String curLockTimeStr = redisUtil.getString(key);
        if (StringUtils.isNotBlank(curLockTimeStr) && Long.valueOf(curLockTimeStr) > System.currentTimeMillis()) {
            redisUtil.delKey(key);
        }
    }
}
