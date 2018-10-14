package com.zwt.framework.utils.util.java8.test8.strategy;

/**
 * @author zwt
 * @detail 策略模式
 * @date 2018/10/11
 * @since 1.0
 */
public class Validator {
    private final ValidationStrategy strategy;

    public Validator(ValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean validate(String s){
        return strategy.execute(s);
    }
}
