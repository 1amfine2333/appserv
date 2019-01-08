/**
 *
 */
package com.xianglin.appserv.biz.shared.impl;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.common.dal.daointerface.*;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.model.vo.AreaVo;
import com.xianglin.appserv.common.service.facade.model.vo.MemberVo;
import com.xianglin.appserv.common.service.facade.model.vo.OrganizeNoticeVo;
import com.xianglin.appserv.common.service.facade.model.vo.OrganizeVo;
import com.xianglin.xlStation.common.service.facade.userFacade.MessageFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.biz.shared.GroupManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 群组处理器实现类
 *
 * @author Yao 2016年2月25日上午11:13:03
 */
@Service
public class GroupManagerImpl implements GroupManager {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(GroupManagerImpl.class);
    private static final String organizationUserNotApp = "INVITE_UNREGISTERED_PEOPLE_FOR_ORGANIZATION";
    private static final String organizationUserApp = "INVITE_REGISTERED_PEOPLE_FOR_ORGANIZATION";
    private static final String groupUserNotApp = "INVITE_UNREGISTERED_PEOPLE_FOR_CHAT";
    private static final String groupUserApp = "INVITE_REGISTERED_PEOPLE_FOR_CHAT";

    @Autowired
    private AppGroupDAO appGroupDAO;

    @Autowired
    private AppGroupMemberDAO appGroupMemberDAO;

    @Autowired
    private AppGroupNoticeDAO appGroupNoticeDAO;

    @Autowired
    private AppGroupApplyDAO appGroupApplyDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MessageFacade messageFacade;


    @Override
    public AppGroup Group(long id) {
        return appGroupDAO.selectByPrimaryKey(id);
    }

    @Override
    public List<AppGroupNotice> queryNotices(Map<String, Object> paras) {
        return appGroupNoticeDAO.queryGroupNotice(paras);
    }

    @Override
    public List<AppGroupMember> queryMembers(Map<String, Object> paras) {
        return appGroupMemberDAO.queryGroupMember(paras);
    }

    @Override
    public List<AppGroup> queryGroup(Map<String, Object> paras) {
        return appGroupDAO.queryGroup(paras);
    }

    @Override
    public AppGroupMember groupMember(long id) {
        return appGroupMemberDAO.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteMember(long id) {
        return appGroupMemberDAO.deleteMember(id) == 1;
    }

    @Override
    public Boolean insertApply(AppGroupApply appGroupApply) {
        return appGroupApplyDAO.insert(appGroupApply) == 1;
    }

    @Override
    public List<AppGroupApply> queryGroupApply(Map<String, Object> paras) {
        return appGroupApplyDAO.queryGroupApply(paras);
    }

    @Override
    public AppGroupApply groupApply(Long id) {
        return appGroupApplyDAO.selectByPrimaryKey(id);
    }

    @Override
    public Boolean confirmApply(AppGroupApply appGroupApply) {
        return appGroupApplyDAO.updateByPrimaryKeySelective(appGroupApply) == 1;
    }

    @Override
    public Boolean create(AppGroup appGroup) {
        //查询用户信息
        User u = userDAO.selectByPartyId(appGroup.getManagePartyId());
        if (StringUtils.isNotEmpty(u.getDistrict())) {
            AreaVo area = JSON.parseObject(u.getDistrict(), AreaVo.class);
            if (area != null) {
                AreaVo vo = new AreaVo();
                String code = vo.selectCode(area);
                appGroup.setDistrict(u.getDistrict());
                appGroup.setDistrictCode(code);
            }
        }
        Boolean create = appGroupDAO.insert(appGroup) == 1;
        return create;
    }

    @Override
    public Boolean updateGroup(AppGroup appGroup) {
        return appGroupDAO.updateByPrimaryKeySelective(appGroup) == 1;
    }

    @Override
    public AppGroupNotice GroupNotice(Long id) {
        return appGroupNoticeDAO.selectByPrimaryKey(id);
    }

    @Override
    public Boolean publishNotice(AppGroupNotice appGroupNotice) {
        return appGroupNoticeDAO.insert(appGroupNotice) == 1;
    }

    @Override
    public List<AppGroupNotice> queryGroupNitice(Map<String, Object> paras) {
        return appGroupNoticeDAO.query(paras);
    }

    @Override
    public Boolean insertGroupMeber(AppGroupMember appGroupMember) {
        //查用户信息
        User u = userDAO.selectByMobilePhone(appGroupMember.getTempMobilePhone());
        if (u != null) {
            appGroupMember.setPartyId(u.getPartyId());
            if (!u.getPartyId().equals(appGroupMember.getOperator())) {
                sendMessage(appGroupMember);
            }
        } else {
            sendMessage(appGroupMember);
        }
        Boolean insertGroupMember = appGroupMemberDAO.insertExceptMember(appGroupMember);
        return insertGroupMember;
    }

    /**
     * 根据条件发送短信
     */
    @Async
    private void sendMessage(AppGroupMember appGroupMember) {
        try {
            if (appGroupMember != null) {
                //查询群或组织
                AppGroup group = appGroupDAO.selectByPrimaryKey(appGroupMember.getGroupId());
                if (group != null) {
                    String phone = appGroupMember.getTempMobilePhone();
                    String smsContentTemplateCode = null;
                    String[] params = new String[]{group.getName()};
                    if (group.getGroupType().equals("O")) {     //组织
                        if (appGroupMember.getPartyId() == null) {    //不是app用户！
                            smsContentTemplateCode = organizationUserNotApp;
                        } else {     //是app用户
                            smsContentTemplateCode = organizationUserApp;
                        }
                    } else {    //群聊
                        if (appGroupMember.getPartyId() == null) {    //不是app用户
                            smsContentTemplateCode = groupUserNotApp;
                        } else {     //是app用户
                            smsContentTemplateCode = groupUserApp;
                        }
                    }
                    //关闭发送短信
//                    if (smsContentTemplateCode != null) {
//                        messageFacade.sendSmsByTemplateCode(phone, smsContentTemplateCode, params);
//                    }
                }
            }
        } catch (Exception e) {
            logger.warn("sendMessage", e);
        }
    }

    @Override
    public Boolean batchDeleteMember(Map<String, Object> paras) {
        return appGroupMemberDAO.batchDeleteMember(paras);
    }


    @Override
    public List<AppGroupMember> queryFollowAndQueryGroup(Map<String, Object> paras) {
        return appGroupMemberDAO.queryFollowAndQueryGroup(paras);
    }

    /**
     * 添加群成员，排除已存在的人员
     */
    @Override
    public Boolean insertExceptMember(AppGroupMember appGroupMember) {
        Boolean insert = appGroupMemberDAO.insertExceptMember(appGroupMember);
        if (!appGroupMember.getPartyId().equals(appGroupMember.getOperator())) {
            sendMessage(appGroupMember);
        }
        return insert;
    }

    @Override
    public List<AppGroup> queryGroupLikeName(Map<String, Object> paras) {
        return appGroupDAO.queryGroupLikeName(paras);
    }

    @Override
    public Boolean updateGroupMember(AppGroupMember appGroupMember) {
        return appGroupMemberDAO.updateByPrimaryKeySelective(appGroupMember) == 1;
    }

    @Override
    public int queryGroupMemberCount(Map<String, Object> paras) {
        return appGroupMemberDAO.queryCount(paras);
    }

    @Override
    public int queryGroupCount(Map<String, Object> paras) {
        return appGroupDAO.queryCount(paras);
    }

    @Override
    public Boolean deleteGroupApply(AppGroupApply appGroupApply) {
        return appGroupApplyDAO.updateByPrimaryKeySelective(appGroupApply) == 1;
    }

    @Override
    public List<AppGroup> queryGroupByParas(Map<String, Object> paras) {
        return appGroupDAO.queryGroupByParas(paras);
    }

    @Override
    public List<AppGroupMember> queryAppMemberByParas(Map<String, Object> paras) {
        return appGroupMemberDAO.query(paras);
    }

    @Override
    public int queryCountByParam(Map<String, Object> paras) {
        return appGroupDAO.queryCountByParam(paras);
    }

    @Override
    public int queryGroupMemberCountByParam(Map<String, Object> paras) {
        return appGroupMemberDAO.queryGroupMemberCountByParam(paras);
    }

    @Override
    public int queryGroupApplyCountByParas(Map<String, Object> paras) {
        return appGroupApplyDAO.queryCount(paras);
    }

    @Override
    public Boolean updateGroupApply(AppGroupApply build) {
        return appGroupApplyDAO.updateByPrimaryKeySelective(build)==1;
    }

    /**
     *查询群成员带删除的数据
     * @param paras
     * @return
     */
    @Override
    public List<AppGroupMember> queryMembersV2(Map<String, Object> paras) {
        return appGroupMemberDAO.queryV2(paras);
    }

    @Override
    public List<AppGroupApply> queryGroupApplyByParas(Map<String, Object> paras) {
        return appGroupApplyDAO.query(paras);
    }
}
