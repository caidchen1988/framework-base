package com.zwt.framework.redis.helper.limit;

import com.zwt.framework.redis.util.RedisClusterUtil;
import com.zwt.framework.redis.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwt
 * @detail 高频请求访问限制
 * @date 2019/4/9
 * @since 1.0
 */
public class HighFreqLimit {

    //记录用户行为并判断高频访问的zset
    private static final String REDIS_VISIT_KEY_FORMAT = "redis.visit:user:zset:%s";
    //高频访问用户key
    private static final String REDIS_LIMIT_KEY_FORMAT = "redis.limit:user:%s";

    private static final String REDIS_VISIT_KEY_FORMAT2 = "redis.visit:user:lua:%s";
    //Redis 工具
    private RedisUtil redisUtil;

    public HighFreqLimit(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    /**
     * 方案一：使用Zset来判断高频访问情况
     */
    /**
     * 判断用户是否高频访问
     * 高频访问抛出异常
     * @param userNo 用户唯一的编号
     * @param limitRule 访问规则   这个规则可以用配置文件的方式处理
     * @return
     */
    public void checkLimit1(String userNo,LimitRule limitRule){

        //判断是否启用了高频访问
        if(!limitRule.enableLimitLock()){
            return;
        }

        String redisKeyUserVisitZset = String.format(REDIS_VISIT_KEY_FORMAT,userNo);
        String redisUserFreqLimitKey = String.format(REDIS_LIMIT_KEY_FORMAT,userNo);
        //如果存在高频访问key说明已经高频访问了
        if (redisUtil.exists(redisUserFreqLimitKey)) {
            throw new RuntimeException("您操作的太快了，请稍后访问");
        }

        long currentTimeMillis=System.currentTimeMillis();
        //访问信息，可以根据具体业务定制
        String visitInfo = userNo +":"+ System.currentTimeMillis();
        //将信息添加到zset里
        redisUtil.zadd(redisKeyUserVisitZset,System.currentTimeMillis(), visitInfo);
        //设置过期时间为单位时间
        redisUtil.expire(redisKeyUserVisitZset, limitRule.getSeconds());
        long startTimeMillis = currentTimeMillis - limitRule.getSeconds() * 1000;
        long visitCount = redisUtil.zcount(redisKeyUserVisitZset, startTimeMillis, currentTimeMillis);
        //超过阈值则成为高频用户
        if (visitCount > limitRule.getLimitCount()) {
            redisUtil.setString(redisUserFreqLimitKey, visitInfo);
            redisUtil.expire(redisUserFreqLimitKey, limitRule.getLockTime());
            throw new RuntimeException("您操作的太快了，请稍后访问");
        }
    }


    /**
     * 方案二：使用Lua表达式来判断高频访问情况
     */
    /**
     * 判断用户是否高频访问
     * 高频访问抛出异常
     * @param userNo
     * @param limitRule
     */
    public  void checkLimit2(String userNo, LimitRule limitRule) {
        String redisKeyUserVisit = String.format(REDIS_VISIT_KEY_FORMAT2,userNo);
        long count;
        List<String> keys = new ArrayList<String>();
        keys.add(redisKeyUserVisit);
        List<String> args = new ArrayList<String>();
        args.add(limitRule.getLimitCount() + "");
        args.add(limitRule.getSeconds() + "");
        args.add(limitRule.getLockTime() + "");
        count = Long.parseLong(redisUtil.getJedisFactory().getJedisCluster().eval(buildLuaScript(limitRule), keys, args) + "");
        if(count > limitRule.getLimitCount()){
            throw new RuntimeException("您操作的太快了，请稍后访问");
        }
    }
    /**
     * 构造lua表达式
     * @param limitRule
     * @return
     */
    private String buildLuaScript(LimitRule limitRule) {
        StringBuilder lua = new StringBuilder();
        lua.append("\nlocal c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        if (limitRule.enableLimitLock()) {
            lua.append("\nif tonumber(c) > tonumber(ARGV[1]) then");
            lua.append("\nredis.call('expire',KEYS[1],ARGV[3])");
            lua.append("\nend");
        }
        lua.append("\nreturn c;");
        return lua.toString();
    }

}
