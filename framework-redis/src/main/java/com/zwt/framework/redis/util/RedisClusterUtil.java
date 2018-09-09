package com.zwt.framework.redis.util;

import com.zwt.framework.redis.exception.RedisException;
import com.zwt.framework.redis.factory.JedisFactory;
import com.zwt.framework.redis.factory.RedisConfiguration;
import com.zwt.framework.redis.factory.RedisConstants;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * @author zwt
 * @detail 集群Redis工具类
 * @date 2018/9/3
 * @since 1.0
 */
public class RedisClusterUtil implements RedisUtil{
    private JedisFactory jedisFactory;

    /**
     * 设置 jedisClusterFactory
     * @param jedisFactory
     */
    public void setJedisClusterFactory(JedisFactory jedisFactory) {
        this.jedisFactory = jedisFactory;
    }



    public RedisClusterUtil(){
        RedisConfiguration redisConfiguration=new RedisConfiguration();
        redisConfiguration.setLocalPropertiesPath(RedisConstants.DEFAULT_PROPERTIES_PATH);
        this.jedisFactory = new JedisFactory(redisConfiguration);
    }

    public RedisClusterUtil(JedisFactory jedisFactory){
        this.jedisFactory = jedisFactory;
    }

    public RedisClusterUtil(RedisConfiguration redisConfiguration){
        this.jedisFactory = new JedisFactory(redisConfiguration);
    }

    public RedisClusterUtil(String localPropertiesPath){
        RedisConfiguration redisConfiguration=new RedisConfiguration();
        redisConfiguration.setLocalPropertiesPath(localPropertiesPath);
        this.jedisFactory = new JedisFactory(redisConfiguration);
    }

    @Override
    public JedisFactory getJedisFactory() {
        return jedisFactory;
    }

    private JedisCluster getResource(){
        if(jedisFactory==null){
            throw new RedisException("redis配置未初始化");
        }
        return jedisFactory.getJedisCluster();
    }

    /*-----------------------------------------BASE---------------------------------------------*/


    @Override
    public long expire(String key, int seconds) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis expire key null!");
        }

        return getResource().expire(key, seconds);
    }

    @Override
    public long expireAt(String key, int unixTimestamp) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis expireAt key null!");
        }
        return getResource().expireAt(key, unixTimestamp);
    }

    @Override
    public Long delKey(String key) {
        return getResource().del(key);
    }

    @Override
    public Long hdel(String key, String... fields) {
        return getResource().hdel(key, fields);
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
        return getResource().hkeys(key);
    }

    @Override
    public long incr(String key) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis incr key null!");
        }

        long id=this.getResource().incr(key);
        if ((id + 75807) >= Long.MAX_VALUE) {
            // 避免溢出，重置，getSet命令之前允许incr插队，75807就是预留的插队空间
            getResource().getSet(key, "0");
        }
        return id;
    }

    @Override
    public long incrby(String key, long value) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis incrby key null!");
        }

        return getResource().incrBy(key, value);
    }

    @Override
    public long decr(String key) {

        long id = 0;
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis decr key null!");
        }

        id = getResource().decr(key);

        return id;
    }

    @Override
    public long decrby(String key, long value) {
        if (key == null || key.equals("")) {
            throw new RuntimeException("redis decrby key null!");
        }

        try{
            return getResource().decrBy(key, value);
        }catch(Exception ex){
            throw new RuntimeException("redis decrby exception :"+ex.getMessage());
        }
    }

    @Override
    public double incrFloatby(String key, double value) {
        return getResource().incrByFloat(key, value);
    }

    @Override
    public Boolean exists(String key) {
        if (key == null || key.equals("")) {
            return false;
        }

        return getResource().exists(key);
    }

    @Override
    public String setString(String key, String value) {
        JedisCluster cluster=getResource();
        return getResource().set(key, value);
    }

    @Override
    public String setString(String key, String value, int expire) {
        return getResource().setex(key, expire, value);
    }

    @Override
    public Long setStringIfNotExists(String key, String value) {
        return getResource().setnx(key, value);
    }

    @Override
    public String getString(String key) {
        return getResource().get(key);
    }

    @Override
    public long getLong(String key) {

        String num = getResource().get(key);
        if(num==null){
            return 0;
        }
        return Long.parseLong(num);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getResource().get(key) == null ? defaultValue : getResource().get(key);
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
        return getResource().hset(key, field, value);
    }

    @Override
    public String hashGet(String key, String field) {
        return getResource().hget(key, field);
    }

    @Override
    public String hashMultipleSet(String key, Map<String, String> hash) {
        return getResource().hmset(key, hash);
    }

    @Override
    public List<String> hashMultipleGet(String key, String... fields) {
        List<String> list = getResource().hmget(key, fields);
        return list;
    }

    @Override
    public Map<String, String> hashGetAll(String key) {
        return getResource().hgetAll(key);
    }

    @Override
    public Long listPushTail(String key, String... values) {
        return getResource().rpush(key, values);
    }

    @Override
    public Long listPushHead(String key, String... values) {
        return getResource().lpush(key, values);
    }

    @Override
    public List<String> listGetAll(String key) {
        return getResource().lrange(key, 0, -1);
    }

    @Override
    public List<String> listRange(String key, long beginIndex, long endIndex) {
        return getResource().lrange(key, beginIndex, endIndex);
    }

    @Override
    public String listPop(String key) {
        return getResource().lpop(key);
    }

    @Override
    public long listLen(String key) {
        return getResource().llen(key);
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
        JedisCluster jedis = getResource();
        return jedis.hmset(key, hash);
    }

    @Override
    public List<byte[]> hashBinaryGet(byte[] key, byte[]... fields) {
        JedisCluster jedis = getResource();

        List<byte[]> list = jedis.hmget(key, fields);
        return list;
    }

    @Override
    public void zadd(String key, double score, String member) {
        JedisCluster jedis = getResource();
        jedis.zadd(key, score, member);
    }

    @Override
    public void zadd(String key, Map<String, Double> scoreMembers) {
        JedisCluster jedis = getResource();
        jedis.zadd(key, scoreMembers);
    }

    @Override
    public long zcard(String key) {
        JedisCluster jedis = getResource();
        return jedis.zcard(key);
    }

    @Override
    public long zcount(String key, double min, double max) {
        JedisCluster jedis = getResource();
        return jedis.zcount(key, min, max);
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        JedisCluster jedis = getResource();
        return jedis.zrange(key, start, end);
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        JedisCluster jedis = getResource();
        return jedis.zrangeWithScores(key, start, end);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        JedisCluster jedis = getResource();
        return jedis.zrangeByScore(key, min, max);
    }

    @Override
    public long zrank(String key, String member) {
        JedisCluster jedis = getResource();
        Long zrank = jedis.zrank(key, member);
        if(zrank==null){
            return -1;
        }else{
            return zrank.longValue();
        }
    }

    @Override
    public long zrem(String key, String member) {
        JedisCluster jedis = getResource();
        return jedis.zrem(key, member);
    }

    @Override
    public double zscore(String key, String member) {
        JedisCluster jedis = getResource();
        Double zscore = jedis.zscore(key, member);
        if(zscore==null){
            return -1;
        }else{
            return zscore.doubleValue();
        }
    }

    @Override
    public double zincrby(String key, double score, String member) {
        JedisCluster jedis = getResource();
        return jedis.zincrby(key, score, member);
    }

    @Override
    public long zremrangeByRank(String key, long start, long end) {
        JedisCluster jedis = getResource();
        return jedis.zremrangeByRank(key, start, end);
    }

    @Override
    public long zremrangeByScore(String key, double start, double end) {
        JedisCluster jedis = getResource();
        return jedis.zremrangeByScore(key, start, end);
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        JedisCluster jedis = getResource();
        return jedis.zrevrange(key, start, end);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        JedisCluster jedis = getResource();
        return jedis.zrevrangeByScore(key, max, min);
    }

    @Override
    public long zrevrank(String key, String member) {
        JedisCluster jedis = getResource();
        Long zrevrank = jedis.zrevrank(key, member);

        if(zrevrank==null){
            return -1;
        }else{
            return zrevrank.longValue();
        }
    }

    @Override
    public void sadd(String key, String... members) {
        JedisCluster jedis = getResource();
        jedis.sadd(key, members);
    }

    @Override
    public long scard(String key, String... members) {
        JedisCluster jedis = getResource();
        return jedis.scard(key);
    }

    @Override
    public Set<String> sdiff(String... keys) {
        JedisCluster jedis = getResource();
        return jedis.sdiff(keys);
    }

    @Override
    public long sdiffstore(String dstkey, String... keys) {
        JedisCluster jedis = getResource();
        return jedis.sdiffstore(dstkey, keys);
    }

    @Override
    public Set<String> sinter(String... keys) {
        JedisCluster jedis = getResource();
        return jedis.sinter(keys);
    }

    @Override
    public long sinterstore(String dstkey, String... keys) {
        JedisCluster jedis = getResource();
        return jedis.sinterstore(dstkey, keys);
    }

    @Override
    public boolean sismember(String key, String member) {
        JedisCluster jedis = getResource();
        return jedis.sismember(key, member);
    }

    @Override
    public Set<String> smembers(String key) {
        JedisCluster jedis = getResource();
        return jedis.smembers(key);
    }

    @Override
    public long srem(String key, String... members) {
        JedisCluster jedis = getResource();
        return jedis.srem(key, members);
    }

    @Override
    public Set<String> sunion(String... keys) {
        JedisCluster jedis = getResource();
        return jedis.sunion(keys);
    }

    @Override
    public long sunionstore(String dstkey, String... keys) {
        JedisCluster jedis = getResource();
        return jedis.sunionstore(dstkey, keys);
    }

    @Override
    public String spop(String key) {
        JedisCluster jedis = getResource();
        return jedis.spop(key);
    }

    @Override
    public String srandmember(String key) {
        JedisCluster jedis = getResource();
        return jedis.srandmember(key);
    }

    /**
     * 集群模式不支持获取当前数据库的key数量
     */
    @Override
    public long dbSize() {
        return -1;
    }

    @Override
    public String setByte(String key, byte[] value) {
        JedisCluster jedis = this.getResource();
        return jedis.set(key.getBytes(), value);
    }

    @Override
    public String setByteEx(String key, int seconds, byte[] value) {
        JedisCluster jedis = this.getResource();
        return jedis.setex(key.getBytes(), seconds, value);
    }

    @Override
    public byte[] getByte(String key) {
        JedisCluster jedis = this.getResource();
        return jedis.get(key.getBytes());
    }

    @Override
    public long delByte(String key) {
        JedisCluster jedis = this.getResource();
        return jedis.del(key.getBytes());
    }

    @Override
    public Map<byte[], byte[]> hashByteGetAll(String key) {
        return getResource().hgetAll(key.getBytes());
    }

    @Override
    public Long hashByteDel(byte[] key, byte[]... fields) {
        return getResource().hdel(key, fields);
    }

    @Override
    public Long delByteKey(byte[] key) {
        return getResource().del(key);
    }

    @Override
    public Long zaddHashByte(byte[] key, double score, byte[] member) {
        return getResource().zadd(key, score, member);
    }

    @Override
    public Long zremrangeByScoreHashByte(byte[] key, double start, double end) {
        return getResource().zremrangeByScore(key, start, end);
    }

    @Override
    public Long zremHashByte(byte[] key, byte[]... member) {
        return getResource().zrem(key, member);
    }

    @Override
    public Set<byte[]> zrangeByScoreHashByte(byte[] key, double min, double max) {
        return getResource().zrangeByScore(key, min, max);
    }

    @Override
    public String info() {
        throw new RuntimeException("cluster not support info command!");
    }

    @Override
    public String info(String section) {
        throw new RuntimeException("cluster not support info command!");
    }

    @Override
    public String getSet(String key, String value) {
        JedisCluster jedis = this.getResource();
        return jedis.getSet(key, value);
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        JedisCluster jedis = getResource();
        return jedis.zrangeByScore(key, min, max,offset,count);
    }

    @Override
    public String lindex(String key, long index) {
        return getResource().lindex(key, index);
    }

    @Override
    public String rpop(String key) {
        return getResource().rpop(key);
    }

    @Override
    public Long hincrBy(String key, String field, int value) {
        Long aLong = getResource().hincrBy(key, field, value);
        return aLong;
    }
}
