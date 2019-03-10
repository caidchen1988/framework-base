package com.zwt.zkregistryspringbootstarter.util;

/**
 * @author zwt
 * @detail 工具类
 * @date 2018/12/27
 * @since 1.0
 */
public class ZKRegistryUtil {
    /**
     * 路径分割符
     */
    public final static String SEP_STRING = "/";
    /**
     * 注册中心路径
     */
    private static final String REGISTRY_PATH = "registry";
    /**
     * 服务地址
     */
    public static final String ADDRESS_PATH = "address";

    /**
     * HTTP服务后缀
     */
    public static final String HTTP_SERVICE_SUFFIX = "-SERVICE";

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
     * 获取注册中心根路径
     * @return
     */
    public static String getZKRegistryPath() {
        return SEP_STRING + REGISTRY_PATH;
    }
}
