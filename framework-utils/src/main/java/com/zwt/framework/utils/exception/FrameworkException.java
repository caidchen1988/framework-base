package com.zwt.framework.utils.exception;

/**
 * @author zwt
 * @detail  框架异常处理类
 * @date 2018/9/5
 * @since 1.0
 */
public class FrameworkException extends RuntimeException {

    private static final long serialVersionUID = -6722995484124847145L;

    public FrameworkException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public FrameworkException(final Exception cause) {
        super(cause);
    }
}
