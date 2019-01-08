package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppActivityInviteReward;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wanglei on 2018/1/23.
 */
@Repository
public interface AppActivityInviteRewardDAO extends BaseDAO<AppActivityInviteReward>{

    /**查询
     * @param req
     * @return
     */
    List<AppActivityInviteReward> select(AppActivityInviteReward req);
}
