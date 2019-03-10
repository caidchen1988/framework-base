package com.zwt.zkregistryspringbootstarter.autoconfigure;

import com.zwt.framework.zk.config.ZKConfig;
import com.zwt.zkregistryspringbootstarter.config.ZKRegistryConfiguration;
import com.zwt.zkregistryspringbootstarter.registry.ZookeeperRegistryClient;
import com.zwt.zkregistryspringbootstarter.util.ZKRegistryUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

/**
 * @author zwt
 * @detail springboot 自动配置类
 * @date 2018/12/21
 * @since 1.0
 */
@Configuration
@ConditionalOnClass(ZookeeperRegistryClient.class)
@EnableConfigurationProperties(ZKRegistryConfiguration.class)
public class ZKRegistryAutoConfig implements ApplicationContextAware {

    @Autowired
    private ZKRegistryConfiguration properties;
    @Value("${server.address}")
    private String serverAddress;
    @Value("${server.port}")
    private int serverPort;

    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.zookeeper.registry", name = "enabled", havingValue = "true")
    ZookeeperRegistryClient zookeeperRegistryClient (){
        ZKConfig config = new ZKConfig();
        config.setConnectString(properties.getZkAddress());
        config.setNameSpace(properties.getSysName());
        config.setSessionTimeoutMs(properties.getSessionTimeoutMs());
        config.setConnectTimeoutMs(properties.getConnectTimeoutMs());
        config.setRetryInterval(properties.getRetryInterval());
        ZookeeperRegistryClient zookeeperRegistryClient = new ZookeeperRegistryClient(config);
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> infoMap = mapping.getHandlerMethods();
        infoMap.forEach((key,value)->{
            String serviceName = key.getName();
            String methodName = value.getMethod().getName();
            if(StringUtils.isBlank(serviceName)){
                serviceName = methodName.toUpperCase() + ZKRegistryUtil.HTTP_SERVICE_SUFFIX;
            }else{
                serviceName = serviceName.toUpperCase() + ZKRegistryUtil.HTTP_SERVICE_SUFFIX;
            }
            zookeeperRegistryClient.register(serviceName,String.format("%s:%s/%s",serverAddress,serverPort,methodName));
        });
        return zookeeperRegistryClient;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
