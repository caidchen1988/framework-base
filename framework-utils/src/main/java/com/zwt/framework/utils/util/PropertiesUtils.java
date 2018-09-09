package com.zwt.framework.utils.util;

import com.zwt.framework.utils.exception.LocalPropertiesFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zwt
 * @detail 文件加在工具类
 * @date 2018/9/5
 * @since 1.0
 */
public class PropertiesUtils {
    public static Properties loadLocalProperties(String localPropertiesPath) {
        Properties result = new Properties();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(localPropertiesPath)) {
            if (null == input) {
                throw new LocalPropertiesFileNotFoundException(localPropertiesPath);
            }
            result.load(input);
        } catch (final IOException ex) {
            throw new LocalPropertiesFileNotFoundException(ex);
        }
        return result;
    }
}
