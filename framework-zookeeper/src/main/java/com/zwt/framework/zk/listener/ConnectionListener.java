package com.zwt.framework.zk.listener;

/**
 * @author zwt
 * @detail  连接监听端口
 * @date 2018/9/4
 * @since 1.0
 */
public interface ConnectionListener {
    public enum State {
        /**未连接*/
        DISCONNECTED,
        /**已连接*/
        CONNECTED,
        /**重新连接*/
        RECONNECTED
    }

    void stateChanged(State state);
}
