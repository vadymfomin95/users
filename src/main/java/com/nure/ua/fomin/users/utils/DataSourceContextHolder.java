package com.nure.ua.fomin.users.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setCurrentDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    public static String getCurrentDataSource() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
