package com.zwt.framework.redis.helper.limit;

/**
 * @author zwt
 * @detail 使用redis实现高频请求访问限制
 * @date 2019/4/9
 * @since 1.0
 */
public class LimitRule {

	/**
	 * 单位访问时间
	 */
	private int seconds;

	/**
	 * 单位时间内限制的访问次数
	 */
	private int limitCount;

	/**
	 * 单位时间超过访问次数后的锁定时间
	 */
	private int lockTime;

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public int getLockTime() {
		return lockTime;
	}

	public void setLockTime(int lockTime) {
		this.lockTime = lockTime;
	}

	public boolean enableLimitLock() {
		return getLockTime() > 0 && getLimitCount() > 0;
	}
}
