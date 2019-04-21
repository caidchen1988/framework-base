package com.zwt.framework.utils.util.concurrent;

/**
 * 生产者模板
 * @param <C_E>
 */
public interface ProducerTemplate<C_E> {
	/**
	 * 生产数据
	 * @param context
	 * @throws Exception
	 */
	void production(Context<C_E> context) throws Exception;
}
