package com.zwt.framework.redis.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zwt
 * @detail  Redis异常处理类
 * @date 2018/9/3
 * @since 1.0
 */
public class RedisExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(RedisExceptionHandler.class);
    private RedisExceptionHandler() {
    }

    /**
     * 处理异常：忽略自定义的异常或继续抛出RedisException.
     *
     * @param cause 待处理的异常.
     */
    public static void handleException(final Exception cause) {
        if (isIgnoredException(cause) || isIgnoredException(cause.getCause())) {
            log.debug("framework-redis: ignored exception for: {}", cause.getMessage());
        } else if (cause instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        } else {
            throw new RedisException(cause);
        }
    }

    private static boolean isIgnoredException(final Throwable cause) {
        if (null == cause) {
            return false;
        }
        //return cause instanceof ConnectionLossException ;
        return false;
    }
}
