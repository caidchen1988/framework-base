package com.zwt.frameworkdatasource.multipleDataSource;

import java.lang.annotation.*;

/**
 * @author zwt
 * @detail 目标数据源
 * @date 2019/1/7
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    /**
     * 数据源key
     * @return
     */
    DatabaseType database() default DatabaseType.MASTER;
}
