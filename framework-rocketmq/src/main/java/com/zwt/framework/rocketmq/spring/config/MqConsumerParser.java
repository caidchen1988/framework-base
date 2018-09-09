/**
 * <br>项目名: mq-util
 * <br>文件名: MqProducerParser.java
 * <br>Copyright 2015 恒昌互联网运营中心
 */
package com.zwt.framework.rocketmq.spring.config;

import com.zwt.framework.rocketmq.spring.config.support.MqConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MqConsumerParser implements BeanDefinitionParser {
	
	private static transient final Logger LOGGER = LoggerFactory.getLogger(MqConsumerParser.class);
	
	public MqConsumerParser(){
		
	}
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String consumerId = element.getAttribute("consumerId");
		String groupName = element.getAttribute("groupName");
		String namesrvAddr=element.getAttribute("namesrvAddr");
		String listenerRef=element.getAttribute("listenerRef");

		Map<String /*topic*/,String /*tag*/> topicAndTagMap=new HashMap<String, String>();
		Element topicRootElement=DomUtils.getChildElementByTagName(element, "topicList");
		List<Element> topicListElement = DomUtils.getChildElements(topicRootElement);
		
		for(Element e:topicListElement){
			String name=e.getAttribute("name");
			String tags=e.getAttribute("tags");
			LOGGER.debug(String.format("topicAndTagMap add topic:%s,tags:%s", name,tags));
			topicAndTagMap.put(name, tags);
		}
		if(topicAndTagMap.size()==0){
			throw new RuntimeException("topic is null!");
		}
		
		
		Map<String,String> options=new HashMap<String,String>();//消费者参数
		String consumeFromWhere=element.getAttribute("consumeFromWhere");
		String consumeThreadMin=element.getAttribute("consumeThreadMin");
		String consumeThreadMax=element.getAttribute("consumeThreadMax");
		String pullThresholdForQueue=element.getAttribute("pullThresholdForQueue");
		String consumeMessageBatchMaxSize=element.getAttribute("consumeMessageBatchMaxSize");
		String pullBatchSize=element.getAttribute("pullBatchSize");
		String pullInterval=element.getAttribute("pullInterval");
		options.put("consumeFromWhere", consumeFromWhere);
		options.put("consumeThreadMin", consumeThreadMin);
		options.put("consumeThreadMax", consumeThreadMax);
		options.put("pullThresholdForQueue", pullThresholdForQueue);
		options.put("consumeMessageBatchMaxSize", consumeMessageBatchMaxSize);
		options.put("pullBatchSize", pullBatchSize);
		options.put("pullInterval", pullInterval);
		
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(MqConsumer.class);
		beanDefinition.setLazyInit(false);
		//beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		beanDefinition.getPropertyValues().addPropertyValue("consumerId", consumerId);
        beanDefinition.getPropertyValues().addPropertyValue("groupName", groupName);
        beanDefinition.getPropertyValues().addPropertyValue("namesrvAddr", namesrvAddr);
        beanDefinition.getPropertyValues().addPropertyValue("topicAndTagMap", topicAndTagMap);
        beanDefinition.getPropertyValues().addPropertyValue("listenerRef", listenerRef);
        beanDefinition.getPropertyValues().addPropertyValue("options", options);
		parserContext.getRegistry().registerBeanDefinition(consumerId, beanDefinition);

		return beanDefinition;
	}

}
