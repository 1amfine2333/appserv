package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.AppUserRelationVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2017/1/4
 * Time: 14:37
 */
public interface UserRelationService {

    /**
     * 查询关注和粉丝列表
     * @param vo
     * @return
     */
    Response<List<AppUserRelationVo>> queryRelationList(AppUserRelationVo vo);

    /**
     * 查询本人的关注或粉丝列表
     * @param vo
     * @return
     */
    Response<List<AppUserRelationVo>> queryFollowsOrFans(AppUserRelationVo vo);

    /**
     * 找朋友，根据手机号或者昵称
     * 前缀匹配
     * @param keyWords
     * @return
     */
    Response<List<AppUserRelationVo>> findFriends(String keyWords,String district,Integer startPage,Integer pageSize);

    /**
     * 关注和取消关注
     * @param vo
     * @return
     */
    Response<String> follow(AppUserRelationVo vo);

    /**
     * 查询所有联系人
     * @param vo
     * @return
     */
    Response<List<AppUserRelationVo>> queryContact(AppUserRelationVo vo);

    /**推荐好友
     * @return
     */
    Response<List<AppUserRelationVo>> recommendFriend();

    /**查询好友呢称和备注信息
     * @param partyId
     * @return
     */
    Response<String> queryRelationInfo(Long partyId);

    /**设置好友备注名
     * @param partyId
     * @param remarkName
     * @return
     */
    Response<Boolean> modifyRelationInfo(Long partyId,String remarkName);
}