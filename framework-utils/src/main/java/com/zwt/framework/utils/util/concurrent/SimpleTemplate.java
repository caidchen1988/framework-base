package com.zwt.framework.utils.util.concurrent;

/**
 *实现生产、消费（适用于生产、消费在一个类里完成且只有一个生产、消费组合，并且方法入参列表简单）简易模板
 */
public interface SimpleTemplate<C_E> {

	/**
	 * 生产数据
	 * @param context
	 * @throws Exception
	 */
	void production(Context<C_E> context) throws Exception;

	/**
	 * 描 述：消费数据 <br>
	 * @param context
	 * @throws Exception
	 */
	void consumption(Context<C_E> context) throws Exception;

}
