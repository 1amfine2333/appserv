package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppGroup;
import com.xianglin.appserv.common.dal.dataobject.AppGroupMember;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Repository
public interface AppGroupMemberDAO extends BaseDAO<AppGroupMember> {
    List<AppGroupMember> queryGroupMember(Map<String, Object> paras);

    int deleteMember(long id);

    Boolean batchDeleteMember(Map<String, Object> paras);

    List<AppGroupMember> queryFollowAndQueryGroup(Map<String, Object> paras);

    Boolean insertExceptMember(AppGroupMember appGroupMember);

    /**新用户登陆同步partyId
     * @param mobilePhoen
     * @param partyId
     * @return
     */
    int updateMemberPartyId(@Param("mobilePhoen") String mobilePhoen, @Param("partyId") Long partyId);

    int queryCount(Map<String, Object> paras);

    List<AppGroupMember> query(Map<String, Object> paras);

    int queryGroupMemberCountByParam(Map<String, Object> paras);

    /**
     *查询群成员带删除的数据
     * @param paras
     * @return
     */
    List<AppGroupMember> queryV2(Map<String, Object> paras);
}
