package com.zwt.framework.utils.util.java8.test8.strategy;

/**
 * @author zwt
 * @detail
 * @date 2018/10/11
 * @since 1.0
 */
public class MyTest {
    public static void main(String[] args) {
        Validator numericValidator=new Validator((String s)->s.matches("[a-z]+"));
        boolean b1=numericValidator.validate("aaaaa");

        Validator lowerCaseValidator=new Validator((String s)->s.matches("\\d+"));
        boolean b2=lowerCaseValidator.validate("bbbbb");
    }
}
