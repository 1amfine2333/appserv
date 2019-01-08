package com.xianglin.appserv.common.dal.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 多数据源事务管理器
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    private final static Logger logger = LoggerFactory.getLogger(DynamicDataSourceTransactionManager.class);

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        //获取当前事务切点的方法的读写属性（在spring的xml或者事务注解中的配置）
        boolean readOnly = definition.isReadOnly();
        if (readOnly) {
            logger.info("use slave datasource");
            DateSourceHolder.setSlave();
        } else {
            logger.info("use master datasource");
            DateSourceHolder.setMaster();
        }
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DateSourceHolder.clearDataSource();
    }
}
