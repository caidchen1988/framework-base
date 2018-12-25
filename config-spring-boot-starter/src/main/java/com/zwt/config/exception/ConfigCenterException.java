package com.zwt.config.exception;

/**
 * @author zwt
 * @detail 异常处理类
 * @date 2018/9/5
 * @since 1.0
 */
public class ConfigCenterException extends RuntimeException{
    public ConfigCenterException() {
        super();
    }
    public ConfigCenterException(String message) {
        super(message);
    }
    public ConfigCenterException(Throwable cause) {
        super(cause);
    }
}
