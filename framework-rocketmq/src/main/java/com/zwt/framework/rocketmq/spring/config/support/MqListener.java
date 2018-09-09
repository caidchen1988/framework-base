/**
 * <br>项目名: mq-util
 * <br>文件名: MqListener.java
 * <br>Copyright 2015 恒昌互联网运营中心
 */
package com.zwt.framework.rocketmq.spring.config.support;

import java.util.ArrayList;
import java.util.List;

import com.zwt.framework.rocketmq.consumer.IProcessor;
import com.zwt.framework.rocketmq.consumer.RocketMQMessageListenerConcurrently;
import com.zwt.framework.rocketmq.consumer.RocketMQMessageListenerOrderly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.rocketmq.client.consumer.listener.MessageListener;



public class MqListener implements FactoryBean<MessageListener>, ApplicationContextAware {
	private static transient final Logger LOGGER = LoggerFactory.getLogger(MqListener.class);
	
	private ApplicationContext applicationContext;

	private String listenerId;
	private boolean orderly;//是否是顺序消费
	/**
	 * 消费者处理器列表
	 */
	private List<String> processorRefList;


	@Override
	public MessageListener getObject() throws Exception {
		List<IProcessor> processorList=new ArrayList<IProcessor>();
		for(String processorRef:processorRefList){
			IProcessor processor=(IProcessor)applicationContext.getBean(processorRef);
			LOGGER.info(String.format("listenerId:[%s],orderly:[%s],add processorRef:[%s]", listenerId,orderly,processorRef));
			processorList.add(processor);
		}
		
		if(orderly){
			RocketMQMessageListenerOrderly rocketMQMessageListenerOrder=new RocketMQMessageListenerOrderly();
			rocketMQMessageListenerOrder.setProcessorList(processorList);
			return rocketMQMessageListenerOrder;
		}else{
			RocketMQMessageListenerConcurrently messageListener=new RocketMQMessageListenerConcurrently();
			messageListener.setProcessorList(processorList);
			return messageListener;
		}
	}

	@Override
	public Class<?> getObjectType() {
		return (orderly==true)?RocketMQMessageListenerOrderly.class:RocketMQMessageListenerConcurrently.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	this.applicationContext=applicationContext;
    }

	/** 
	 * 获取  消费者处理器列表
	 * @return processorRefList
	 */
	public List<String> getProcessorRefList() {
		return processorRefList;
	}

	/** 
	 * 设置 消费者处理器列表
	 * @param processorRefList 
	 */
	public void setProcessorRefList(List<String> processorRefList) {
		this.processorRefList = processorRefList;
	}

	/** 
	 * 获取  listenerId
	 * @return listenerId
	 */
	public String getListenerId() {
		return listenerId;
	}

	/** 
	 * 设置 listenerId
	 * @param listenerId 
	 */
	public void setListenerId(String listenerId) {
		this.listenerId = listenerId;
	}

	/** 
	 * 获取  orderly
	 * @return orderly
	 */
	public boolean isOrderly() {
		return orderly;
	}

	/** 
	 * 设置 orderly
	 * @param orderly 
	 */
	public void setOrderly(boolean orderly) {
		this.orderly = orderly;
	}




}
