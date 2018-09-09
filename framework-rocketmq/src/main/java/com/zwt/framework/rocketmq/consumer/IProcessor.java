package com.zwt.framework.rocketmq.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author zwt
 * @detail
 * @date 2018/9/5
 * @since 1.0
 */
public interface IProcessor {
    /** 处理消息 */
    public void handleMessage(MessageExt msg) throws Exception;
}
