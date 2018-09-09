package com.zwt.framework.rocketmq.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/9/5
 * @since 1.0
 */
public abstract class RocketMQMessageListenerAbstract {
    List<IProcessor> processorList;

    /**
     * 获取  processorList
     * @return processorList
     */
    public List<IProcessor> getProcessorList() {
        return processorList;
    }

    /**
     * 设置 processorList
     * @param processorList
     */
    public void setProcessorList(List<IProcessor> processorList) {
        this.processorList = processorList;
    }
    protected void process(IProcessor processor, MessageExt message) throws Exception{
        //do something  ex. monitor
        try {
            // 处理消息
            processor.handleMessage(message);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
