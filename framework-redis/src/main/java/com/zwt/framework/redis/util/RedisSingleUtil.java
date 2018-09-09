package com.zwt.framework.redis.util;

import com.zwt.framework.redis.exception.RedisException;
import com.zwt.framework.redis.factory.JedisFactory;
import com.zwt.framework.redis.factory.RedisConfiguration;
import com.zwt.framework.redis.factory.RedisConstants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * @author zwt
 * @detail 单Redis数据源工具类
 * @date 2018/9/3
 * @since 1.0
 */
public class RedisSingleUtil implements RedisUtil{

    private JedisFactory jedisFactory;

    public void setJedisFactory(JedisFactory jedisFactory){
        this.jedisFactory=jedisFactory;
    }

    @Override
    public JedisFactory getJedisFactory() {
        return jedisFactory;
    }

    public RedisSingleUtil(){
        RedisConfiguration redisConfiguration=new RedisConfiguration();
        redisConfiguration.setLocalPropertiesPath(RedisConstants.DEFAULT_PROPERTIES_PATH);
        this.jedisFactory = new JedisFactory(redisConfiguration);
    }
    public RedisSingleUtil(JedisFactory jedisFactory){
        this.jedisFactory = jedisFactory;
    }
    public RedisSingleUtil(RedisConfiguration redisConfiguration){
        this.jedisFactory = new JedisFactory(redisConfiguration);
    }
    public RedisSingleUtil(String localPropertiesPath){
        RedisConfiguration redisConfiguration=new RedisConfiguration();
        redisConfiguration.setLocalPropertiesPath(localPropertiesPath);
        this.jedisFactory = new JedisFactory(redisConfiguration);
    }


/*---------------------------------BASE----------------------------------------*/

    /**
     * 获取资源
     * @return
     */
    private Jedis getResource(){
        if(jedisFactory==null){
            throw new RedisException("redis配置未初始化");
        }
        Jedis jedis = jedisFactory.getJedisPool().getResource();
        return jedis;
    }

    /**
     * 释放资源
     * @param resource
     */
    private void closeResource(Jedis resource){
        resource.close();
    }


    @Override
    public long expire(String key, int seconds) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis expire key null!");
        }

        Jedis jedis = this.getResource();

        try{
            return jedis.expire(key, seconds);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long expireAt(String key, int unixTimestamp) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis expireAt key null!");
        }

        Jedis jedis = this.getResource();
        try{
            return jedis.expireAt(key, unixTimestamp);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long delKey(String key) {
        Jedis jedis=this.getResource();
        try{
            return jedis.del(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long hdel(String key, String... fields) {
        Jedis jedis = this.getResource();
        try{
            return jedis.hdel(key, fields);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long delKeys(String[] keys) {
        long count=0;
        for(String key:keys){
            count+=this.delKey(key);
        }
        return count;
    }

    @Override
    public Set<String> hkeys(String key) {
        Jedis jedis = this.getResource();
        try{
            return jedis.hkeys(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long incr(String key) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis incr key null!");
        }

        long id=this.getResource().incr(key);
        if ((id + 75807) >= Long.MAX_VALUE) {
            // 避免溢出，重置，getSet命令之前允许incr插队，75807就是预留的插队空间
            Jedis jedis = this.getResource();
            try{
                jedis.getSet(key, "0");
            }finally{
                this.closeResource(jedis);
            }
        }
        return id;
    }

    @Override
    public long incrby(String key, long value) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis incrby key null!");
        }

        Jedis jedis = this.getResource();
        try
        {
            return jedis.incrBy(key, value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long decr(String key) {

        long id = 0;
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis decr key null!");
        }
        Jedis jedis = this.getResource();
        try{
            id = jedis.decr(key);
        }finally{
            this.closeResource(jedis);
        }
        return id;
    }

    @Override
    public long decrby(String key, long value) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis decrby key null!");
        }

        Jedis jedis = this.getResource();
        try{
            return jedis.decrBy(key, value);
        }catch(Exception ex){
            throw new RuntimeException("redis decrby exception :"+ex.getMessage());
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public double incrFloatby(String key, double value) {
        Jedis jedis = getResource();
        try{
            return jedis.incrByFloat(key, value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Boolean exists(String key) {
        if (key == null || key.equals("")) {
            return false;
        }

        Jedis jedis = this.getResource();
        try{
            return jedis.exists(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String setString(String key, String value) {
        Jedis jedis = this.getResource();
        try{
            return jedis.set(key, value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String setString(String key, String value, int expire) {
        Jedis jedis = this.getResource();
        try{
            return jedis.setex(key, expire, value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long setStringIfNotExists(String key, String value) {
        Jedis jedis = this.getResource();
        try{
            return jedis.setnx(key, value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String getString(String key) {
        Jedis jedis = this.getResource();
        try{
            return jedis.get(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long getLong(String key) {

        Jedis jedis = getResource();
        try{
            String num = jedis.get(key);
            if(num == null){
                return 0;
            }
            return Long.parseLong(num);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        Jedis jedis = getResource();
        try{
            return jedis.get(key) == null ? defaultValue : jedis.get(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public List<String> batchSetString(String[] keys, String[] values) {
        if(keys==null||values==null||keys.length!=values.length){
            throw new IllegalArgumentException("keys或values参数不合法！");
        }

        List<String> responseList=new ArrayList<String>();

        for(int i=0;i<keys.length;i++){
            responseList.add(this.setString(keys[i], values[i]));
        }

        return responseList;
    }

    @Override
    public List<String> batchGetString(String[] keys) {
        List<String> responseList=new ArrayList<String>();
        if(keys==null){
            return responseList;
        }
        for(String key:keys){
            responseList.add(this.getString(key));
        }
        return responseList;
    }

    @Override
    public Long hashSet(String key, String field, String value) {
        Jedis jedis = getResource();
        try{
            return jedis.hset(key, field, value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String hashGet(String key, String field) {
        Jedis jedis = getResource();
        try{
            return jedis.hget(key, field);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String hashMultipleSet(String key, Map<String, String> hash) {
        Jedis jedis = getResource();
        try{
            return jedis.hmset(key, hash);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public List<String> hashMultipleGet(String key, String... fields) {
        Jedis jedis = getResource();
        try{
            List<String> list = jedis.hmget(key, fields);
            return list;
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Map<String, String> hashGetAll(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.hgetAll(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long listPushTail(String key, String... values) {
        Jedis jedis = getResource();
        try{
            return jedis.rpush(key, values);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long listPushHead(String key, String... values) {
        Jedis jedis = getResource();
        try{
            return jedis.lpush(key, values);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public List<String> listGetAll(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.lrange(key, 0, -1);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public List<String> listRange(String key, long beginIndex, long endIndex) {
        Jedis jedis = getResource();
        try{
            return jedis.lrange(key, beginIndex, endIndex);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String listPop(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.lpop(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long listLen(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.llen(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Map<String, List<String>> batchGetAllList(String... keys) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        for (String key : keys) {
            List<String> list=this.listGetAll(key);
            result.put(key, list);
        }
        return result;
    }

    @Override
    public String hashBinarySet(byte[] key, Map<byte[], byte[]> hash) {
        Jedis jedis = getResource();
        try{
            return jedis.hmset(key, hash);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public List<byte[]> hashBinaryGet(byte[] key, byte[]... fields) {
        Jedis jedis = getResource();
        try{
            List<byte[]> list = jedis.hmget(key, fields);
            return list;
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public void zadd(String key, double score, String member) {
        Jedis jedis = getResource();
        try{
            jedis.zadd(key, score, member);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public void zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = getResource();
        try{
            jedis.zadd(key, scoreMembers);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long zcard(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.zcard(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long zcount(String key, double min, double max) {
        Jedis jedis = getResource();
        try{
            return jedis.zcount(key, min, max);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = getResource();
        try{
            return jedis.zrange(key, start, end);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        Jedis jedis = getResource();
        try{
            return jedis.zrangeWithScores(key, start, end);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        Jedis jedis = getResource();
        try{
            return jedis.zrangeByScore(key, min, max);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long zrank(String key, String member) {
        Jedis jedis = getResource();
        try{
            Long zrank = jedis.zrank(key, member);
            if(zrank==null){
                return -1;
            }else{
                return zrank.longValue();
            }
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long zrem(String key, String member) {
        Jedis jedis = getResource();
        try{
            return jedis.zrem(key, member);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public double zscore(String key, String member) {
        Jedis jedis = getResource();
        try{
            Double zscore = jedis.zscore(key, member);
            if(zscore==null){
                return -1;
            }else{
                return zscore.doubleValue();
            }
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public double zincrby(String key, double score, String member) {
        Jedis jedis = getResource();
        try{
            return jedis.zincrby(key, score, member);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = getResource();
        try{
            return jedis.zremrangeByRank(key, start, end);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = getResource();
        try{
            return jedis.zremrangeByScore(key, start, end);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = getResource();
        try{
            return jedis.zrevrange(key, start, end);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Jedis jedis = getResource();
        try{
            return jedis.zrevrangeByScore(key, max, min);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long zrevrank(String key, String member) {
        Jedis jedis = getResource();
        try{
            Long zrevrank = jedis.zrevrank(key, member);

            if(zrevrank==null){
                return -1;
            }else{
                return zrevrank.longValue();
            }
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public void sadd(String key, String... members) {
        Jedis jedis = getResource();
        try{
            jedis.sadd(key, members);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long scard(String key, String... members) {
        Jedis jedis = getResource();
        try{
            return jedis.scard(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> sdiff(String... keys) {
        Jedis jedis = getResource();
        try{
            return jedis.sdiff(keys);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long sdiffstore(String dstkey, String... keys) {
        Jedis jedis = getResource();
        try{
            return jedis.sdiffstore(dstkey, keys);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> sinter(String... keys) {
        Jedis jedis = getResource();
        try{
            return jedis.sinter(keys);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long sinterstore(String dstkey, String... keys) {
        Jedis jedis = getResource();
        try{
            return jedis.sinterstore(dstkey, keys);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public boolean sismember(String key, String member) {
        Jedis jedis = getResource();
        try{
            return jedis.sismember(key, member);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.smembers(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long srem(String key, String... members) {
        Jedis jedis = getResource();
        try{
            return jedis.srem(key, members);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> sunion(String... keys) {
        Jedis jedis = getResource();
        try{
            return jedis.sunion(keys);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long sunionstore(String dstkey, String... keys) {
        Jedis jedis = getResource();
        try{
            return jedis.sunionstore(dstkey, keys);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String spop(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.spop(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String srandmember(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.srandmember(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long dbSize() {
        Jedis jedis = getResource();
        try{
            return jedis.dbSize();
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String setByte(String key, byte[] value) {
        Jedis jedis = this.getResource();
        try{
            return jedis.set(key.getBytes(), value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String setByteEx(String key, int seconds, byte[] value) {
        Jedis jedis = this.getResource();
        try{
            return jedis.setex(key.getBytes(), seconds, value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public byte[] getByte(String key) {
        Jedis jedis = this.getResource();
        try{
            return jedis.get(key.getBytes());
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public long delByte(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.del(key.getBytes());
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Map<byte[], byte[]> hashByteGetAll(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.hgetAll(key.getBytes());
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long hashByteDel(byte[] key, byte[]... fields) {
        Jedis jedis = getResource();
        try{
            return jedis.hdel(key, fields);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long delByteKey(byte[] key) {
        Jedis jedis = getResource();
        try{
            return jedis.del(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long zaddHashByte(byte[] key, double score, byte[] member) {
        Jedis jedis = getResource();
        try{
            return jedis.zadd(key, score, member);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long zremrangeByScoreHashByte(byte[] key, double start, double end) {
        Jedis jedis = getResource();
        try{
            return jedis.zremrangeByScore(key, start, end);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long zremHashByte(byte[] key, byte[]... member) {
        Jedis jedis = getResource();
        try{
            return jedis.zrem(key, member);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<byte[]> zrangeByScoreHashByte(byte[] key, double min, double max) {
        Jedis jedis = getResource();
        try{
            return jedis.zrangeByScore(key, min, max);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String info() {
        Jedis jedis = getResource();
        try{
            return jedis.info();
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String info(String section) {
        Jedis jedis = getResource();
        try{
            return jedis.info(section);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String getSet(String key, String value) {
        Jedis jedis = getResource();
        try{
            return jedis.getSet(key,value);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Jedis jedis = getResource();
        try{
            return jedis.zrangeByScore(key, min, max,offset,count);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String lindex(String key, long index) {
        Jedis jedis = getResource();
        try{
            return jedis.lindex(key, index);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public String rpop(String key) {
        Jedis jedis = getResource();
        try{
            return jedis.rpop(key);
        }finally{
            this.closeResource(jedis);
        }
    }

    @Override
    public Long hincrBy(String key, String field, int value) {
        Jedis jedis = getResource();
        try{
            return jedis.hincrBy(key, field, value);
        }finally{
            this.closeResource(jedis);
        }
    }
}
