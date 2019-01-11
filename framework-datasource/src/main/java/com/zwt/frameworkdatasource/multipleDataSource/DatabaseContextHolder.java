package com.zwt.frameworkdatasource.multipleDataSource;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zwt
 * @detail 当前ThreadLocal数据源信息设置获取类
 * @date 2018/12/29
 * @since 1.0
 */
public class DatabaseContextHolder {
    /**
     * 使用ThreadLocal管理数据源
     */
    private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<>();
    /**
     * 当前可以使用的所有数据源key值
     */
    public static Set<DatabaseType> databaseTypes = new HashSet<>();
    /**
     * 设置数据源类型
     * @param type
     */
    public static void setDatabaseType(DatabaseType type) {
        contextHolder.set(type);
    }
    /**
     * 获取数据源类型
     * @return
     */
    public static DatabaseType getDatabaseType() {
        return contextHolder.get();
    }
    /**
     * 清除数据源类型
     */
    public static void clearDatabaseType(){
        contextHolder.remove();
    }
}
