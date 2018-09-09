/**
 * <br>项目名: mq-util
 * <br>文件名: MqProducerParser.java
 * <br>Copyright 2015 恒昌互联网运营中心
 */
package com.zwt.framework.rocketmq.spring.config;

import java.util.ArrayList;
import java.util.List;

import com.zwt.framework.rocketmq.spring.config.support.MqListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;


public class MqListenerParser implements BeanDefinitionParser {
	
	public MqListenerParser(){
		
	}
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String listenerId = element.getAttribute("listenerId");
		String orderly = element.getAttribute("orderly");
		boolean isOrderly=false;
		if(orderly!=null&&orderly.equalsIgnoreCase("true")){
			isOrderly=true;
		}
		List<String> processorRefList=new ArrayList<String>();
		Element processorRootElement=DomUtils.getChildElementByTagName(element, "processorList");
		
		List<Element> processorElement = DomUtils.getChildElements(processorRootElement);
		
		for(Element e:processorElement){
			String ref=e.getAttribute("ref");
			processorRefList.add(ref);
		}
		if(processorRefList.size()==0){
			throw new RuntimeException("processor is null!");
		}
		
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(MqListener.class);
//		beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		beanDefinition.setLazyInit(false);
		beanDefinition.getPropertyValues().addPropertyValue("listenerId", listenerId);
		beanDefinition.getPropertyValues().addPropertyValue("orderly", isOrderly);
        beanDefinition.getPropertyValues().addPropertyValue("processorRefList", processorRefList);
        
        parserContext.getRegistry().registerBeanDefinition(listenerId, beanDefinition);
		return beanDefinition;
	}

}
