package com.xianglin.appserv.common.dal.datasource;

/**
 * 工具类，用于缓数据源状态
 */
public class DateSourceHolder {

    private static final String MASTER = "master";
    private static final String SLAVE = "slave";

    private static final ThreadLocal<String> dataSource = new ThreadLocal<>();

    public static String getDataSource() {
        return dataSource.get();
    }

    public static boolean isMaster() {
        return getDataSource() == MASTER;
    }

    public static boolean isSlave() {
        return getDataSource() == SLAVE;
    }

    public static void setSlave() {
        dataSource.set(SLAVE);
    }

    public static void setMaster() {
        dataSource.set(MASTER);
    }

    public static void clearDataSource() {
        dataSource.remove();
    }

}
