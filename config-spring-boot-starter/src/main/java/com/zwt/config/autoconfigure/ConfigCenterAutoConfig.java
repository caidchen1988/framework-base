package com.zwt.config.autoconfigure;

import com.zwt.config.config.ConfigCenterConfiguration;
import com.zwt.framework.zk.client.impl.CuratorZKClient;
import com.zwt.framework.zk.config.ZKConfig;
import com.zwt.framework.zk.constants.ZKConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zwt
 * @detail springboot 自动配置类
 * @date 2018/12/21
 * @since 1.0
 */
@Configuration
@ConditionalOnClass(CuratorZKClient.class)
@EnableConfigurationProperties(ConfigCenterConfiguration.class)
public class ConfigCenterAutoConfig implements ApplicationContextAware {
    private static final Logger log= LoggerFactory.getLogger(ConfigCenterAutoConfig.class);

    @Autowired
    private ConfigCenterConfiguration properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.zookeeper.config-center", name = "enabled", havingValue = "true")
    CuratorZKClient curatorZKClient (){
        ZKConfig config = new ZKConfig();
        config.setConnectString(properties.getZkAddress());
        config.setNameSpace(properties.getSysName());
        config.setSessionTimeoutMs(properties.getSessionTimeoutMs());
        config.setConnectTimeoutMs(properties.getConnectTimeoutMs());
        config.setRetryInterval(properties.getRetryInterval());
        CuratorZKClient zkClient = new CuratorZKClient(config);
        zkClient.addConnectionListener((state) -> {
            log.debug("ZKConfigService connectionListener state：" + state);
            if (state == ZKConstants.State.CONNECTED || state == ZKConstants.State.RECONNECTED) {
                log.info("ZKConfigService zookeeper is connected...");
            }
        });
        zkClient.start();
        return zkClient;
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    //根据class类型返回bean
    public static <T> T getBean(Class<T> requireType){
        return applicationContext.getBean(requireType);
    }
}
