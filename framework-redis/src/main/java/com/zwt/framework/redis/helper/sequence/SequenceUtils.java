package com.zwt.framework.redis.helper.sequence;

import com.zwt.framework.redis.util.RedisUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @author zwt
 * @detail 使用redis实现唯一自增序列号
 * @date 2019/4/9
 * @since 1.0
 */
public class SequenceUtils {

    private RedisUtil redisUtil;

    public SequenceUtils(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    //业务规定序号为00001 ，00002  这种5位格式
    private static final int DEFAULT_LENGTH = 5;
    //缓存时长
    private static final int ONE_DAY_TIME = 24*60*60;

    private static final String REDIS_CACHE_KEY = "redis.serialnumber:%s:%s";

    /**
     * 获取自增数字的字符串形式,包含0前缀
     * @param seq
     * @return
     */
    private String getSequenceWithZeroPrefix(long seq) {
        String str = String.valueOf(seq);
        int len = str.length();
        if (len >= DEFAULT_LENGTH) {
            throw new RuntimeException("Sequence generate failed!");
        }
        int rest = DEFAULT_LENGTH - len;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rest; i++) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 获取自增数字的字符串形式,不包含0前缀
     * @param seq
     * @return
     */
    private String getSequenceNoZeroPrefix(long seq){
        return String.valueOf(seq);
    }


    /**
     * 序列号生成器
     * @param bizCode  业务码
     * @param needZero 是否需要0前缀
     * @return
     */
    public String generate(String bizCode,boolean needZero){
        String date = DateFormatUtils.format(new Date(),"yyyyMMdd");
        //redis key
        String key = String.format(REDIS_CACHE_KEY,bizCode,date);
        //自增并设置过期时间
        long sequence = redisUtil.incr(key);
        redisUtil.expire(key,ONE_DAY_TIME);

        String seq;
        if(needZero){
            seq = getSequenceWithZeroPrefix(sequence);
        }else{
            seq = getSequenceNoZeroPrefix(sequence);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(bizCode).append(date).append(seq);

        return sb.toString();
    }
}
