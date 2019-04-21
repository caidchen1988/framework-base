package com.zwt.framework.utils.util.concurrent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 *线程单元（无返回值）
 */
public class RunnableThreadUnit implements Runnable {

	private final static Logger logger = LoggerFactory.getLogger(RunnableThreadUnit.class);

	private Object object;
	private String methodName;
	private Object[] methodParameters;

	public RunnableThreadUnit(Object object, String methodName, Object... methodParameters) {
		if (object == null || StringUtils.isBlank(methodName) || methodParameters == null) {
			throw new RuntimeException("init runnable thread unit error...");
		}
		this.object = object;
		this.methodName = methodName;
		this.methodParameters = methodParameters;
	}

	@Override
	public void run() {
		try {
			Class<?>[] classes = new Class[methodParameters.length];
			for (int i = 0; i < methodParameters.length; i++) {
				classes[i] = methodParameters[i].getClass();
			}
			Method method = object.getClass().getMethod(methodName, classes);
			method.invoke(object, methodParameters);
		} catch (Exception e) {
			logger.error(String.format("execute runnable thread unit error... service=[%s],invokeMethodName=[%s]", object, methodName), e);
		}
	}

}
