package com.zwt.framework.config.env;

import com.zwt.framework.utils.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * @author zwt
 * @detail
 * @date 2018/9/5
 * @since 1.0
 */
public class ConfigCenterEnv {
    /**
     * env prop
     */
    public static final String DEFAULT_PROPERTIES_FILE = "property/config-center.properties";
    public static final String LOCAL_PROPERTIES_ZK_ADDRESS_KEY = "configCenter.zkAddress";
    public static final String LOCAL_PROPERTIES_SYS_NAME = "configCenter.sysName";
    public static final String LOCAL_PROPERTIES_CONNECT_TIMEOUT_MS = "configCenter.connectTimeoutMs";
    public static final String LOCAL_PROPERTIES_SESSION_TIMEOUT_MS = "configCenter.sessionTimeoutMs";
    public static final String LOCAL_PROPERTIES_RETRY_INTERVAL = "configCenter.retryInterval";

    /**
     * 本地配置：优先加载该 "本地配置文件" 中的配置数据，其次加载配置中心中配置数据
     */
    public static String LOCAL_PROPERTIES_FILE_PATH;
    public static String ZK_ADDRESS;
    public static String ZK_NAMESPACE;
    public static int ZK_CONNECT_TIMEOUT_MS;
    public static int ZK_SESSION_TIMEOUT_MS;
    public static int ZK_SESSION_RETRY_INTERVAL_MS;

    public static void init() {
        // env prop
        Properties envProperties = PropertiesUtils.loadLocalProperties(ConfigCenterEnv.DEFAULT_PROPERTIES_FILE);

        // env param
        ZK_ADDRESS = envProperties.getProperty(LOCAL_PROPERTIES_ZK_ADDRESS_KEY);
        ZK_NAMESPACE = envProperties.getProperty(LOCAL_PROPERTIES_SYS_NAME);
        ZK_CONNECT_TIMEOUT_MS = Integer.parseInt(envProperties.getProperty(LOCAL_PROPERTIES_CONNECT_TIMEOUT_MS,60000+""));
        ZK_SESSION_TIMEOUT_MS = Integer.parseInt(envProperties.getProperty(LOCAL_PROPERTIES_SESSION_TIMEOUT_MS,60000+""));
        ZK_SESSION_RETRY_INTERVAL_MS = Integer.parseInt(envProperties.getProperty(LOCAL_PROPERTIES_RETRY_INTERVAL,1000+""));

        if (StringUtils.isBlank(ZK_ADDRESS)|| StringUtils.isBlank(ZK_NAMESPACE)) {
            throw new RuntimeException(String.format("配置中心配置文件:%s，zkAddress与sysName不能为空",DEFAULT_PROPERTIES_FILE));
        }
    }
}
