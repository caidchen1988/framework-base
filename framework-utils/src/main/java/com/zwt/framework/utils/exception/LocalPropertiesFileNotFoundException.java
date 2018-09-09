package com.zwt.framework.utils.exception;

import java.io.IOException;

/**
 * @author zwt
 * @detail 配置文件缺失异常类
 * @date 2018/9/3
 * @since 1.0
 */
public class LocalPropertiesFileNotFoundException extends FrameworkException {

    private static final long serialVersionUID = 3319901337955519555L;
    private static final String MSG = "CAN NOT found local properties files: [%s].";

    public LocalPropertiesFileNotFoundException(final String localPropertiesFileName) {
        super(MSG, localPropertiesFileName);
    }

    public LocalPropertiesFileNotFoundException(final IOException cause) {
        super(cause);
    }

}
