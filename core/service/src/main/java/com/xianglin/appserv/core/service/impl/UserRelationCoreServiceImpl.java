package com.xianglin.appserv.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.common.dal.daointerface.AppUserRelationMapper;
import com.xianglin.appserv.common.dal.daointerface.UserDAO;
import com.xianglin.appserv.common.dal.dataobject.AppUserRelation;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.dal.dataobject.UserInfoWrap;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.AreaVo;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.XLCommonUtils;
import com.xianglin.appserv.core.service.UserRelationCoreService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2017/1/3
 * Time: 16:33
 */
@Service
@Transactional
public class UserRelationCoreServiceImpl implements UserRelationCoreService {

    private static final Logger logger = LoggerFactory.getLogger(UserRelationCoreServiceImpl.class);
    @Autowired
    private AppUserRelationMapper userRelationMapper;
    @Autowired
    private UserDAO userDAO;

    //private static String FROM_PARTY_ID = "fromPartyId";
    private static String  PARTY_ID = "partyId";
    private static String  BOTH_STATUS = "bothStatus";
    private static String PARTY_ID_LIST = "list";

    @Override
    @Transactional
    public String follow(Long fromPartyId, Long toPartyId, String operationType) {

        checkOperationType(fromPartyId, toPartyId, operationType);

        Map<String, Object> param = new HashMap<>();
        param.put("fromPartyId", fromPartyId);
        param.put("toPartyId", toPartyId);
        param.put("isDeleted",Constant.Delete_Y_N.N.name());
        List<AppUserRelation> list = null;
//                userRelationMapper.selectByParams(param);
        AppUserRelation relation;
        boolean add = true;
        String result = Constant.RelationStatus.UNFOLLOW.name();
        if (list != null && list.size() > 0) {
            relation = list.get(0);
            relation.setFromPartyId(fromPartyId);
            relation.setToPartyId(toPartyId);
            relation.setBothStatus(operationType);
            relation.setUpdateDate(DateUtils.getNow());
            relation.setRelation(relationDesc(fromPartyId, toPartyId, operationType));
            add = false;
        } else {
            relation = wrap(fromPartyId, toPartyId, operationType);
        }
        if (add) {//一定为添加关注，取消关注的前提是已经关注过
            //1.执行更新
            if (Constant.RelationStatus.FOLLOW.name().equals(operationType)) {
                //查看是否关注的人也关注了自己
                //如果关注的人也关注了自己，更新对方数据both_status 为互相关注
                if (countFansOrFollows(fromPartyId, toPartyId) > 0) {
                    relation.setBothStatus(Constant.RelationStatus.BOTH.name());
                    userRelationMapper.insert(relation);
                    //更新对方关注状态
                    relation.setFromPartyId(toPartyId);
                    relation.setToPartyId(fromPartyId);
                    relation.setIsDeleted(Constant.Delete_Y_N.N.code);
//                    userRelationMapper.updateBothStatus(relation);
                } else {
                    userRelationMapper.insert(relation);
                }
            }
        } else {
            relation.setIsDeleted(Constant.Delete_Y_N.N.code);
            //1.执行更新
            if (Constant.RelationStatus.FOLLOW.name().equals(operationType)) {
                //查看是否关注的人也关注了自己
                //如果关注的人也关注了自己，更新对方数据both_status 为互相关注
                if (countFansOrFollows(fromPartyId, toPartyId) > 0) {
                    relation.setBothStatus(Constant.RelationStatus.BOTH.name());
//                    userRelationMapper.updateBothStatus(relation);
                    //更新对方关注状态
                    relation.setRelation(null);
                    relation.setFromPartyId(toPartyId);
                    relation.setToPartyId(fromPartyId);
//                    userRelationMapper.updateBothStatus(relation);
                } else {
//                    userRelationMapper.updateBothStatus(relation);
                }
            } else {//取消关注
                if (countFansOrFollows(fromPartyId, toPartyId) > 0) {
//                    userRelationMapper.updateBothStatus(relation);
                    relation.setBothStatus(Constant.RelationStatus.FOLLOW.name());
                    relation.setFromPartyId(toPartyId);
                    relation.setToPartyId(fromPartyId);
                    relation.setRelation(null);
//                    userRelationMapper.updateBothStatus(relation);
                    return Constant.RelationStatus.UNFOLLOW.code;
                } else {
//                    userRelationMapper.updateBothStatus(relation);
                }
            }
        }
        return relation.getBothStatus();
    }

    public Integer countFansOrFollows(Long toPartyId, Long fromPartyId) {
        AppUserRelation ur = new AppUserRelation();
        ur.setToPartyId(toPartyId);
        ur.setFromPartyId(fromPartyId);
        ur.setIsDeleted(Constant.Delete_Y_N.N.code);
//        return userRelationMapper.selectCount(ur);
        return null;
    }

    @Override
    public UserInfoWrap selectFansAndFollows(Long partyId) {
//        return userRelationMapper.selectFansAndFollows(partyId);
        return null;
    }

    @Override
    public List<UserInfoWrap> queryList(Long partyId, String bothStatus,List<Long> toPartyIds,Integer startPage,Integer pageSize) {
        checkOperationType(partyId,null,bothStatus);
        Map<String, Object> param = new HashMap<>();
        param.put(PARTY_ID, partyId);
        param.put(BOTH_STATUS, bothStatus);
       // param.put(TO_PARTY_ID, toPartyId);
        param.put(PARTY_ID_LIST, toPartyIds);
        param.put("startPage",startPage);
        param.put("pageSize",pageSize);


        /**
         * 根据当前登录用户的partyId 和根据传入partyId查询出来的关注或者粉丝列表A 查询
         * from_party_id 且 to_party_id 在A中的集合
         *
         */
        List<UserInfoWrap> wraplist = null;
//                userRelationMapper.selectList(param);
        for(UserInfoWrap wrap : wraplist){
            if(StringUtils.isEmpty(wrap.getNikerName())){
                wrap.setNikerName("xl".concat(String.valueOf(wrap.getPartyId())));
            }
            if(StringUtils.isNotEmpty(wrap.getDistrict())) {
                AreaVo area1 = JSON.parseObject(wrap.getDistrict(), AreaVo.class);
                AreaVo areaVo1 = new AreaVo();
                if (area1 != null) {
                    String district = areaVo1.selectName(area1).replace("·",".");
                    wrap.setDistrict(district);
                }
            }
        }
        return wraplist;
    }

    private void checkOperationType(Long fromPartyId, Long toPartyId, String operationType) {
        if (fromPartyId == null && toPartyId == null) {
            throw new RuntimeException("参数不能为空：from：" + fromPartyId + " to:" + toPartyId);
        }
        if (!Constant.RelationStatus.FOLLOW.name().equals(operationType)
                && !Constant.RelationStatus.UNFOLLOW.name().equals(operationType)
                && !Constant.RelationStatus.FANS.name().equals(operationType)
                ) {
            throw new RuntimeException("不支持的操作类型" + operationType);
        }
    }

    private String relationDesc(Long fromPartyId, Long toPartyId, String bothStatus) {

        String fromNick = "";
        String toNick = "";
        User query = new User();
        query.setPartyId(fromPartyId);
        List<Long> partyIds = new ArrayList<>();
        partyIds.add(fromPartyId);
        partyIds.add(toPartyId);
        List<Map<String, Object>> mapLists = userDAO.getNickNameByPartyId(partyIds);
        for (Map<String, Object> map : mapLists) {

            if (map.get("party_id").toString().equals(String.valueOf(fromPartyId))) {

                fromNick =map.get("niker_name") == null ? "" + fromPartyId : map.get("niker_name").toString();
                continue;
            }
            if (map.get("party_id").toString().equals(String.valueOf(toPartyId))) {

                toNick = map.get("niker_name") == null ? "" + toPartyId : map.get("niker_name").toString();
                continue;
            }

        }
        if (Constant.RelationStatus.FOLLOW.name().equals(bothStatus)) {
            return fromNick + "关注了" + toNick;
        } else {
            return fromNick + "取消关注" + toNick;
        }
    }

    private AppUserRelation wrap(Long fromPartyId, Long toPartyId, String bothStatus) {
        AppUserRelation relation = new AppUserRelation();

        Date now = DateUtils.getNow();
        relation.setCreateDate(now);
        relation.setUpdateDate(now);
        relation.setRelation(relationDesc(fromPartyId, toPartyId, bothStatus));
        relation.setFromPartyId(fromPartyId);
        relation.setToPartyId(toPartyId);
        relation.setBothStatus(bothStatus);
        relation.setIsDeleted(Constant.Delete_Y_N.N.code);
        return relation;
    }
}
