package com.xianglin.appserv.core.service;

import com.xianglin.appserv.common.dal.dataobject.UserInfoWrap;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2017/1/3
 * Time: 16:25
 */
@Deprecated
public interface UserRelationCoreService {

    /**
     * 执行关注操作
     * @param fromPartyId 关注者
     * @param toPartyId   被关注者
     * @param operationType 关注还是取关
     * @return
     */
    String follow(Long fromPartyId,Long toPartyId,String operationType);

    /**
     * 查询粉丝数量
     * @param toPartyId
     * @param fromPartyId
     *
     * @return
     */
//    Integer countFansOrFollows(Long toPartyId,Long fromPartyId);

    /**
     * 查询粉丝和关注数量
     * @param partyId
     * @return
     */
    UserInfoWrap selectFansAndFollows(Long partyId);

    /**
     * 查询关注列表
     * @param partyId
     * @param bothStatus
     * @param toPartyIds
     * @param startPage
     * @param pageSize
     * @return
     */
    List<UserInfoWrap> queryList(Long partyId, String bothStatus,List<Long> toPartyIds,Integer startPage,Integer pageSize) ;

}
