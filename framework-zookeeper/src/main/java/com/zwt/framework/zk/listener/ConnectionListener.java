package com.zwt.framework.zk.listener;

import com.zwt.framework.zk.constants.ZKConstants;

/**
 * @author zwt
 * @detail  连接监听端口
 * @date 2018/9/4
 * @since 1.0
 */
public interface ConnectionListener {
    void stateChanged(ZKConstants.State state);
}
