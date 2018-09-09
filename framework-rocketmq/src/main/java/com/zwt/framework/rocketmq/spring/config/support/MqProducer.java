/**
 * <br>项目名: mq-util
 * <br>文件名: MqProducerParser.java
 * <br>Copyright 2015 恒昌互联网运营中心
 */
package com.zwt.framework.rocketmq.spring.config.support;

import com.zwt.framework.rocketmq.factory.RocketMQFactory;
import com.zwt.framework.rocketmq.producer.RocketMQMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

public class MqProducer implements FactoryBean<RocketMQMessageProducer>, InitializingBean, DisposableBean {
	private static Logger LOGGER = LoggerFactory.getLogger(MqProducer.class);
	/**
	 * 生产者ID
	 */
	private String producerId;
	/**
	 * rocketMQ生产者群组名字
	 */
	private String groupName;

	/**
	 * rocketMQ的nameServer地址
	 */
	private String namesrvAddr;

	/**
	 * 生产者参数
	 */
	private Map<String,String> options;
	
	private RocketMQMessageProducer producer;
	
	private void init(){
		LOGGER.info("init MqProducer:"+this.toString());
		this.producer= RocketMQFactory.getInstance().createProducer(this.producerId,this.groupName, this.namesrvAddr,this.options);
	}
	
	@Override
	public void destroy() throws Exception {
		RocketMQFactory.getInstance().stopProducer(producerId);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	@Override
	public RocketMQMessageProducer getObject() throws Exception {
		if(this.producer==null){
			init();
		}
		return this.producer;
	}

	@Override
	public Class<?> getObjectType() {
		return RocketMQMessageProducer.class;
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
	 * 设置 生产者ID
	 * @param producerId 
	 */
	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	/** 
	 * 设置 生产者参数
	 * @param options 
	 */
	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

    @Override
    public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("MqProducer [producerId=");
	    builder.append(producerId);
	    builder.append(", groupName=");
	    builder.append(groupName);
	    builder.append(", namesrvAddr=");
	    builder.append(namesrvAddr);
	    builder.append(", options=");
	    builder.append(options);
	    builder.append("]");
	    return builder.toString();
    }

}
