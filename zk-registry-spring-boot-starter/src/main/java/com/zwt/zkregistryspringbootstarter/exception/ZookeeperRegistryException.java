package com.zwt.zkregistryspringbootstarter.exception;

/**
 * @author zwt
 * @detail 异常处理类
 * @date 2018/9/5
 * @since 1.0
 */
public class ZookeeperRegistryException extends RuntimeException{
    public ZookeeperRegistryException() {
        super();
    }
    public ZookeeperRegistryException(String message) {
        super(message);
    }
    public ZookeeperRegistryException(Throwable cause) {
        super(cause);
    }
}
