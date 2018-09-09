package com.zwt.framework.redis.exception;

/**
 * @author zwt
 * @detail Redis异常类
 * @date 2018/9/3
 * @since 1.0
 */
public class RedisException extends RuntimeException {

    private static final long serialVersionUID = 938922976328726204L;

    public RedisException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public RedisException(final Exception cause) {
        super(cause);
    }
}
