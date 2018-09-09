package com.zwt.framework.rocketmq.consumer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/9/5
 * @since 1.0
 */
public class RocketMQMessageListenerConcurrently extends RocketMQMessageListenerAbstract implements MessageListenerConcurrently {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(RocketMQMessageListenerConcurrently.class);



    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        LOGGER.debug("RocketMQMessageListenerConcurrently receive message begin,length:{}" , msgs.size());
        for(MessageExt msg:msgs ) {
            for (IProcessor processor : processorList) {
                try {
                    // 处理消息
                    process(processor, msg);
                } catch (Exception ex) {
                    try {
                        String errorMsg = String.format("processor 处理消息时异常：[%s],消息体：[%s]", ex.getMessage(),new String(msg.getBody(),"utf-8"));
                        LOGGER.error(errorMsg,ex);
                    } catch (UnsupportedEncodingException e) {
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }



}
