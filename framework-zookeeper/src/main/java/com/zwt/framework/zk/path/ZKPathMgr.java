package com.zwt.framework.zk.path;

import com.zwt.framework.zk.constants.ZKConstants;

/**
 * @author zwt
 * @detail 获取zookeeper的path
 * @date 2018/9/4
 * @since 1.0
 */
public class ZKPathMgr {
    /**
     * @param path1
     * @param path2
     * @return
     */
    public static String joinPath(String path1, String path2) {
        return path1 + ZKConstants.SEP_STRING + path2;
    }

    /**
     * 获取配置中心路径
     * @return
     */
    public static String getConfigCenterPath() {
        return ZKConstants.SEP_STRING + ZKConstants.FRAMEWORK_CONFIG_PATH_KEY;
    }

    /**
     * 获取业务路径
     * @return
     */
    public static String getSysNamePath(String sysName) {
        return ZKConstants.SEP_STRING + sysName;
    }
}
