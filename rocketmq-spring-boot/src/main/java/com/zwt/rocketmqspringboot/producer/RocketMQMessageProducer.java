package com.zwt.rocketmqspringboot.producer;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import com.alibaba.rocketmq.common.message.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zwt
 * @detail MQ Producer
 * @date 2018/9/5
 * @since 1.0
 */
public class RocketMQMessageProducer {
    private static final transient Logger logger = LoggerFactory.getLogger(RocketMQMessageProducer.class);
    private DefaultMQProducer producer;

    /**
     * 构造器
     * @param groupName
     * @param namesrvAddr
     */
    public RocketMQMessageProducer(String groupName, String namesrvAddr){
        this.producer=new DefaultMQProducer(groupName);
//		this.setInstanceName(instanceName);
        this.producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
        this.producer.setNamesrvAddr(namesrvAddr);
        this.producer.setVipChannelEnabled(false);
    }

    /**
     * 发送同步消息
     * @param message
     * @param businessId
     * @return
     * @throws Exception
     */
    private SendResult sendMessageSync(Message message, String businessId) throws Exception{
        if(StringUtils.isEmpty(message.getTopic())){
            throw new Exception("topic is null");
        }
        try{
            SendResult sendResult =null;
            if(businessId==null){
                sendResult = this.producer.send(message);
                logger.debug("sendMessageSync:"+message.toString());
            }else{
                sendResult = this.producer.send(message, new SelectMessageQueueByHash(), businessId);
                logger.debug("sendMessageSync orderly:"+ message.toString());
            }
            return sendResult;
        }catch(Exception e){
            logger.error("sendMessageSync error",e);
            throw e;
        }
    }
    /**
     * 发送异步消息
     * @param message
     * @param businessId
     * @return
     * @throws Exception
     */
    private void sendMessageAsync(Message message, String businessId, SendCallback sendCallback) throws Exception{
        if(StringUtils.isEmpty(message.getTopic())){
            throw new Exception("topic is null");
        }
        try{
            if(businessId==null){
                this.producer.send(message, sendCallback);
                logger.debug("sendMessageAsync:"+message.toString());
            }else{
                this.producer.send(message,  new SelectMessageQueueByHash(), businessId, sendCallback);
                logger.debug("sendMessageAsync orderly:"+ message.toString());
            }
        }catch(Exception e){
            logger.error("sendMessageAsync error",e);
            throw e;
        }
    }

    /**
     * 发送消息（随机选择队列）,此方法为同步调用，只要不抛出异常就为成功。
     * 成功也包含多种状态，例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高
     * @param message
     * @return
     * @throws Exception
     */
    public SendResult sendMessage(Message message) throws Exception{
        return sendMessageSync(message,null);
    }

    /**
     * 发送顺序消息（按照businessId的hash选择队列）
     * @param message
     * @param businessId
     * @return
     * @throws Exception
     */
    public SendResult sendMessageOrderly(Message message,String businessId) throws Exception{
        return sendMessageSync(message, businessId);
    }

    /**
     * 发送异步消息（带回调函数）。
     * @param message
     * @param sendCallback
     * @throws Exception
     */
    public void sendMessage(Message message,SendCallback sendCallback) throws Exception{
        this.sendMessageAsync(message,null,sendCallback);
    }


    /**
     * 发送异步回调的顺序消息（按照businessId的hash选择队列）
     * @param message
     * @param businessId
     * @param sendCallback
     * @throws Exception
     */
    public void sendMessageOrderly(Message message,String businessId,SendCallback sendCallback) throws Exception{
        this.sendMessageAsync(message,businessId,sendCallback);
    }

    /**
     * 启动发送者
     * @throws MQClientException
     */
    public void start() throws MQClientException {
        this.producer.start();
    }

    /**
     * 关闭发送者
     */
    public void shutdown() {
        this.producer.shutdown();
    }

    /**
     * 设置发送消息超时时间，默认3000毫秒
     * @param sendMsgTimeout
     */
    public void setSendMsgTimeout(int sendMsgTimeout) {
        this.producer.setSendMsgTimeout(sendMsgTimeout);
    }

    /**
     * 客户端限制的消息大小，超过报错，默认131072(128kb)
     * @param maxMessageSize
     */
    public void setMaxMessageSize(int maxMessageSize) {
        this.producer.setMaxMessageSize(maxMessageSize);
    }

}
