package com.zwt.rocketmqspringboot.config;

import java.util.Map;

/**
 * @author zwt
 * @detail Mq消费者配置
 * @date 2018/11/30
 * @since 1.0
 */
public class MqConsumerConfiguration {
    /**
     * rocketMQ消费者群组名字
     */
    private String groupName;
    /**
     * rocketMQ的nameServer地址
     */
    private String namesrvAddr;
    /**
     * 消费者ID
     */
    private String consumerId;
    /**
     * 消费者订阅的主题与Tag，多个tag用||分割，例如：TagA || TagC || TagD
     */
    private Map<String /*topic*/,String /*tag*/> topicAndTagMap;
    /**
     * 消费者参数
     */
    private Map<String,String> options;

    /**
     * 该消费者的监听listener是否有序
     */
    private boolean orderly;

    public boolean isOrderly() {
        return orderly;
    }

    public void setOrderly(boolean orderly) {
        this.orderly = orderly;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Map<String, String> getTopicAndTagMap() {
        return topicAndTagMap;
    }

    public void setTopicAndTagMap(Map<String, String> topicAndTagMap) {
        this.topicAndTagMap = topicAndTagMap;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "MqConsumerConfiguration{" +
                "groupName='" + groupName + '\'' +
                ", namesrvAddr='" + namesrvAddr + '\'' +
                ", consumerId='" + consumerId + '\'' +
                ", topicAndTagMap=" + topicAndTagMap +
                ", options=" + options +
                ", orderly=" + orderly +
                '}';
    }
}
