package com.zwt.framework.rocketmq.consumer;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zwt
 * @detail
 * @date 2018/9/5
 * @since 1.0
 */
public class RocketMQMessageConsumer extends DefaultMQPushConsumer {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(RocketMQMessageConsumer.class);
    /** 消费者ID */
    private String consumerId;

    public RocketMQMessageConsumer(String consumerId, String groupName, String namesrvAddr) {
        super(groupName);
        //this.setInstanceName(instanceName);
        this.setNamesrvAddr(namesrvAddr);
        this.setVipChannelEnabled(false);
        this.consumerId = consumerId;
    }

    public void subscribe(Map<String /*topic*/, String /*tag*/> topicAndTagMap) {
        for(Map.Entry<String, String> topicAndTagEntry: topicAndTagMap.entrySet()){
            String topic=topicAndTagEntry.getKey();
            String tag=topicAndTagEntry.getValue();
            try {
                this.subscribe(topic, tag);
            } catch (MQClientException ex) {
                String errorMsg = String.format("订阅主题时异常,consumerId:[%s],topic:[%s],tag:[%s]", consumerId, topic, tag);
                LOGGER.error(errorMsg, ex);
                throw new RuntimeException(errorMsg);
            }

        }
    }


    /**
     * 获取 消费者ID
     *
     * @return consumerId
     */
    public String getConsumerId() {
        return consumerId;
    }

    /**
     * 设置 消费者ID
     *
     * @param consumerId
     */
    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consumerId == null) ? 0 : consumerId.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
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
