package com.zwt.framework.utils.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产与消费协调者
 */
public class Coordinator {

	private static final Logger log = LoggerFactory.getLogger(Coordinator.class);
	private final Lock lock = new ReentrantLock();
	private final Condition enabledConsumers = lock.newCondition();
	private volatile boolean isEnabledForConsumers;
	private final Context<?> context;
	// 是否等待生产及消费完成
	private boolean isWaitingToFinish;
	// 最大消费线程数
	private int consumersMaxTotal;

	public Coordinator(Context<?> context, int consumersMaxTotal) {
		this(context, consumersMaxTotal, true);
	}

	public Coordinator(Context<?> context, int consumersMaxTotal, boolean isWaitingToFinish) {
		this.context = context;
		this.consumersMaxTotal = consumersMaxTotal;
		this.isWaitingToFinish = isWaitingToFinish;
	}
	/**
	 *启动生产、消费(使用简易模板，生产者和消费者方法在同一个类里)
	 * @param  simpleTemplate 简易模板
	 */
	public void start(SimpleTemplate<?> simpleTemplate) throws Exception {
		if (context.getConsumersThreadState() != null || context.getProducersThreadState() != null){
			return;
		}
		ProducersThreadUnit producersThreadUnit = new ProducersThreadUnit(simpleTemplate, "production", context);
		ConsumersThreadUnit consumersThreadUnit = new ConsumersThreadUnit(simpleTemplate, "consumption", context);
		this.start(producersThreadUnit, consumersThreadUnit);
	}


	/**
	 *启动生产、消费
	 * @param  producerTemplate 生产者模板
	 * @param  consumerTemplate 消费者模板
	 */
	public void start(ProducerTemplate<?> producerTemplate,ConsumerTemplate<?> consumerTemplate) throws Exception {
		if (context.getConsumersThreadState() != null || context.getProducersThreadState() != null){
			return;
		}
		ProducersThreadUnit producersThreadUnit = new ProducersThreadUnit(producerTemplate, "production", context);
		ConsumersThreadUnit consumersThreadUnit = new ConsumersThreadUnit(consumerTemplate, "consumption", context);
		this.start(producersThreadUnit, consumersThreadUnit);
	}

	/**
	 * 启动生产、消费（适用于生产函数、消费函数不在一个类里实现，或者一个类里有多对生产、消费组合，或者方法入参列表复杂）
	 * @param producersThreadUnit
	 * @param consumersThreadUnit
	 */
	public void start(ProducersThreadUnit producersThreadUnit, ConsumersThreadUnit consumersThreadUnit) throws Exception {
		if (context.getConsumersThreadState() != ThreadState.NEW || context.getProducersThreadState() != ThreadState.NEW){
			return;
		}
		long time = System.currentTimeMillis();
		try {
			Thread startProducersThread = this.startProducers(producersThreadUnit);
			Thread startConsumersThread = this.startConsumers(consumersThreadUnit);
			if (!this.isWaitingToFinish){
				return;
			}
			startProducersThread.join();
			if (startConsumersThread != null){
				startConsumersThread.join();
			}
		} catch (Exception e) {
			log.error("start worker error...", e);
			throw e;
		}
		log.info(String.format("processing is completed... man-hour(millisecond)=[%s]", System.currentTimeMillis() - time));
	}

	/**
	 * 启动生产
	 * @param producersThreadUnit
	 * @return
	 */
	private Thread startProducers(ProducersThreadUnit producersThreadUnit) throws Exception {
		Thread thread = new Thread(producersThreadUnit);
		thread.start();
		return thread;
	}

	/**
	 * 启动消费
	 * @param consumersThreadUnit
	 * @return
	 */
	private Thread startConsumers(ConsumersThreadUnit consumersThreadUnit) throws Exception {
		lock.lock();
		try {
			log.info("wating for producers...");
			while (!isEnabledForConsumers){
				// 等待生产（造成当前线程在接到信号、被中断或到达指定等待时间之前一直处于等待状态）,假定可能发生虚假唤醒（这并非是因为等待超时），因此总是在一个循环中等待
				// 间隔检查，防止意外情况下线程没能被成功唤醒（机率小之又小,导致线程无限挂起）
				enabledConsumers.await(5, TimeUnit.SECONDS);
			}
			if (context.getConsumptionQueueSize() == 0){
				return null;
			}
			log.info("start consumers before...");
			Thread thread = new Thread(consumersThreadUnit);
			thread.start();
			return thread;
		} catch (Exception e) {
			log.error("start consumers error...", e);
			throw e;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 生产线程
	 */
	public class ProducersThreadUnit implements Runnable {

		private Object targetObject;
		private String targetMethodName;
		private Object[] targetMethodParameters;
		private ExecutorService executorService = Executors.newFixedThreadPool(1);

		public ProducersThreadUnit(Object targetObject, String targetMethodName, Object... targetMethodParameters) {
			this.targetObject = targetObject;
			this.targetMethodName = targetMethodName;
			this.targetMethodParameters = targetMethodParameters;
			context.setProducersThreadState(ThreadState.NEW);
		}

		@Override
		public void run() {
			try {
				executorService.execute(new RunnableThreadUnit(targetObject, targetMethodName, targetMethodParameters));
				context.setProducersThreadState(ThreadState.RUNNABLE);
				executorService.shutdown();
				// 阻塞线程，直到生产中（消费队列不为空）或者停止生产
				while (!executorService.isTerminated() && context.getConsumptionQueueSize() == 0){
					Thread.sleep(20);
				}
				log.info("production the end or products have been delivered,ready to inform consumers...");
				this.wakeConsumers();
				log.info("wait until the production is complete...");
				while (!executorService.isTerminated()){
					// 等待生产完毕
					Thread.sleep(200);
				}
			} catch (Exception e) {
				log.error(String.format("production error... targetObject=[%s],targetMethodName=[%s],targetMethodParameters=[%s]", targetObject, targetMethodName, targetMethodParameters), e);
				if (!executorService.isShutdown()){
					executorService.shutdown();
				}
			} finally {
				log.info("production the end...");
				context.setProducersThreadState(ThreadState.DEAD);
				// 无论在何种情况下，必须确保能够结束挂起中的消费者线程
				isEnabledForConsumers = true;
			}
		}

		/**
		 * 向消费者发送信号
		 */
		private void wakeConsumers() {
			// 即使唤醒消费者线程失败，也可以使用该句柄结束挂起中的消费者线程
			isEnabledForConsumers = true;
			lock.lock();
			try {
				enabledConsumers.signal();
			} catch (Exception e) {
				log.error("inform to consumers error...", e);
			} finally {
				lock.unlock();
			}
		}

	}

	/**
	 * 消费线程
	 */
	public class ConsumersThreadUnit implements Runnable {

		private Object targetObject;
		private String targetMethodName;
		private Object[] targetMethodParameters;

		public ConsumersThreadUnit(Object targetObject, String targetMethodName, Object... targetMethodParameters) {
			this.targetObject = targetObject;
			this.targetMethodName = targetMethodName;
			this.targetMethodParameters = targetMethodParameters;
			context.setConsumersThreadState(ThreadState.NEW);
		}

		@Override
		public void run() {
			ThreadPoolExecutor threadPoolExecutor = null;
			int concurrencyMaxTotal = Coordinator.this.consumersMaxTotal;
			try {
				threadPoolExecutor = new ThreadPoolExecutor(0, concurrencyMaxTotal, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
				while (concurrencyMaxTotal > 0) {
					if (threadPoolExecutor.getPoolSize() > context.getConsumptionQueueSize()) {
						if (ThreadState.DEAD == context.getProducersThreadState()) {
							// 无须再提交新任务
							break;
						}else {
							Thread.sleep(50);
							// 再次检查是否有必要提交新任务
							continue;
						}
					}
					RunnableThreadUnit consumers = new RunnableThreadUnit(targetObject, targetMethodName, targetMethodParameters);
					threadPoolExecutor.execute(consumers);
					context.setConsumersThreadState(ThreadState.RUNNABLE);
					log.info("submit consumption task...");
					concurrencyMaxTotal--;
				}
				threadPoolExecutor.shutdown();
				while (!threadPoolExecutor.isTerminated()) {
					// 等待消费完毕
					Thread.sleep(100);
				}
			} catch (Exception e) {
				log.error(String.format("consumption error... targetObject=[%s],targetMethodName=[%s],targetMethodParameters=[%s]", targetObject, targetMethodName, targetMethodParameters), e);
				if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
					threadPoolExecutor.shutdown();
				}
			} finally {
				log.info("consumption the end...");
				context.setConsumersThreadState(ThreadState.DEAD);
			}
		}
	}

}