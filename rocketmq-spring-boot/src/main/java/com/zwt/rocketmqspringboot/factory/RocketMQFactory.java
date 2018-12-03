package com.zwt.rocketmqspringboot.factory;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.zwt.rocketmqspringboot.config.MqConsumerConfiguration;
import com.zwt.rocketmqspringboot.config.MqProducerConfiguration;
import com.zwt.rocketmqspringboot.consumer.RocketMQMessageConsumer;
import com.zwt.rocketmqspringboot.listener.ConcurrentlyRocketMQMessageListener;
import com.zwt.rocketmqspringboot.listener.IProcessor;
import com.zwt.rocketmqspringboot.listener.OrderlyRocketMQMessageListener;
import com.zwt.rocketmqspringboot.producer.RocketMQMessageProducer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zwt
 * @detail 创建生产者和消费者工厂
 * @date 2018/9/5
 * @since 1.0
 */
public class RocketMQFactory {
    private static final transient Logger logger = LoggerFactory.getLogger(RocketMQFactory.class);

    //用于存放生产者的map组
    private static Map<String, RocketMQMessageProducer> producers=new ConcurrentHashMap<>();
    //用于存放消费者的map组
    private static Map<String, RocketMQMessageConsumer> consumers=new ConcurrentHashMap<>();

    //将工厂设置成单例模式
    private static class SingletonHolder {
        static final RocketMQFactory instance = new RocketMQFactory();
    }
    public static RocketMQFactory getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 创建一个生产者
     * @param configuration
     * @return
     */
    public RocketMQMessageProducer createProducer(MqProducerConfiguration configuration){
        //如果map里存在这个实例，直接返回
        if(producers.get(configuration.getProducerId())!=null){
            return producers.get(configuration.getProducerId());
        }
        //创建一个生产者
        RocketMQMessageProducer producer=new RocketMQMessageProducer(configuration.getGroupName(), configuration.getNamesrvAddr());
        if(configuration.getSendMsgTimeout()!=null){
            producer.setSendMsgTimeout(configuration.getSendMsgTimeout());
        }
        if(configuration.getMaxMessageSize()!=null){
            producer.setMaxMessageSize(configuration.getMaxMessageSize());
        }
        try {
            //启动生产者并放入map进行管理
            producer.start();
            producers.put(configuration.getProducerId(), producer);
            logger.info("MqProducer start success "+configuration.toString());
        } catch (MQClientException e) {
            logger.error("MqProducer start error "+configuration.toString(),e);
            throw new RuntimeException(e);
        }
        return producer;
    }

    /**
     * 获取一个生产者
     * @param producerId
     * @return
     */
    public RocketMQMessageProducer getProducer(String producerId){
        if(producers.get(producerId)!=null){
            return producers.get(producerId);
        }
        return null;
    }

    /**
     * 停止某个生产者
     * @param producerId
     */
    public void stopProducer(String producerId){
        if(producers.get(producerId)!=null){
            producers.get(producerId).shutdown();
            producers.remove(producerId);
            logger.info("MqProducer "+producerId+" is shutdown!");
        }
    }

    /**
     * 停止全部生产者
     */
    public void stopProducers(){
        for(Map.Entry<String, RocketMQMessageProducer> producerEntry:producers.entrySet()){
            producerEntry.getValue().shutdown();
            logger.info("MqProducer "+producerEntry.getKey()+" is shutdown!");
        }
        producers.clear();
    }


    /**
     * 创建一个消费者
     * @param mqConsumerConfiguration
     * @return
     */
    public RocketMQMessageConsumer createConsumer(MqConsumerConfiguration mqConsumerConfiguration, List<IProcessor> list) {
        //如果map里存在，直接返回
        if (consumers.get(mqConsumerConfiguration.getConsumerId()) != null) {
            return consumers.get(mqConsumerConfiguration.getConsumerId());
        }
        try {
            RocketMQMessageConsumer consumer = new RocketMQMessageConsumer(mqConsumerConfiguration.getConsumerId(), mqConsumerConfiguration.getGroupName(), mqConsumerConfiguration.getNamesrvAddr());
            consumer.subscribe(mqConsumerConfiguration.getTopicAndTagMap());

            //设置消费者其它参数
            if(!CollectionUtils.isEmpty(mqConsumerConfiguration.getOptions())){
                String consumeFromWhere = mqConsumerConfiguration.getOptions().get("consumeFromWhere");
                String consumeThreadMin = mqConsumerConfiguration.getOptions().get("consumeThreadMin");
                String consumeThreadMax = mqConsumerConfiguration.getOptions().get("consumeThreadMax");
                String pullThresholdForQueue = mqConsumerConfiguration.getOptions().get("pullThresholdForQueue");
                String consumeMessageBatchMaxSize = mqConsumerConfiguration.getOptions().get("consumeMessageBatchMaxSize");
                String pullBatchSize = mqConsumerConfiguration.getOptions().get("pullBatchSize");
                String pullInterval = mqConsumerConfiguration.getOptions().get("pullInterval");
                if (StringUtils.isNotBlank(consumeFromWhere)) {
                    if (StringUtils.equals(consumeFromWhere, "CONSUME_FROM_LAST_OFFSET")) {
                        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
                    } else if (StringUtils.equals(consumeFromWhere, "CONSUME_FROM_FIRST_OFFSET")) {
                        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
                    }
                }
                if (StringUtils.isNotBlank(consumeThreadMin)) {
                    consumer.setConsumeThreadMin(Integer.parseInt(consumeThreadMin));
                }
                if (StringUtils.isNotBlank(consumeThreadMax)) {
                    consumer.setConsumeThreadMax(Integer.parseInt(consumeThreadMax));
                }
                if (StringUtils.isNotBlank(pullThresholdForQueue)) {
                    consumer.setPullThresholdForQueue(Integer.parseInt(pullThresholdForQueue));
                }
                if (StringUtils.isNotBlank(consumeMessageBatchMaxSize)) {
                    consumer.setConsumeMessageBatchMaxSize(Integer.parseInt(consumeMessageBatchMaxSize));
                }
                if (StringUtils.isNotBlank(pullBatchSize)) {
                    consumer.setPullBatchSize(Integer.parseInt(pullBatchSize));
                }
                if (StringUtils.isNotBlank(pullInterval)) {
                    consumer.setPullInterval(Integer.parseInt(pullInterval));
                }
            }

            //设置消费者监听
            if(mqConsumerConfiguration.isOrderly()){
                OrderlyRocketMQMessageListener orderlyRocketMQMessageListener=new OrderlyRocketMQMessageListener();
                orderlyRocketMQMessageListener.setProcessorList(list);
                consumer.registerMessageListener(orderlyRocketMQMessageListener);
            }else{
                ConcurrentlyRocketMQMessageListener concurrentlyRocketMQMessageListener=new ConcurrentlyRocketMQMessageListener();
                concurrentlyRocketMQMessageListener.setProcessorList(list);
                consumer.registerMessageListener(concurrentlyRocketMQMessageListener);
            }

            consumer.start();
            consumers.put(mqConsumerConfiguration.getConsumerId(), consumer);
            logger.info("MqConsumer start success "+mqConsumerConfiguration.toString());
            logger.info("MqConsumer processors size "+list.size());
            return consumer;
        } catch (Exception e) {
            logger.error("MqConsumer start error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取一个消费者
     * @param customerId
     * @return
     */
    public RocketMQMessageConsumer getConsumer(String customerId){
        if(consumers.get(customerId)!=null){
            return consumers.get(customerId);
        }
        return null;
    }

    /**
     * 停止某个消费者
     * @param customerId
     */
    public void stopConsumer(String customerId){
        if(consumers.get(customerId)!=null){
            consumers.get(customerId).shutdown();
            consumers.remove(customerId);
            logger.info("MqConsumer "+customerId+" is shutdown!");
        }
    }

    /**
     * 停止全部消费者
     */
    public void stopConsumers(){
        for(Map.Entry<String, RocketMQMessageConsumer> consumerEntry:consumers.entrySet()){
            consumerEntry.getValue().shutdown();
            logger.info("MqConsumer "+consumerEntry.getKey()+" is shutdown!");
        }
        consumers.clear();
    }

}
