package com.zwt.configcenterserver.util;

/**
 * @author zwt
 * @detail 帮助类
 * @date 2018/12/21
 * @since 1.0
 */
public class ConfigUtil {
    /**
     * 路径分割符
     */
    public final static String SEP_STRING = "/";
    /**
     * 配置中心根路径名称
     */
    public final static String FRAMEWORK_CONFIG_PATH_KEY = "config";

    /**
     * 两个路径合并
     * @param path1
     * @param path2
     * @return
     */
    public static String joinPath(String path1, String path2) {
        return path1 + SEP_STRING + path2;
    }
    /**
     * 获取配置中心根路径
     * @return
     */
    public static String getConfigCenterPath() {
        return SEP_STRING + FRAMEWORK_CONFIG_PATH_KEY;
    }
    /**
     * 获取业务路径
     * @return
     */
    public static String getSysNamePath(String sysName) {
        return SEP_STRING + sysName;
    }
}
