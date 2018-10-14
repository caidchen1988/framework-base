package com.zwt.framework.utils.util.java8.test8.observer;

/**
 * @author zwt
 * @detail
 * @date 2018/10/11
 * @since 1.0
 */
public interface Subject {
    void registerObserver(Observer observer);
    void notifyObservers(String tweet);
}
