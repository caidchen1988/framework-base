package com.zwt.rocketmqspringboot.config;

import java.util.Map;

public class MqProducerConfiguration {
	/**
	 * 生产者ID，用于生产者唯一标识
	 */
	private String producerId;
	/**
	 * rocketMQ生产者群组名字，可以把一类的生产者归为一组
	 */
	private String groupName;

	/**
	 * rocketMQ的nameServer地址，服务地址
	 */
	private String namesrvAddr;

	/**
	 * 发送消息超时时间，单位毫秒
	 */
	private Integer sendMsgTimeout;
	/**
	 * 限制的消息大小，默认131072（128kb）
	 */
	private Integer maxMessageSize;
	/**
	 * 生产者其他参数
	 */
	private Map<String,String> options;


	public String getProducerId() {
		return producerId;
	}

	public void setProducerId(String producerId) {
		this.producerId = producerId;
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

	public Integer getSendMsgTimeout() {
		return sendMsgTimeout;
	}

	public void setSendMsgTimeout(Integer sendMsgTimeout) {
		this.sendMsgTimeout = sendMsgTimeout;
	}

	public Integer getMaxMessageSize() {
		return maxMessageSize;
	}

	public void setMaxMessageSize(Integer maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "MqProducerConfiguration{" +
				"producerId='" + producerId + '\'' +
				", groupName='" + groupName + '\'' +
				", namesrvAddr='" + namesrvAddr + '\'' +
				", sendMsgTimeout=" + sendMsgTimeout +
				", maxMessageSize=" + maxMessageSize +
				", options=" + options +
				'}';
	}
}
