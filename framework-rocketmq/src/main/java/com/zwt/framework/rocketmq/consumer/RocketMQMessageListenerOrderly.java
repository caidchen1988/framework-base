package com.zwt.framework.rocketmq.consumer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author zwt
 * @detail 顺序消息（broker只能有一个队列）
 * @date 2018/9/5
 * @since 1.0
 */
public class RocketMQMessageListenerOrderly extends RocketMQMessageListenerAbstract implements MessageListenerOrderly {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(RocketMQMessageListenerOrderly.class);

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        LOGGER.debug("RocketMQMessageListenerOrderly receive message begin,length:{}" , msgs.size());
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
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
        }

        return ConsumeOrderlyStatus.SUCCESS;
    }



}
