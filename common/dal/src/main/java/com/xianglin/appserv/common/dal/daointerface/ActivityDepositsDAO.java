package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.ActivityDeposits;
import com.xianglin.appserv.common.dal.dataobject.ActivityInvite;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 揽储活动
 */
public interface ActivityDepositsDAO extends BaseDAO<ActivityDeposits>{

    /** 根据partyId查询
     * @param partyId
     * @return
     */
    public ActivityDeposits queryByPairyId(@Param("partyId") Long partyId);

}