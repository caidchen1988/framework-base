package com.zwt.framework.redis.helper.configcenter;

import com.zwt.framework.redis.util.RedisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zwt
 * @detail Redis配置中心
 * @date 2019/4/15
 * @since 1.0
 */
public class RedisConfigCenter {

    /**
     * 配置中心key
     */
    public static final String CONFIG_CENTER_KEY = "redis.configcenter:hash:key";

    //Redis 工具
    private RedisUtil redisUtil;

    public RedisConfigCenter(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    /**
     * 配置中心添加数据
     * 可以添加一个或多个
     * @return
     */
    public boolean insertData(Map<String,String> insertData){
        //放入Redis hash表
        boolean exists = redisUtil.exists(CONFIG_CENTER_KEY);
        if(exists){
            //拿到key对应的Redis hash表数据
            Map<String,String> redisMap = redisUtil.hashGetAll(CONFIG_CENTER_KEY);
            insertData.putAll(redisMap);
        }
        //写入Redis hash表
        redisUtil.hashMultipleSet(CONFIG_CENTER_KEY,insertData);

        //数据库处理部分代码略
        return true;
    }

    /**
     * 配置中心更新数据
     * @param updateData
     * @return
     */
    public boolean updateData(Map<String,String> updateData){
        boolean exists = redisUtil.exists(CONFIG_CENTER_KEY);
        if(!exists){
            throw new RuntimeException("请先新增数据!");
        }
        //拿到key对应的Redis hash表数据
        Map<String,String> redisMap = redisUtil.hashGetAll(CONFIG_CENTER_KEY);
        redisMap.putAll(updateData);
        //写入Redis hash表
        redisUtil.hashMultipleSet(CONFIG_CENTER_KEY,updateData);
        //数据库处理部分代码略
        return true;
    }

    /**
     * 配置中心删除数据
     * @param deleteKeys
     * @return
     */
    public boolean deleteData(List<String> deleteKeys){
        boolean exists = redisUtil.exists(CONFIG_CENTER_KEY);
        if(!exists){
            throw new RuntimeException("请先新增数据!");
        }
        //拿到key对应的Redis hash表数据
        Map<String,String> redisMap = redisUtil.hashGetAll(CONFIG_CENTER_KEY);
        deleteKeys.forEach(key->{
            redisMap.remove(key);
        });
        //写入Redis hash表
        redisUtil.hashMultipleSet(CONFIG_CENTER_KEY,redisMap);
        //数据库处理部分代码略
        return true;
    }

    /**
     * 查询数据列表
     * @return
     */
    public Map<String,String> selectData(){
        Map<String,String> map = new HashMap<>();
        boolean exists = redisUtil.exists(CONFIG_CENTER_KEY);
        if(!exists){
            return map;
        }
        map = redisUtil.hashGetAll(CONFIG_CENTER_KEY);
        return map;
    }

    /**
     * 将数据库数据刷新到Redis
     * @return
     */
    public boolean refreshToRedis(){
        //数据库获取到数据集合 略
        Map<String,String> map = new HashMap<>();//TODO
        redisUtil.hashMultipleSet(CONFIG_CENTER_KEY,map);
        return true;
    }

    /**
     * Redis刷新到数据库
     * @return
     */
    public boolean refreshToDataBase(){
        Map<String,String> map = redisUtil.hashGetAll(CONFIG_CENTER_KEY);
        //数据库操作，略
        return true;
    }


    /**
     * 可以改为配置的形式
     */
    private static boolean refreshRedisThreadStop = false;
    private static boolean refreshDataBaseThreadStop = false;
    /**
     * 刷新数据的线程
     */
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * 初始化
     */
    public void init() {
        // refresh thread
        executorService.submit(() ->{
            while (!refreshRedisThreadStop) {
                try {
                    //30 min 刷新一次
                    TimeUnit.MINUTES.sleep(30);
                    refreshToRedis();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.submit(() ->{
            while (!refreshDataBaseThreadStop) {
                try {
                    //30 min 刷新一次
                    TimeUnit.MINUTES.sleep(30);
                    refreshToDataBase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

