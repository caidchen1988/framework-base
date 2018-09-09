package com.zwt.framework.es.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zwt
 * @detail ES查询异常处理类
 * @date 2018/8/30
 * @since 1.0
 */
public class ElasticSearchExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(ElasticSearchExceptionHandler.class);
    private ElasticSearchExceptionHandler() {
    }

    /**
     * 处理异常：忽略自定义的异常或继续抛出ElasticSearchException.
     *
     * @param cause 待处理的异常.
     */
    public static void handleException(final Exception cause) {
        if (isIgnoredException(cause) || isIgnoredException(cause.getCause())) {
            log.debug("framework-es: ignored exception for: {}", cause.getMessage());
        } else if (cause instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        } else {
            throw new ElasticSearchException(cause);
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
