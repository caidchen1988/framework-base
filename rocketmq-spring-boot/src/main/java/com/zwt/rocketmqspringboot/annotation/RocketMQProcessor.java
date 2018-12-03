package com.zwt.rocketmqspringboot.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author zwt
 * @detail 程序处理
 * @date 2018/11/30
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RocketMQProcessor {
    /**
     * 该程序归哪个消费者监听处理
     * @return
     */
    String consumerId() default "";
}
