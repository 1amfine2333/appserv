package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppFilofaxAccount;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppFilofaxAccountDAO extends BaseDAO<AppFilofaxAccount>{

    List<AppFilofaxAccount> selectList(Map<String, Object> paras);

    /**账号余额调整，保证当前余额和明细中的余额和保持一致
     * @param id
     * @return
     */
    int updateAccountBalance(AppFilofaxAccount account);
}
