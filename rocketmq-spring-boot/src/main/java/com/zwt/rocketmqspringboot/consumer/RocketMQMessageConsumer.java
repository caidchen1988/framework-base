package com.zwt.rocketmqspringboot.consumer;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zwt
 * @detail 消费者
 * @date 2018/9/5
 * @since 1.0
 */
public class RocketMQMessageConsumer extends DefaultMQPushConsumer {
    private static final transient Logger logger = LoggerFactory.getLogger(RocketMQMessageConsumer.class);
    /** 消费者ID */
    private String consumerId;

    public RocketMQMessageConsumer(String consumerId, String groupName, String namesrvAddr) {
        super(groupName);
        //this.setInstanceName(instanceName);
        this.setNamesrvAddr(namesrvAddr);
        this.setVipChannelEnabled(false);
        this.consumerId = consumerId;
    }

    /**
     * 订阅指定主题
     * @param topicAndTagMap
     */
    public void subscribe(Map<String /*topic*/, String /*tag*/> topicAndTagMap) {
        for(Map.Entry<String, String> topicAndTagEntry: topicAndTagMap.entrySet()){
            String topic=topicAndTagEntry.getKey();
            String tag=topicAndTagEntry.getValue();
            try {
                this.subscribe(topic, tag);
            } catch (MQClientException ex) {
                logger.error("MqConsumer subscribe error", ex);
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 获取 消费者ID
     * @return consumerId
     */
    public String getConsumerId() {
        return consumerId;
    }

    /**
     * 设置 消费者ID
     * @param consumerId
     */
    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    /**
     * 重写hashCode
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consumerId == null) ? 0 : consumerId.hashCode());
        return result;
    }

    /**
     * 重写equals方法，认为consumerId相等则两个消费者相等
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RocketMQMessageConsumer other = (RocketMQMessageConsumer) obj;
        if (consumerId == null) {
            if (other.consumerId != null)
                return false;
        } else if (!consumerId.equals(other.consumerId))
            return false;
        return true;
    }

}
