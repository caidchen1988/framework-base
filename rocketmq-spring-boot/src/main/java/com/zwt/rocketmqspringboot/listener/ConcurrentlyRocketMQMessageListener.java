package com.zwt.rocketmqspringboot.listener;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/9/5
 * @since 1.0
 */
public class ConcurrentlyRocketMQMessageListener extends AbstractRocketMQMessageListener implements MessageListenerConcurrently {
    private static final transient Logger logger = LoggerFactory.getLogger(ConcurrentlyRocketMQMessageListener.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        logger.debug("ConcurrentlyRocketMQMessageListener receive message begin,length:{}" , msgs.size());
        for(MessageExt msg:msgs ) {
            for (IProcessor processor : processorList) {
                try {
                    // 处理消息
                    process(processor, msg);
                } catch (Exception ex) {
                    logger.error("ConcurrentlyRocketMQMessageListener error",ex);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
