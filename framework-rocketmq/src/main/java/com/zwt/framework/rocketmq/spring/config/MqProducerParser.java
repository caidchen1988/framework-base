/**
 * <br>项目名: mq-util
 * <br>文件名: MqProducerParser.java
 * <br>Copyright 2015 恒昌互联网运营中心
 */
package com.zwt.framework.rocketmq.spring.config;

import com.zwt.framework.rocketmq.spring.config.support.MqProducer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class MqProducerParser implements BeanDefinitionParser {
	
	public MqProducerParser(){
		
	}
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String producerId = element.getAttribute("producerId");
		String groupName = element.getAttribute("groupName");
		String namesrvAddr=element.getAttribute("namesrvAddr");

		Map<String,String> options=new HashMap<String,String>();//生产者参数
		String sendMsgTimeout=element.getAttribute("sendMsgTimeout");
		String maxMessageSize=element.getAttribute("maxMessageSize");
		options.put("sendMsgTimeout", sendMsgTimeout);
		options.put("maxMessageSize", maxMessageSize);
		
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(MqProducer.class);
		beanDefinition.setLazyInit(false);
		beanDefinition.getPropertyValues().addPropertyValue("producerId", producerId);
        beanDefinition.getPropertyValues().addPropertyValue("groupName", groupName);
        beanDefinition.getPropertyValues().addPropertyValue("namesrvAddr", namesrvAddr);
        beanDefinition.getPropertyValues().addPropertyValue("options", options);

        parserContext.getRegistry().registerBeanDefinition(producerId, beanDefinition);
		return beanDefinition;
	}

}
