package com.zwt.framework.utils.util.concurrent;

/**
 * 消费者模板
 * @param <C_E>
 */
public interface ConsumerTemplate<C_E> {
	/**
	 * 消费数据
	 * @param context
	 * @throws Exception
	 */
	void consumption(Context<C_E> context) throws Exception;
}
