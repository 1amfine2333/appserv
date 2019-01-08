package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.biz.shared.ArticleManager;
import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.UserRelationManager;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.AppArticleTip;
import com.xianglin.appserv.common.dal.dataobject.AppUserRelation;
import com.xianglin.appserv.common.dal.dataobject.Page;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.UserRelationService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.AppUserRelationVo;
import com.xianglin.appserv.common.service.facade.model.vo.AreaVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2017/1/4
 * Time: 14:41
 */
@Service("userRelationService")
@ServiceInterface
public class UserRelationServiceImpl implements UserRelationService {
    private static final Logger logger = LoggerFactory.getLogger(UserRelationServiceImpl.class);

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private UserRelationManager relationCoreService;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ArticleManager articleManager;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserRelationService.queryFollowsOrFans", description = "查询关注和粉丝列表")
    public Response<List<AppUserRelationVo>> queryFollowsOrFans(AppUserRelationVo vo) {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            List<AppUserRelation> list = relationCoreService.queryRelationList(partyId, vo.getBothStatus(), new Page(vo.getStartPage(), vo.getPageSize()));
            return list.stream().map(v -> {
                AppUserRelationVo r = AppUserRelationVo.builder().id(v.getId()).fromPartyId(v.getFromPartyId()).partyId(v.getToPartyId()).toPartyId(v.getToPartyId()).showName(v.getRemarkName()).build();
                User user = userManager.queryUser(v.getToPartyId());
                if (user == null) {
                    return null;
                }
                r.setHeadImg(user.getHeadImg());
                r.setBothStatus(v.getUserRelation());
                if(StringUtils.equals(r.getBothStatus(),Constant.RelationStatus.FANS.name())){
                    //用于查询粉丝时客户端显示加关注按钮
                    r.setBothStatus(Constant.RelationStatus.FOLLOW.name());
                }
                r.setIntroduce(user.getIntroduce());
                r.setShowName(user.showName());
                r.setTrueName(user.getTrueName());
                if(StringUtils.isNotEmpty(user.getDistrict())){
                    AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                    r.setDistrict(area.selectName());
                }
                return r;
            }).filter(v -> v != null).collect(Collectors.toList());
        });
    }


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserRelationService.findFriends", description = "找朋友")
    public Response<List<AppUserRelationVo>> findFriends(String keyWords, String district, Integer startPage, Integer pageSize) {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            logger.info("找朋友");
            Map<String, Object> map = new HashMap<>();
            map.put("keyWords", keyWords);
            map.put("likeDistrict", district);
            map.put("startPage",startPage);
            map.put("pageSize",pageSize);
            return userManager.getUsersByParam(map).stream().map(v -> {
                AppUserRelationVo r = AppUserRelationVo.builder().id(v.getId()).fromPartyId(partyId).partyId(v.getPartyId()).toPartyId(v.getPartyId()).showName(v.showName()).build();
                r.setHeadImg(v.getHeadImg());
                r.setIntroduce(v.getIntroduce());
                r.setTrueName(v.getTrueName());
                AppUserRelation relation = relationCoreService.queryRelation(partyId, r.getPartyId());
                if (relation != null) {
                    r.setBothStatus(relation.getUserRelation());
                } else {
                    r.setBothStatus(Constant.RelationStatus.FOLLOW.name());
                }
                return r;
            }).filter(v -> v != null).collect(Collectors.toList());
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserRelationService.follow", description = "关注和取消关注")
    public Response<String> follow(AppUserRelationVo vo) {
        return responseCacheUtils.execute(() -> {

            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId.equals(vo.getToPartyId())) {
                throw new BusiException("太自恋了，去关注别人吧");
            }
            logger.info("关注和取消关注");
            if (Constant.RelationStatus.FOLLOW.code.equals(vo.getBothStatus())) {
                AppArticleTip tip = new AppArticleTip();
                tip.setPartyId(partyId);
                tip.setToPartyId(vo.getToPartyId());
                tip.setTipStatus(Constant.YESNO.YES.code);
                tip.setTipType(Constant.ArticleTipType.FOLLOW.name());
                tip.setContent("关注了你");
                articleManager.addArticleTip(tip);
                //发推送
                List<Long> partyIds = new ArrayList<>(1);
                partyIds.add(vo.getToPartyId());
                if (partyIds.size() > 0) {
                    User user = userManager.queryUser(partyId);
                    String showName = user.showName();
                    messageManager.sendMsg(MsgVo.builder().partyId(vo.getToPartyId()).msgTitle("乡邻app ").isSave(Constant.YESNO.YES)
                            .message(showName + "关注了你，赶快去看看吧").msgType(Constant.MsgType.FOLLOW.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("Y").msgSource(Constant.MsgType.FOLLOW.name()).build(), partyIds);
                }
            }
            return relationCoreService.follow(partyId, vo.getToPartyId(), vo.getBothStatus());
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserRelationService.queryContact", description = "查询联系人")
    public Response<List<AppUserRelationVo>> queryContact(AppUserRelationVo vo) {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            logger.info("关注和取消关注");
            return null;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserRelationService.recommendFriend", description = "推荐好友")
    public Response<List<AppUserRelationVo>> recommendFriend() {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            logger.info("关注和取消关注");
            return null;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserRelationService.queryRelationInfo", description = "查询好友备注")
    public Response<String> queryRelationInfo(Long partyId) {
        return responseCacheUtils.execute(() -> {
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            AppUserRelation relation = relationCoreService.queryRelation(userPartyId, partyId);
            return relation.getRemarkName();
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserRelationService.modifyRelationInfo", description = "修改好友备注")
    public Response<Boolean> modifyRelationInfo(Long partyId, String remarkName) {
        return responseCacheUtils.execute(() -> {
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            AppUserRelation relation = relationCoreService.queryRelation(userPartyId, partyId);
            relation.setRemarkName(remarkName);
            return relationCoreService.updateRelationInfo(relation);
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserRelationService.queryRelationList", description = "查询关注和粉丝列表")
    public Response<List<AppUserRelationVo>> queryRelationList(AppUserRelationVo vo) {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            List<AppUserRelation> list = relationCoreService.queryRelationList(vo.getFromPartyId(), vo.getBothStatus(), new Page(vo.getStartPage(), vo.getPageSize()));
            return list.stream().map(v -> {
                AppUserRelationVo r = AppUserRelationVo.builder().id(v.getId()).fromPartyId(v.getFromPartyId()).partyId(v.getToPartyId()).toPartyId(v.getToPartyId()).showName(v.getRemarkName()).build();
                User user = userManager.queryUser(v.getToPartyId());
                if (user == null) {
                    return null;
                }
                r.setHeadImg(user.getHeadImg());
                r.setIntroduce(user.getIntroduce());
                r.setShowName(user.showName());
                r.setTrueName(user.getTrueName());
                AppUserRelation relation = relationCoreService.queryRelation(partyId, r.getPartyId());
                if (relation != null) {
                    r.setBothStatus(relation.getUserRelation());
                } else {
                    r.setBothStatus(Constant.RelationStatus.FOLLOW.name());
                }
                return r;
            }).filter(v -> v != null).collect(Collectors.toList());
        });
    }


}
