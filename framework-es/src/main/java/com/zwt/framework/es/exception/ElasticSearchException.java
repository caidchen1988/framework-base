package com.zwt.framework.es.exception;

/**
 * @author zwt
 * @detail ES查询异常类
 * @date 2018/8/30
 * @since 1.0
 */
public class ElasticSearchException extends RuntimeException{
    private static final long serialVersionUID=938922976328726204L;

    public ElasticSearchException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public ElasticSearchException(final Exception cause) {
        super(cause);
    }
}
