package com.zwt.framework.utils.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 生产、消费线程上下文
 */
public class Context<E> {

	private static final Logger log = LoggerFactory.getLogger(Context.class);

	private final LinkedBlockingQueue<E> consumptionQueue = new LinkedBlockingQueue<E>(2500);
	// 生产线程状态
	private volatile ThreadState producersThreadState;
	// 消费线程状态
	private volatile ThreadState consumersThreadState;

	/**
	 * 获取队列大小
	 * @return
	 * @throws Exception
	 */
	int getConsumptionQueueSize() {
		return consumptionQueue.size();
	}

	/**
	 * 将指定元素插入到此队列的尾部，如有必要（队列空间已满且消费线程未停止运行），则等待空间变得可用。
	 * @param e
	 * @return boolean true:插入成功;false:插入失败（消费线程已停止运行）
	 * @throws Exception
	 */
	public boolean offerDataToConsumptionQueue(E e) throws Exception {
		setProducersThreadState(ThreadState.RUNNING);
		// 如果消费线程停止了，不再生产数据
		if (ThreadState.DEAD == this.getConsumersThreadState()){
			return false;
		}
		while (true) {
			if (consumptionQueue.offer(e, 2, TimeUnit.SECONDS)){
				return true;
			}
			// 添加元素失败，很有可能是队列已满，再次检查消费线程是否工作中
			// 如果消费线程停止了，不再生产数据
			if (ThreadState.DEAD == this.getConsumersThreadState()) {
				return false;
			}
		}
	}

	/**
	 * 获取并移除此队列的头，如果此队列为空且生产线程已停止，则返回 null
	 * @return E 队列的头元素，如果队列为空且生产线程已停止则返回null
	 * @throws Exception
	 */
	public E pollDataFromConsumptionQueue() throws Exception {
		setConsumersThreadState(ThreadState.RUNNING);
		while (true) {
			E e = consumptionQueue.poll(20, TimeUnit.MILLISECONDS);
			if (e != null){
				return e;
			}
			// 没有从队列里获取到元素，并且生产线程已停止，则返回null
			if (ThreadState.DEAD == this.getProducersThreadState()){
				return null;
			}
			log.debug("demand exceeds supply(供不应求，需生产数据)...");
			Thread.sleep(50);
		}
	}

	/**
	 * 获取 producersThreadState
	 * @return producersThreadState
	 */
	ThreadState getProducersThreadState() {
		return producersThreadState;
	}

	/**
	 * 设置 producersThreadState
	 * @param producersThreadState
	 */
	void setProducersThreadState(ThreadState producersThreadState) {
		this.producersThreadState = producersThreadState;
	}

	/**
	 * 获取 consumersThreadState
	 * @return consumersThreadState
	 */
	ThreadState getConsumersThreadState() {
		return consumersThreadState;
	}

	/**
	 * 设置 consumersThreadState
	 * @param consumersThreadState
	 */
	void setConsumersThreadState(ThreadState consumersThreadState) {
		this.consumersThreadState = consumersThreadState;
	}

}
