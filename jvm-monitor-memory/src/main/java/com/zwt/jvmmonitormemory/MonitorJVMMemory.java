package com.zwt.jvmmonitormemory;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import java.util.concurrent.*;

/**
 * JVM内存监控
 */
@ServerEndpoint("/websocket/jvm/monitor")
public class MonitorJVMMemory implements ServletContextListener {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(MonitorJVMMemory.class);
    //定时任务线程
    private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture future = null;
    private JvmTest jvmTest = new JvmTest();

    /**
     * websocket会话
     */
    private Session session;

    @OnOpen
    public void init(Session session) {
        this.session = session;
        future = executorService.scheduleAtFixedRate(() -> {
            JSONObject jsonObject = new JSONObject();
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            jsonObject.put("totalMaxMemery", memoryMXBean.getHeapMemoryUsage().getMax() >> 10 >> 10);
            jsonObject.put("totalUsedMemery", memoryMXBean.getHeapMemoryUsage().getUsed() >> 10 >> 10);
            jsonObject.put("totalInitMemery", memoryMXBean.getHeapMemoryUsage().getInit() >> 10 >> 10);
            //这里会返回老年代，新生代等内存区的使用情况，按需自取就好
            List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
            memoryPoolMXBeans.forEach((pool) -> {
                String poolName = pool.getName().trim();
                long max = pool.getUsage().getMax() >> 10 >> 10;
                long used = pool.getUsage().getUsed() >> 10 >> 10;
                long init = pool.getUsage().getInit() >> 10 >> 10;
                long maxPeak = pool.getPeakUsage().getMax() >> 10 >> 10;
                long usedPeak = pool.getPeakUsage().getUsed() >> 10 >> 10;
                long initPeak = pool.getPeakUsage().getInit() >> 10 >> 10;

                JSONObject poolJSON = new JSONObject();
                poolJSON.put("max", max);
                poolJSON.put("used", used);
                poolJSON.put("init", init);
                poolJSON.put("maxPeak", maxPeak);
                poolJSON.put("usedPeak", usedPeak);
                poolJSON.put("initPeak", initPeak);

                if ("PS Eden Space".equalsIgnoreCase(poolName)) {
                    jsonObject.put("eden", poolJSON);
                } else if ("PS Survivor Space".equalsIgnoreCase(poolName)) {
                    jsonObject.put("survivor", poolJSON);
                } else if ("PS Old Gen".equalsIgnoreCase(poolName)) {
                    jsonObject.put("old", poolJSON);
                }
            });
            //垃圾收集
            List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
            garbageCollectorMXBeans.forEach(collector -> {
                String gcName = collector.getName();
                long gcCount = collector.getCollectionCount();
                long gcTime = collector.getCollectionTime();
                JSONObject gcJSON = new JSONObject();
                gcJSON.put("gcCount", gcCount);
                gcJSON.put("gcTime", gcTime);
                if (gcName.toLowerCase().contains("scavenge")) {
                    jsonObject.put("edenGc", gcJSON);
                } else if (gcName.toLowerCase().contains("marksweep")) {
                    jsonObject.put("oldGc", gcJSON);
                }
            });
            try {
                session.getBasicRemote().sendText(jsonObject.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jvmTest.test();
    }

    /**
     * 接收信息
     */
    @OnMessage
    public void acceptMessage(String message) {
        logger.info("Accept>>>" + message);
    }

    /**
     * 关闭会话
     */
    @OnClose
    public void closeSession(CloseReason closeReason) {
        this.destory();
        logger.info(closeReason.getReasonPhrase());
    }

    /**
     * 异常处理
     */
    @OnError
    public void errorHandler(Throwable e) {
        this.destory();
        logger.info("MonitorJVMMemory websocket error ：" + e.getMessage());
    }

    /**
     * 关闭资源
     */
    private void destory() {
        try {
            if (future != null && !future.isCancelled()) {
                future.cancel(true);
            }
            if (session != null) {
                session.close();
            }
        } catch (Exception e) {
            logger.error("destory", e);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        jvmTest.stop();
        executorService.shutdownNow();
    }
}
