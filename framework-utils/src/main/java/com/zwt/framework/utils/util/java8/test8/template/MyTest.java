package com.zwt.framework.utils.util.java8.test8.template;

/**
 * @author zwt
 * @detail
 * @date 2018/10/11
 * @since 1.0
 */
public class MyTest {
    public static void main(String[] args) {
        new OnlineBanking().processCustomer(1234,(Long l)-> System.out.println(l));
    }
}
