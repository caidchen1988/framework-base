package com.zwt.framework.rocketmq.factory;

import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.zwt.framework.rocketmq.consumer.RocketMQMessageConsumer;
import com.zwt.framework.rocketmq.consumer.RocketMQMessageListenerConcurrently;
import com.zwt.framework.rocketmq.consumer.RocketMQMessageListenerOrderly;
import com.zwt.framework.rocketmq.producer.RocketMQMessageProducer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zwt
 * @detail 创建生产者与消费者工厂
 * @date 2018/9/5
 * @since 1.0
 */
public class RocketMQFactory {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(RocketMQFactory.class);
    private static Map<String, RocketMQMessageProducer> producers=new ConcurrentHashMap<>();
    private static Map<String, RocketMQMessageConsumer> consumers=new ConcurrentHashMap<>();

    private static class SingletonHolder {
        static final RocketMQFactory instance = new RocketMQFactory();
    }

    public static RocketMQFactory getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 创建一个生产者
     * @param producerId
     * @param groupName
     * @param namesrvAddr
     * @param options
     * @return
     */
    public RocketMQMessageProducer createProducer(String producerId,String groupName,String namesrvAddr,final Map<String,String> options){
        if(producers.get(producerId)!=null){
            return producers.get(producerId);
        }

        RocketMQMessageProducer producer=new RocketMQMessageProducer(groupName, namesrvAddr);
        //设置生产者其它参数
        String sendMsgTimeout=options.get("sendMsgTimeout");//发送消息超时时间，单位毫秒
        String maxMessageSize = options.get("maxMessageSize");//客户端限制的消息大小，超过报错，同时服务端也会限制，默认131072（128kb）

        if(StringUtils.isNotBlank(sendMsgTimeout)){
            producer.setSendMsgTimeout(Integer.parseInt(sendMsgTimeout));
        }
        if(StringUtils.isNotBlank(maxMessageSize)){
            producer.setMaxMessageSize(Integer.parseInt(maxMessageSize));
        }

        try {
            producer.start();
            producers.put(producerId, producer);
        } catch (MQClientException e) {
            String errorMsg=String.format("producerId:[%s],groupName:[%s],namesrvAddr:[%s]生产者创建时异常：%s",producerId, groupName,namesrvAddr,e.getMessage());
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        return producer;
    }

    /**
     * 创建一个消费者
     * @param consumerId
     * @param groupName
     * @param namesrvAddr
     * @param topicAndTagMap
     * @param messageListener
     * @param options
     * @return
     */
    public RocketMQMessageConsumer createConsumer(String consumerId,
                                                  String groupName,
                                                  String namesrvAddr,
                                                  Map<String /*topic*/,String /*tag*/> topicAndTagMap,
                                                  MessageListener messageListener,
                                                  final Map<String,String> options){
        if(consumers.get(consumerId)!=null){
            return consumers.get(consumerId);
        }
        try {
            RocketMQMessageConsumer consumer=new RocketMQMessageConsumer(consumerId,groupName, namesrvAddr);
            consumer.subscribe(topicAndTagMap);
            //注册处理监听器
            if(messageListener instanceof RocketMQMessageListenerOrderly){
                //顺序消息
                RocketMQMessageListenerOrderly listenerOrderly = (RocketMQMessageListenerOrderly) messageListener;
                consumer.registerMessageListener(listenerOrderly);
            }else if(messageListener instanceof RocketMQMessageListenerConcurrently){
                //并发消息
                RocketMQMessageListenerConcurrently listenerConcurrently = (RocketMQMessageListenerConcurrently) messageListener;
                consumer.registerMessageListener(listenerConcurrently);
            }

            //设置消费者其它参数

            String consumeFromWhere=options.get("consumeFromWhere");
            String consumeThreadMin=options.get("consumeThreadMin");
            String consumeThreadMax=options.get("consumeThreadMax");
            String pullThresholdForQueue=options.get("pullThresholdForQueue");
            String consumeMessageBatchMaxSize=options.get("consumeMessageBatchMaxSize");
            String pullBatchSize=options.get("pullBatchSize");
            String pullInterval=options.get("pullInterval");
            if(StringUtils.isNotBlank(consumeFromWhere)){
                if(StringUtils.equals(consumeFromWhere, "CONSUME_FROM_LAST_OFFSET")){
                    consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
                }else if(StringUtils.equals(consumeFromWhere, "CONSUME_FROM_FIRST_OFFSET")){
                    consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
                }
            }
            if(StringUtils.isNotBlank(consumeThreadMin)){
                consumer.setConsumeThreadMin(Integer.parseInt(consumeThreadMin));
            }
            if(StringUtils.isNotBlank(consumeThreadMax)){
                consumer.setConsumeThreadMax(Integer.parseInt(consumeThreadMax));
            }
            if(StringUtils.isNotBlank(pullThresholdForQueue)){
                consumer.setPullThresholdForQueue(Integer.parseInt(pullThresholdForQueue));
            }
            if(StringUtils.isNotBlank(consumeMessageBatchMaxSize)){
                consumer.setConsumeMessageBatchMaxSize(Integer.parseInt(consumeMessageBatchMaxSize));
            }
            if(StringUtils.isNotBlank(pullBatchSize)){
                consumer.setPullBatchSize(Integer.parseInt(pullBatchSize));
            }
            if(StringUtils.isNotBlank(pullInterval)){
                consumer.setPullInterval(Integer.parseInt(pullInterval));
            }
            consumer.start();
            consumers.put(consumerId, consumer);
            return consumer;
        } catch (Exception e) {
            String errorMsg=String.format("consumerId:[%s],groupName:[%s],namesrvAddr:[%s]消费者创建时异常：%s", consumerId,groupName,namesrvAddr,e.getMessage());
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    public void stopProducer(String producerId){
        if(producers.get(producerId)!=null){
            producers.get(producerId).shutdown();
            producers.remove(producerId);
            LOGGER.warn(producerId+" is shutdown!");
        }
    }
    public void stopProducers(){
		/*
		for(String producerId:producers.keySet()){
			producers.get(producerId).shutdown();
			LOGGER.warn(producerId+" is shutdown!");
		}
		*/
        for(Map.Entry<String, RocketMQMessageProducer> producerEntry:producers.entrySet()){
            producerEntry.getValue().shutdown();
            LOGGER.warn(producerEntry.getKey()+" is shutdown!");
        }

        producers.clear();
    }
    public void stopConsumer(String consumerId){
        if(consumers.get(consumerId)!=null){
            consumers.get(consumerId).shutdown();
            LOGGER.warn(consumerId+" is shutdown!");
            consumers.remove(consumerId);
        }
    }
    public void stopConsumers(){
		/*
		for(String consumerId:consumers.keySet()){
			consumers.get(consumerId).shutdown();
			LOGGER.warn(consumerId+" is shutdown!");
		}
		*/

        for(Map.Entry<String, RocketMQMessageConsumer> consumerEntry:consumers.entrySet()){
            consumerEntry.getValue().shutdown();
            LOGGER.warn(consumerEntry.getKey()+" is shutdown!");
        }
        consumers.clear();
    }

}
