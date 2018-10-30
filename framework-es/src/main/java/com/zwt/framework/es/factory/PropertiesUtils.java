package com.zwt.framework.es.factory;

import com.zwt.framework.es.exception.ElasticSearchException;
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
                throw new ElasticSearchException(localPropertiesPath);
            }
            result.load(input);
        } catch (final IOException ex) {
            throw new ElasticSearchException(ex);
        }
        return result;
    }
}
