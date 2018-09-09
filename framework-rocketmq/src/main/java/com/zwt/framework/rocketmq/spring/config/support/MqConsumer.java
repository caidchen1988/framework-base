/**
 * <br>项目名: mq-util
 * <br>文件名: MqProducerParser.java
 * <br>Copyright 2015 恒昌互联网运营中心
 */
package com.zwt.framework.rocketmq.spring.config.support;

import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.zwt.framework.rocketmq.consumer.RocketMQMessageConsumer;
import com.zwt.framework.rocketmq.factory.RocketMQFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class MqConsumer implements FactoryBean<RocketMQMessageConsumer>, InitializingBean, DisposableBean,ApplicationContextAware {
	private static transient final Logger LOGGER = LoggerFactory.getLogger(MqConsumer.class);
	private ApplicationContext applicationContext;

	/**
	 * rocketMQ消费者群组名字
	 */
	private String groupName;


	/**
	 * rocketMQ的nameServer地址
	 */
	private String namesrvAddr;
	
	/**
	 * 消费者ID
	 */
	private String consumerId;
	/**
	 * 消费者订阅的主题与Tag，多个tag用||分割，例如：TagA || TagC || TagD
	 */
	private Map<String /*topic*/,String /*tag*/> topicAndTagMap;
	/**
	 * 消费者处理器监听
	 */
	private String listenerRef;
	
	private RocketMQMessageConsumer consumer;

	/**
	 * 消费者参数
	 */
	private Map<String,String> options;
	
	private synchronized void init(){
		MessageListener messageListener= (MessageListener)applicationContext.getBean(listenerRef);
		LOGGER.info("init "+consumerId+" MqConsumer:"+this.toString());
		this.consumer= RocketMQFactory.getInstance().createConsumer(consumerId, groupName, namesrvAddr, topicAndTagMap, messageListener,options);
	}
	
	@Override
	public void destroy() throws Exception {
		RocketMQFactory.getInstance().stopConsumer(this.consumerId);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	@Override
	public RocketMQMessageConsumer getObject() throws Exception {
		if(this.consumer==null){
			init();
		}
		return this.consumer;
	}

	@Override
	public Class<?> getObjectType() {
		return RocketMQMessageConsumer.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/** 
	 * 设置 rocketMQ生产者群组名字
	 * @param groupName 
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/** 
	 * 设置 rocketMQ的nameServer地址
	 * @param namesrvAddr 
	 */
	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	/** 
	 * 设置 消费者ID
	 * @param consumerId 
	 */
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	/** 
	 * 设置 消费者订阅的主题与Tag
	 * @param topicAndTagMap 
	 */
	public void setTopicAndTagMap(Map<String, String> topicAndTagMap) {
		this.topicAndTagMap = topicAndTagMap;
	}


	/** 
	 * 设置 消费者处理器监听
	 * @param listenerRef 
	 */
	public void setListenerRef(String listenerRef) {
		this.listenerRef = listenerRef;
	}


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	this.applicationContext=applicationContext;
    }
    
	/** 
	 * 设置 消费者参数
	 * @param options 
	 */
	public void setOptions(Map<String, String> options) {
		this.options = options;
	}


    @Override
    public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("MqConsumer [consumerId=");
	    builder.append(consumerId);
	    builder.append(", groupName=");
	    builder.append(groupName);
	    builder.append(", namesrvAddr=");
	    builder.append(namesrvAddr);
	    builder.append(", topicAndTagMap=");
	    builder.append(topicAndTagMap);
	    builder.append(", listenerRef=");
	    builder.append(listenerRef);
	    builder.append(", options=");
	    builder.append(options);
	    builder.append("]");
	    return builder.toString();
    }

}
