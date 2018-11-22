package com.zwt.framework.utils.util.java8.test1;

/**
 * @author zwt
 * @detail
 * @date 2018/10/8
 * @since 1.0
 */
public interface TestInf {
    static void doS(){

    }
    default void doSomething(){
        doS();
        System.out.println("just transaction");
    }

    void getSomething();
}
