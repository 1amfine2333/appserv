/**
 *
 */
package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.AppUserRelation;
import com.xianglin.appserv.common.dal.dataobject.Page;
import com.xianglin.appserv.common.dal.dataobject.UserInfoWrap;

import java.util.List;


/** 用户好友关系管理
 * @author wanglei
 */
public interface UserRelationManager {

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
	Integer countFansOrFollows(Long toPartyId,Long fromPartyId);


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
	@Deprecated
	List<UserInfoWrap> queryList(Long partyId, String bothStatus,List<Long> toPartyIds,Integer startPage,Integer pageSize);

	/**分页查询用户关系
	 * @param fromPartyId
	 * @param bothStatus
	 * @param page
	 * @return
	 */
	List<AppUserRelation> queryRelationList(Long fromPartyId,String bothStatus,Page page);

	/**查询用户关系数量
	 * @param fromPartyId
	 * @param bothStatus
	 * @return
	 */
	Integer queryRelationCount(Long fromPartyId,String bothStatus);

	/**查询用户关系
	 * @param fromPartyId
	 * @param toPartyId
	 * @return
	 */
	AppUserRelation queryRelation(Long fromPartyId,Long toPartyId);

	/**更新关注用户信息
	 * @param relation
	 * @return
	 */
	Boolean updateRelationInfo(AppUserRelation relation);
}
