package com.zwt.framework.utils.util.mail.exception;

/**
 * @author zwt
 * @detail 邮件异常处理类
 * @date 2019/1/23
 * @since 1.0
 */
public class SendMailException extends RuntimeException {

    public SendMailException(String errorMessage) {
        super(errorMessage);
    }

    public SendMailException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public SendMailException(final Exception cause) {
        super(cause);
    }
}
