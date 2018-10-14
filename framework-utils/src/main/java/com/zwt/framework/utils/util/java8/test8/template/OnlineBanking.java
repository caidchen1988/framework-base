package com.zwt.framework.utils.util.java8.test8.template;

import java.util.function.Consumer;

/**
 * @author zwt
 * @detail
 * @date 2018/10/11
 * @since 1.0
 */
public  class OnlineBanking {
    public void processCustomer(int id, Consumer<Long> makeCustomerHappy){
        Long l=10000L;
        makeCustomerHappy.accept(l);
    }
}
