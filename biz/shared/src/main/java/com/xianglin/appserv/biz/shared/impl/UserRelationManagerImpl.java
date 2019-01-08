package com.xianglin.appserv.biz.shared.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xianglin.appserv.biz.shared.UserRelationManager;
import com.xianglin.appserv.common.dal.daointerface.AppUserRelationMapper;
import com.xianglin.appserv.common.dal.dataobject.AppUserRelation;
import com.xianglin.appserv.common.dal.dataobject.Page;
import com.xianglin.appserv.common.dal.dataobject.UserInfoWrap;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRelationManagerImpl implements UserRelationManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppUserRelationMapper userRelationMapper;

    @Override
    public String follow(Long fromPartyId, Long toPartyId, String operationType) {
        AppUserRelation relation = queryAndInit(fromPartyId, toPartyId);
        String result = "";
        if (StringUtils.equals(relation.getFollowStatus(), YesNo.N.name())) {
            relation.setFollowStatus(YesNo.Y.name());
            userRelationMapper.updateById(relation);
            //设置被关注状态
            AppUserRelation fans = queryAndInit(toPartyId, fromPartyId);
            fans.setFollowedStatus(YesNo.Y.name());
            userRelationMapper.updateById(fans);
            result = Constant.RelationStatus.FOLLOW.name();
        } else {
            relation.setFollowStatus(YesNo.N.name());
            relation.setRemarkName("");
            userRelationMapper.updateById(relation);
            //设置被关注状态
            AppUserRelation fans = queryAndInit(toPartyId, fromPartyId);
            fans.setFollowedStatus(YesNo.N.name());
            userRelationMapper.updateById(fans);
            result = Constant.RelationStatus.UNFOLLOW.name();
        }
        return result;
    }

    @Override
    public Integer countFansOrFollows(Long toPartyId, Long fromPartyId) {
        return null;
    }

    @Override
    public UserInfoWrap selectFansAndFollows(Long partyId) {
        return UserInfoWrap.builder().fansNumbers(queryRelationCount(partyId,Constant.RelationStatus.FANS.name()))
                .followNumbers(queryRelationCount(partyId,Constant.RelationStatus.FOLLOW.name())).build();
    }

    @Override
    public List<UserInfoWrap> queryList(Long partyId, String bothStatus, List<Long> toPartyIds, Integer startPage, Integer pageSize) {
        return null;
    }

    @Override
    public List<AppUserRelation> queryRelationList(Long fromPartyId, String bothStatus, Page page) {
        EntityWrapper<AppUserRelation> ew = new EntityWrapper();
        ew.eq("FROM_PARTY_ID", fromPartyId);
        if (StringUtils.equals(bothStatus, Constant.RelationStatus.FOLLOW.name())) {
            ew.eq("FOLLOW_STATUS", YesNo.Y.name());
        } else if (StringUtils.equals(bothStatus, Constant.RelationStatus.FANS.name())) {
            ew.eq("FOLLOWED_STATUS", YesNo.Y.name());
        } else {
            ew.eq("FOLLOW_STATUS", YesNo.Y.name());
            ew.eq("FOLLOWED_STATUS", YesNo.Y.name());
        }
        ew.orderBy("CREATE_DATE", false);
        return userRelationMapper.selectPage(new RowBounds(page.getOffset(), page.getPageSize()), ew);
    }

    @Override
    public Integer queryRelationCount(Long fromPartyId, String bothStatus) {
        EntityWrapper<AppUserRelation> ew = new EntityWrapper();
        ew.eq("FROM_PARTY_ID", fromPartyId);
        if (StringUtils.equals(bothStatus, Constant.RelationStatus.FANS.name())) {
            ew.eq("FOLLOWED_STATUS", YesNo.Y.name());
        } else if (StringUtils.equals(bothStatus, Constant.RelationStatus.FOLLOW.name())) {
            ew.eq("FOLLOW_STATUS", YesNo.Y.name());
        } else {
            ew.eq("FOLLOWED_STATUS", YesNo.Y.name());
            ew.eq("FOLLOW_STATUS", YesNo.Y.name());
        }
        return userRelationMapper.selectCount(ew);
    }

    @Override
    public AppUserRelation queryRelation(Long fromPartyId, Long toPartyId) {
        return queryWithDefault(fromPartyId, toPartyId);
    }

    @Override
    public Boolean updateRelationInfo(AppUserRelation relation) {
        return userRelationMapper.updateById(relation) == 1;
    }

    /**
     * 查询以及初始化
     *
     * @param fromPartyId
     * @param toPartyId
     * @return
     */
    private AppUserRelation queryAndInit(Long fromPartyId, Long toPartyId) {
        AppUserRelation relation = AppUserRelation.builder().fromPartyId(fromPartyId).toPartyId(toPartyId).build();
        relation = userRelationMapper.selectOne(relation);
        if (relation == null) {
            relation = AppUserRelation.builder().fromPartyId(fromPartyId).toPartyId(toPartyId).build();
            relation.setFollowedStatus(YesNo.N.name());
            relation.setFollowStatus(YesNo.N.name());
            userRelationMapper.insert(relation);
        }
        return relation;
    }

    /**
     * 查询以及初始化
     *
     * @param fromPartyId
     * @param toPartyId
     * @return
     */
    private AppUserRelation queryWithDefault(Long fromPartyId, Long toPartyId) {
        AppUserRelation relation = AppUserRelation.builder().fromPartyId(fromPartyId).toPartyId(toPartyId).build();
        relation = userRelationMapper.selectOne(relation);
        if (relation == null) {
            relation = AppUserRelation.builder().fromPartyId(fromPartyId).toPartyId(toPartyId)
                    .followedStatus(YesNo.N.name())
                    .followStatus(YesNo.N.name()).build();
        }
        return relation;
    }
}
