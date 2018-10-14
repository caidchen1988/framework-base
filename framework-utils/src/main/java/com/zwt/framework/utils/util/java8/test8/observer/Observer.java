package com.zwt.framework.utils.util.java8.test8.observer;

/**
 * @author zwt
 * @detail
 * @date 2018/10/11
 * @since 1.0
 */
@FunctionalInterface
public interface Observer {
    void notify(String tweet);
}
