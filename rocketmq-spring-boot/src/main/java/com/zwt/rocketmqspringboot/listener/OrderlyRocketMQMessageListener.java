package com.zwt.rocketmqspringboot.listener;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zwt
 * @detail 顺序消息（broker只能有一个队列）
 * @date 2018/9/5
 * @since 1.0
 */
public class OrderlyRocketMQMessageListener extends AbstractRocketMQMessageListener implements MessageListenerOrderly {
    private static final transient Logger logger = LoggerFactory.getLogger(OrderlyRocketMQMessageListener.class);

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        logger.debug("OrderlyRocketMQMessageListener receive message begin,length:{}" , msgs.size());
        for(MessageExt msg:msgs ) {
            for (IProcessor processor : processorList) {
                try {
                    process(processor, msg);
                } catch (Exception ex) {
                    logger.error("OrderlyRocketMQMessageListener error",ex);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }



}
