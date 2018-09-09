/**
 * <br>项目名: mq-util
 * <br>文件名: MqNamespaceHandler.java
 * <br>Copyright 2015 恒昌互联网运营中心
 */
package com.zwt.framework.rocketmq.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MqNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		registerBeanDefinitionParser("producer", new MqProducerParser());
		registerBeanDefinitionParser("consumer", new MqConsumerParser());
		registerBeanDefinitionParser("listener", new MqListenerParser());
	}
}