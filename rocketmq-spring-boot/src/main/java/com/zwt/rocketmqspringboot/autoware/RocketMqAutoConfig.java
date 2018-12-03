package com.zwt.rocketmqspringboot.autoware;

import com.zwt.rocketmqspringboot.annotation.RocketMQProcessor;
import com.zwt.rocketmqspringboot.config.MqConfigurations;
import com.zwt.rocketmqspringboot.factory.RocketMQFactory;
import com.zwt.rocketmqspringboot.listener.IProcessor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zwt
 * @detail 自动装配MqProducer
 * @date 2018/11/30
 * @since 1.0
 */
@Configuration
@ConditionalOnClass(RocketMQFactory.class)
@EnableConfigurationProperties(MqConfigurations.class)
public class RocketMqAutoConfig implements ApplicationContextAware {

    @Autowired
    private MqConfigurations mqConfigurations;

    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.rocketmq.config", name = "enabled", havingValue = "true")
    RocketMQFactory rocketMQFactory(){
        RocketMQFactory rocketMQFactory=new RocketMQFactory();
        //处理生产者
        if(mqConfigurations.getMqProducerConfigurations()!=null&&mqConfigurations.getMqProducerConfigurations().size()>0){
            mqConfigurations.getMqProducerConfigurations().forEach(producerConfiguration ->{
                rocketMQFactory.createProducer(producerConfiguration);
            });
        }
        if(mqConfigurations.getMqConsumerConfigurations()!=null&&mqConfigurations.getMqConsumerConfigurations().size()>0){
            //处理消费者
            mqConfigurations.getMqConsumerConfigurations().forEach(consumerConfiguration->{
                //处理Processor
                final Map<String, Object> annotationMap = applicationContext.getBeansWithAnnotation(RocketMQProcessor.class);
                List<IProcessor> list = new ArrayList<>();
                if(annotationMap!=null){
                    annotationMap.forEach((key,value)->{
                        RocketMQProcessor annotation = value.getClass().getAnnotation(RocketMQProcessor.class);
                        if(consumerConfiguration.getConsumerId().equals(annotation.consumerId())){
                            try{
                                list.add((IProcessor) value);
                            }catch (Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                rocketMQFactory.createConsumer(consumerConfiguration,list);
            });



        }
        return rocketMQFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
