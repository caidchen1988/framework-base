package com.zwt.elasticsearchspringbootstarter.factory;

import com.zwt.elasticsearchspringbootstarter.util.ElasticSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zwt
 * @detail  springboot 自动注入配置类
 * @date 2018/11/29
 * @since 1.0
 */
@Configuration
@ConditionalOnClass(ElasticSearchClientFactory.class)
@EnableConfigurationProperties(ElasticSearchConfiguration.class)
public class ElasticSearchStarterAutoConfigure {
    @Autowired
    private ElasticSearchConfiguration properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.elasticserch.zwt", name = "enabled", havingValue = "true")
    ElasticSearchUtil elasticSearchUtil (){
        ElasticSearchClientFactory elasticSearchClientFactory=new ElasticSearchClientFactory(properties);
        return new ElasticSearchUtil(elasticSearchClientFactory);
    }
}
