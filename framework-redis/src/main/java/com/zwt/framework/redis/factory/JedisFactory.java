package com.zwt.framework.redis.factory;

import com.zwt.framework.redis.exception.RedisException;
import com.zwt.framework.redis.exception.RedisExceptionHandler;
import com.zwt.framework.utils.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author zwt
 * @detail 获取一个JedisPool
 * @date 2018/9/3
 * @since 1.0
 */
public class JedisFactory {
    private final static Logger logger = LoggerFactory.getLogger(JedisFactory.class);
    private volatile JedisPool jedisPool;
    private volatile JedisCluster jedisCluster;
    private RedisConfiguration redisConfig;
    private Pattern addRessPattern = Pattern.compile("^.+[:]\\d{1,5}\\s*(;.+[:]\\d{1,5}\\s*)*[;]?\\s*$");

    public JedisFactory(final RedisConfiguration redisConfiguration){
        this.redisConfig=redisConfiguration;
    }

    public JedisPool getJedisPool(){
        if(jedisPool==null){
            synchronized (JedisFactory.class){
                if(jedisPool==null){
                    init();
                }
            }
        }
        return jedisPool;
    }

    public JedisCluster getJedisCluster(){
        if(jedisCluster==null){
            synchronized (JedisFactory.class){
                if(jedisCluster==null){
                    init();
                }
            }
        }
        return jedisCluster;
    }

    public void init(){
        logger.info("JedisFactory init start...");
        try{
            if(StringUtils.isNotBlank(redisConfig.getLocalPropertiesPath())){
                fillData();
            }
            logger.info("redis config is: {}.", redisConfig.toString());
            Set<HostAndPort> hostAndPortSet = this.parseHostAndPort(redisConfig.getAddress());

            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
            genericObjectPoolConfig.setMaxTotal(redisConfig.getMaxTotal());
            genericObjectPoolConfig.setMinIdle(redisConfig.getMinIdle());
            genericObjectPoolConfig.setMaxIdle(redisConfig.getMaxIdle());

            if(redisConfig.getMode()== RedisConstants.REDIS_MODE_SINGLE){
                HostAndPort hostAndPort=(HostAndPort)hostAndPortSet.toArray()[0];
                jedisPool=new JedisPool(genericObjectPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(), redisConfig.getTimeout(), null,redisConfig.getDatabase());
                logger.info("jedisPool init is finished");
            }else{
                if(redisConfig.getDatabase()!=0){
                    logger.warn("当前配置的database为："+redisConfig.getDatabase()+",集群模式下不能选择database，只能使用database0");
                }
                jedisCluster = new JedisCluster(hostAndPortSet, redisConfig.getTimeout(), redisConfig.getMaxRedirections(), genericObjectPoolConfig);
                logger.info("jedisCluster init is finished");
            }

        }catch(Exception ex){
            RedisExceptionHandler.handleException(ex);
        }


    }

    private void fillData() throws Exception {

        Properties localProperties = PropertiesUtils.loadLocalProperties(redisConfig.getLocalPropertiesPath());

        String address=localProperties.getProperty("address", "");
        if (StringUtils.isBlank(address)) {
            throw new RedisException("error:redis config address is blank!");
        }

        // 设置初始值
        long maxWaitMillis=Long.parseLong(localProperties.getProperty("maxWaitMillis", String.valueOf(GenericObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS)));
        int maxTotal=Integer.parseInt(localProperties.getProperty("maxTotal", String.valueOf(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL)));
        int minIdle=Integer.parseInt(localProperties.getProperty("minIdle", String.valueOf(GenericObjectPoolConfig.DEFAULT_MIN_IDLE)));
        int maxIdle=Integer.parseInt(localProperties.getProperty("maxIdle", String.valueOf(GenericObjectPoolConfig.DEFAULT_MAX_IDLE)));
        int timeout=Integer.parseInt((localProperties.getProperty("timeout", "2000")));
        int maxRedirections=Integer.parseInt((localProperties.getProperty("maxRedirections", "6")));
        int database=Integer.parseInt((localProperties.getProperty("database", "0")));
        //1单机模式，2集群模式
        int mode=Integer.parseInt((localProperties.getProperty("mode", String.valueOf(RedisConstants.REDIS_MODE_SINGLE))));

        redisConfig.setAddress(address);
        redisConfig.setMaxWaitMillis(maxWaitMillis);
        redisConfig.setMaxTotal(maxTotal);
        redisConfig.setMinIdle(minIdle);
        redisConfig.setMaxIdle(maxIdle);
        redisConfig.setTimeout(timeout);
        redisConfig.setMaxRedirections(maxRedirections);
        redisConfig.setDatabase(database);
        redisConfig.setMode(mode);

    }

    private Set<HostAndPort> parseHostAndPort(String addressContent) throws Exception {
        try {
            if (StringUtils.isBlank(addressContent)) {
                throw new IllegalArgumentException("redis 连接地址配置不合法");
            }
            boolean result = addRessPattern.matcher(addressContent).matches();
            if (!result) {
                throw new IllegalArgumentException("redis 连接地址配置不合法");
            }
            Set<HostAndPort> hostAndPortSet = new HashSet<HostAndPort>();
            String[] addressArrays = addressContent.split(";");
            for (String address : addressArrays) {
                String[] ipAndPort = address.trim().split(":");
                String ip = ipAndPort[0].trim();
                String port = ipAndPort[1].trim();
                HostAndPort hap = new HostAndPort(ip, Integer.parseInt(port));
                hostAndPortSet.add(hap);
            }
            return hostAndPortSet;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Exception("解析 jedis 配置文件失败", ex);
        }
    }
}
