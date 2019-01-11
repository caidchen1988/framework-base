package com.zwt.frameworkdatasource.multipleDataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author zwt
 * @detail 动态数据源切换
 * @date 2019/1/7
 * @since 1.0
 */
@Aspect
@Order(-1)//保证在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    /**
     * 改变数据源
     * @param joinPoint
     * @param targetDataSource
     */
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        DatabaseType type = targetDataSource.database();
        if(DatabaseContextHolder.databaseTypes.contains(type)){
            logger.info("使用数据源："+ type);
            DatabaseContextHolder.setDatabaseType(type);
        }else{
            logger.error("未配置数据源，使用默认数据源：MASTER");
            DatabaseContextHolder.setDatabaseType(DatabaseType.MASTER);
        }
    }


    /**
     * 清除数据源
     * @param joinPoint
     * @param targetDataSource
     */
    @After("@annotation(targetDataSource)")
    public void clearDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        logger.info("清除数据源 " + targetDataSource.database());
        DatabaseContextHolder.clearDatabaseType();
    }
}
