package com.zwt.framework.utils.util.concurrent;

/**
 * 并行处理器
 */
public interface ConcurrentHandler {

	/**
	 * 并行处理
	 * @throws Exception
	 */
	void run() throws Exception;
}
