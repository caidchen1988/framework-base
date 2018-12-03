package com.zwt.rocketmqspringboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/11/30
 * @since 1.0
 */
@ConfigurationProperties("spring.rocketmq.config")
public class MqConfigurations {

    private List<MqProducerConfiguration> mqProducerConfigurations;

    private List<MqConsumerConfiguration> mqConsumerConfigurations;

    public List<MqProducerConfiguration> getMqProducerConfigurations() {
        return mqProducerConfigurations;
    }

    public void setMqProducerConfigurations(List<MqProducerConfiguration> mqProducerConfigurations) {
        this.mqProducerConfigurations = mqProducerConfigurations;
    }

    public List<MqConsumerConfiguration> getMqConsumerConfigurations() {
        return mqConsumerConfigurations;
    }

    public void setMqConsumerConfigurations(List<MqConsumerConfiguration> mqConsumerConfigurations) {
        this.mqConsumerConfigurations = mqConsumerConfigurations;
    }
}
