package com.zwt.framework.utils.util.java8.test8.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/10/11
 * @since 1.0
 */
public class Feed implements Subject{

    private final List<Observer> observers=new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void notifyObservers(String tweet) {
        observers.forEach(observer -> observer.notify(tweet));
    }
}
