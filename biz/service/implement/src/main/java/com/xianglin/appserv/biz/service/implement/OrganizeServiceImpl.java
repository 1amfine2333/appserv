package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.biz.shared.GroupManager;
import com.xianglin.appserv.biz.shared.PropExtendManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.OrganizeService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.OranginzeApplyReq;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.common.service.facade.req.MemberReq;
import com.xianglin.appserv.common.service.facade.req.OranginzeReq;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.common.util.*;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.xlStation.common.service.facade.userFacade.MessageFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wanglei on 2017/9/19.
 */
@Service("organizeService")
@ServiceInterface
public class OrganizeServiceImpl implements OrganizeService {

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    private static final String sendSmsByPhone = "INVITE_UNREGISTERED_PEOPLE_FOR_FRIEND";

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private MessageFacade messageFacade;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PropExtendManager propExtendManager;

    /**
     * 组织明细
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.organizeDetail", description = "组织明细")
    public Response<OrganizeVo> organizeDetail(long id) {
        Response<OrganizeVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            OrganizeVo vo = new OrganizeVo();
            AppGroup group = groupManager.Group(id);
            if (group != null) {
                vo = getGranizeVo(group, partyId);
            }
            resp.setResult(vo);
        } catch (Exception e) {
            logger.error("organizeDetail error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private OrganizeVo getGranizeVo(AppGroup group, Long partyId) {
        OrganizeVo vo = new OrganizeVo();
        //根据partyid查询用户
        //User user=  userManager.queryUser(partyId);
        vo.setName(group.getName());
        vo.setId(group.getId());
        AreaVo area = JSON.parseObject(group.getDistrict(), AreaVo.class);
        AreaVo areaVo = new AreaVo();
        if (area != null) {
            vo.setDistrict(areaVo.selectName(area));
        }
        vo.setManagerPartyId(group.getManagePartyId());
        User user = userManager.queryUser(group.getManagePartyId());
        if (user != null) {
            vo.setManagerName(user.getShowName());
            if(StringUtils.isNotEmpty(user.getTrueName())){
                vo.setIsAuth(true);   
            }
        }
        vo.setDateTime(DateUtils.formatDateTime("yyyy-MM-dd", group.getCreateTime()));
        vo.setIsManager(isManagerPartyId(partyId, group.getId()));
        Map<String, Object> paras = new HashMap<>();
        paras.put("groupId", group.getId());
        List<OrganizeNoticeVo> organizeVoList = new ArrayList<>();
        List<AppGroupNotice> listNotices = groupManager.queryNotices(paras);
        if (listNotices.size() > 0) {
            if (listNotices.size() > 3) {
                for (int i = 0; i < 3; i++) {
                    OrganizeNoticeVo organizeNoticeVo = getOrganizeNoticeVo(listNotices.get(i));
                    organizeVoList.add(organizeNoticeVo);
                }
            } else {
                for (int i = 0; i < listNotices.size(); i++) {
                    OrganizeNoticeVo organizeNoticeVo = getOrganizeNoticeVo(listNotices.get(i));
                    organizeVoList.add(organizeNoticeVo);
                }

            }
        }
        vo.setNotices(organizeVoList);
        List<MemberVo> listM = new ArrayList<>();
        paras.put("orderBy", "CREATE_TIME asc");
        List<AppGroupMember> listMembers = groupManager.queryMembers(paras);
        if (listMembers.size() > 0) {
            for (AppGroupMember appGroupMember : listMembers) {
                MemberVo memberVo = getMemberVo(appGroupMember);
                if (memberVo != null) {
                    listM.add(memberVo);
                }
            }
        }
        vo.setMembers(listM);
        return vo;
    }

    private OrganizeNoticeVo getOrganizeNoticeVo(AppGroupNotice appGroupNotice) {
        OrganizeNoticeVo organizeNoticeVo = null;
        User user = null;
        if (appGroupNotice != null) {
            organizeNoticeVo = OrganizeNoticeVo.builder().id(appGroupNotice.getId()).organzeId(appGroupNotice.getGroupId()).title(appGroupNotice.getTitle()).content(appGroupNotice.getContent()).dateTime(DateUtils.formatDateTime("yyyy-MM-dd", appGroupNotice.getCreateTime())).build();
            user = userManager.queryUser(appGroupNotice.getPartyId());
        }
        //获取显示姓名
        if (user != null) {
            organizeNoticeVo.setUserName(user.getShowName());
            if(StringUtils.isNotEmpty(user.getTrueName())){
                organizeNoticeVo.setIsAuth(true);  
            }
        }
        return organizeNoticeVo;
    }

    private String getUserName(Long partyId, String nikerName, String trueName) {
        String name = null;
        if (partyId != null) {
            name = "xl" + partyId;
        }
        if (StringUtils.isNotEmpty(trueName)) {
            name = trueName;
        }
        if (StringUtils.isNotEmpty(nikerName)) {
            name = nikerName;
        }
        
        return name;
    }

    private MemberVo getMemberVo(AppGroupMember appGroupMember) {
        MemberVo memberVo = null;
        if (appGroupMember.getPartyId() == null) {
            memberVo = MemberVo.builder().id(appGroupMember.getId()).name(appGroupMember.getTempName()).mobilePhone(appGroupMember.getTempMobilePhone()).imageUrl(SysConfigUtil.getStr("default_user_headimg")).groupId(appGroupMember.getGroupId()).activeStatus("N").follwoStatus("").isManager("N").build();
        } else {
            User user = userManager.getUserByLoginAccount(appGroupMember.getTempMobilePhone());
            if (user != null) {
                String headImg = null;
                if (StringUtils.isNotEmpty(user.getHeadImg())) {
                    headImg = user.getHeadImg();
                } else {
                    headImg = SysConfigUtil.getStr("default_user_headimg");
                }
                String isManager = null;
                if (StringUtils.isNotEmpty(appGroupMember.getType())) {
                    if (appGroupMember.getType().equals(Constant.MemberType.MANAGER.name())) {
                        isManager = "Y";
                    } else {
                        isManager = "N";
                    }
                } else {
                    if (isManagerPartyId(appGroupMember.getPartyId(), appGroupMember.getGroupId())) {
                        isManager = "Y";
                    } else {
                        isManager = "N";
                    }
                }
                Boolean isAuth=false;
                if(StringUtils.isNotEmpty(user.getTrueName())){
                    isAuth=true;
                }
                memberVo = MemberVo.builder().id(appGroupMember.getId()).isAuth(isAuth).name(appGroupMember.getTempName()).mobilePhone(user.getLoginName()).imageUrl(headImg).groupId(appGroupMember.getGroupId()).partyId(appGroupMember.getPartyId()).activeStatus("Y").follwoStatus("").isManager(isManager).build();
                if(StringUtils.isNotEmpty(user.getShowName())){
                    memberVo.setShowName(user.getShowName());
                    memberVo.setName(user.getShowName());
                }else{
                    memberVo.setShowName(getShowName(user));
                    memberVo.setName(getShowName(user));
                }
            } else {
                groupManager.deleteMember(appGroupMember.getId());
            }

        }
        return memberVo;
    }


    private String getShowName(User user) {
        String showName=null;
        if(StringUtils.isEmpty(user.getShowName())){
            if(user.getPartyId() != null){
                showName="xl"+user.getPartyId();
            }
            if(StringUtils.isNotEmpty(user.getTrueName())){
                showName=user.getTrueName();
            }
            if(StringUtils.isNotEmpty(user.getNikerName())){
                showName=user.getNikerName();
            }
        }
        return showName;
    }

    /**
     * 删除组织成员
     *
     * @param memberId
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.deleteMember", description = "删除组织成员")
    public Response<Boolean> deleteMember(long memberId, long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询组织
            Boolean isManager = isManagerPartyId(partyId, id);
            if (isManager) {
                AppGroupMember appGroupMember = groupManager.groupMember(memberId);
                if (appGroupMember != null) {
                    if (partyId.equals(appGroupMember.getPartyId())) {   //判断当前用户是否删除的是自己
                        //提示用户不能删除自己
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400020);
                        return resp;
                    } else {
                        Boolean deleteMember = groupManager.deleteMember(memberId);
                        resp.setResult(deleteMember);
                    }
                } else {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400022);
                    return resp;
                }
            } else {
                //提示用户无权删除组织成员
                resp.setFacade(FacadeEnums.ERROR_CHAT_400019);
                return resp;
            }
        } catch (Exception e) {
            logger.error("deleteMember error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 个人组织列表查询
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.listOrganize", description = "个人组织列表查询")
    public Response<List<OrganizeVo>> listOrganize() {
        Response<List<OrganizeVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            List<AppGroupMember> appGroupMembers = groupManager.queryMembers(paras);
            String groupIdS = null;
            List<OrganizeVo> list = new ArrayList<>();
            if (appGroupMembers.size() > 0) {
                for (int i = 0; i < appGroupMembers.size(); i++) {
                    if (groupIdS != null) {
                        groupIdS = groupIdS + "," + appGroupMembers.get(i).getGroupId();
                    } else {
                        groupIdS = String.valueOf(appGroupMembers.get(i).getGroupId());
                    }
                }
                paras.remove("partyId");
                paras.put("ids", groupIdS);
                paras.put("groupType", Constant.GroupType.O.name());
                List<AppGroup> appGroups = groupManager.queryGroup(paras);
                if (appGroups.size() > 0) {
                    for (AppGroup appGroup : appGroups) {
                        OrganizeVo organizeVo = null;
                        AreaVo area = null;
                        if(StringUtils.isNotEmpty(appGroup.getDistrict())){
                            area = JSON.parseObject(appGroup.getDistrict(), AreaVo.class);
                        }
                        AreaVo areaVo = new AreaVo();
                        String district = null;
                        if (area != null) {
                            district = areaVo.selectName(area);
                        }
                        User user = userManager.queryUser(appGroup.getManagePartyId());
                        if (user != null) {
                            organizeVo = OrganizeVo.builder().id(appGroup.getId()).name(appGroup.getName()).dateTime(DateUtils.formatDateTime("yyyy-MM-dd", appGroup.getCreateTime())).district(district).managerName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).isManager(isManagerPartyId(partyId, appGroup.getId())).build();
                            list.add(organizeVo);
                        }
                    }
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.error("listOrganize error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查询当前用户默认组织
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.defaultOrganize", description = "查询当前用户默认组织")
    public Response<OrganizeVo> defaultOrganize() {
        Response<OrganizeVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String code = null;
            String district = null;
            String groupName = null;
            //查询用户的信息
            User user = userManager.queryUser(partyId);
            if (user != null) {
                if(StringUtils.isNotEmpty(user.getDistrict())){
                    AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                    AreaVo areaVo = new AreaVo();
                    code = areaVo.selectCode(area);
                    district = areaVo.selectName(area);
                    groupName = areaVo.selectLowName(area);
                }
            }
            OrganizeVo vo = null;
            if (StringUtils.isNotEmpty(code)) {
                //根据用户的code查询群的code
                Map<String, Object> paras = new HashMap<>();
                paras.put("districtCode", code);
                paras.put("createType", Constant.CreateType.SYSTEM.name());
                List<AppGroup> appGroupList = groupManager.queryGroup(paras);
                if (appGroupList.size() > 0) {//如果组织存在，将当前用户添加进去
                    //返回该群
                    groupManager.insertExceptMember(AppGroupMember.builder().groupId(appGroupList.get(0).getId()).partyId(partyId).tempName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).tempMobilePhone(user.getLoginName()).operator(partyId).build());
                    AppGroup appGroup = appGroupList.get(0);
                    vo = getGranizeVo(appGroup, partyId);
                } else {
                    //创建群
                    AppGroup appGroup = null;
                    appGroup = AppGroup.builder().managePartyId(partyId).district(district).districtCode(code).createType(Constant.CreateType.SYSTEM.name()).groupType(Constant.GroupType.O.name()).name(groupName).imageUrl(SysConfigUtil.getStr("group_default_url")).operator(partyId).build();
                    Boolean insertGroup = groupManager.create(appGroup);
                    if (insertGroup) {
                        groupManager.insertExceptMember(AppGroupMember.builder().groupId(appGroup.getId()).partyId(partyId).tempName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).tempMobilePhone(user.getLoginName()).operator(partyId).build());
                        List<AppGroup> list = groupManager.queryGroup(paras);
                        if (list.size() > 0) {
                            vo = getGranizeVo(list.get(0), partyId);
                        }
                    }
                }
            }

            resp.setResult(vo);
        } catch (Exception e) {
            logger.error("defaultOrganize error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 群搜索，排除个人已经参加的群
     *
     * @param keyword
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.queryOrganize", description = "群搜索，排除个人已经参加的群")
    public Response<List<OrganizeVo>> queryOrganize(String keyword) {
        Response<List<OrganizeVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询当前用户已经参加的群
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("name", keyword);
            List<AppGroup> appGroups = groupManager.queryGroupLikeName(paras);
            List<OrganizeVo> list = new ArrayList<>();
            if (appGroups.size() > 0) {
                for (AppGroup appGroup : appGroups) {
                    OrganizeVo vo = getGranizeVo(appGroup, partyId);
                    list.add(vo);
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.error("queryOrganize error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 申请加入群
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.apply", description = "申请加入群")
    public Response<Boolean> apply(Long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询该群是否存在
            AppGroup appGroup = groupManager.Group(id);
            if (appGroup == null) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400021);
                return resp;
            } else {
                AppGroupApply appGroupApply = new AppGroupApply();
                appGroupApply.setPartyId(partyId);
                appGroupApply.setGroupId(id);
                Boolean insertApply = groupManager.insertApply(appGroupApply);
                resp.setResult(insertApply);
            }
        } catch (Exception e) {
            logger.error("apply error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 组织加入申请列表
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.listApply", description = "组织加入申请列表")
    public Response<List<OrganizeApplyVo>> listApply(Long id) {
        Response<List<OrganizeApplyVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询该组织是否存在
            AppGroup appGroup = groupManager.Group(id);
            List<OrganizeApplyVo> list = new ArrayList<>();
            if (appGroup != null) {
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", id);
                paras.put("limit", 20);
                List<AppGroupApply> appGroupApply = groupManager.queryGroupApply(paras);
                if (appGroupApply.size() > 0) {
                    for (AppGroupApply apply : appGroupApply) {
                        OrganizeApplyVo organizeApplyVo = new OrganizeApplyVo();
                        organizeApplyVo.setId(apply.getId());
                        organizeApplyVo.setOrganzeId(apply.getGroupId());
                        User user = userManager.queryUser(apply.getPartyId());
                        if (user != null) {
                            Boolean isAuth=false;
                            if(StringUtils.isNotEmpty(user.getTrueName())){
                                isAuth=true;
                            }
                            organizeApplyVo.setIsAuth(isAuth);
                            organizeApplyVo.setUserName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName()));
                            if (StringUtils.isNotEmpty(user.getHeadImg())) {
                                organizeApplyVo.setUserImg(user.getHeadImg());
                            } else {
                                organizeApplyVo.setUserImg((SysConfigUtil.getStr(SysConfigUtil.getStr("default_user_headimg"))));
                            }
                            organizeApplyVo.setMobilePhone(user.getLoginName());
                            if (StringUtils.isNotEmpty(user.getShowName())) {
                                organizeApplyVo.setShowName(user.getShowName());
                            }else{
                                organizeApplyVo.setShowName(getShowName(user));
                            }
                        }
                        organizeApplyVo.setStatus(apply.getStatus());
                        organizeApplyVo.setDateTime(DateUtils.formatDateTime("yyyy-MM-dd", apply.getCreateTime()));
                        list.add(organizeApplyVo);
                    }
                }
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400021);
                return resp;
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.error("queryNotice error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 确认申请
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.confirmApply", description = "确认申请")
    public Response<Boolean> confirmApply(Long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询该申请是否存在
            AppGroupApply appGroupApply = groupManager.groupApply(id);
            if (appGroupApply != null) {
                //判断是否是管理员
                Boolean isManager = isManagerPartyId(partyId, appGroupApply.getGroupId());
                if (isManager) {
                    AppGroupApply apply = new AppGroupApply();
                    apply.setId(id);
                    apply.setStatus("Y");
                    apply.setOperator(partyId);
                    Boolean confirm = groupManager.confirmApply(apply);
                    if (confirm) {
                        User user = userManager.queryUser(appGroupApply.getPartyId());
                        AppGroupMember appGroupMember = new AppGroupMember();
                        appGroupMember.setTempMobilePhone(user.getLoginName());
                        appGroupMember.setTempName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName()));
                        appGroupMember.setGroupId(appGroupApply.getGroupId());
                        appGroupMember.setOperator(partyId);
                        groupManager.insertGroupMeber(appGroupMember);
                        resp.setResult(confirm);
                    }
                } else {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400024);
                    return resp;
                }
            }
        } catch (Exception e) {
            logger.error("confirmApply error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private Boolean isManagerPartyId(Long partyId, Long groupId) {
        Boolean isManage = false;
        AppGroup appGroup = groupManager.Group(groupId);
        if (partyId.equals(appGroup.getManagePartyId())) {
            isManage = true;
        }
        return isManage;
    }


    /**
     * 添加单个组织成员
     *
     * @param member
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.addMember", description = "添加单个组织成员")
    public Response<Boolean> addMember(MemberVo member) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Boolean addMember = false;
            //根据手机号判断该成员是否在这个组织里面
            Map<String, Object> paras = new HashMap<>();
            paras.put("tempMobilePhone", member.getMobilePhone());
            paras.put("groupId", member.getGroupId());
            List<AppGroupMember> list = groupManager.queryMembers(paras);
            if (list.size() > 0) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400038);
                return resp;
            } else {
                AppGroupMember appGroupMember = new AppGroupMember();
                appGroupMember.setTempMobilePhone(member.getMobilePhone());
                appGroupMember.setTempName(member.getName());
                appGroupMember.setGroupId(member.getGroupId());
                appGroupMember.setOperator(partyId);
                addMember = groupManager.insertGroupMeber(appGroupMember);
            }
            resp.setResult(addMember);
        } catch (Exception e) {
            logger.error("addMember error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 批量添加组织成员
     *
     * @param members
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.batchAddmember", description = "批量添加组织成员")
    public Response<Boolean> batchAddmember(List<MemberVo> members) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            if (members.size() > 0) {
                //判断当前登录用户是否是组织管理员
                AppGroup appGroup = groupManager.Group(members.get(0).getGroupId());
                if (appGroup != null) {
                    Boolean isManager = isManagerPartyId(partyId, members.get(0).getGroupId());
                    if (isManager) {
                        for (int i = 0; i < members.size(); i++) {
                            AppGroupMember groupMember = new AppGroupMember();
                            groupMember.setGroupId(members.get(i).getGroupId());
                            groupMember.setTempName(members.get(i).getName());
                            groupMember.setTempMobilePhone(members.get(i).getMobilePhone());
                            groupMember.setOperator(partyId);
                            Boolean insertAppGroupMember = groupManager.insertGroupMeber(groupMember);
                            resp.setResult(insertAppGroupMember);
                        }
                    } else {
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400024);
                        return resp;
                    }
                } else {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400021);
                    return resp;
                }
            }
        } catch (Exception e) {
            logger.error("batchAddmember error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 群公告列表查询
     *
     * @param orgnId
     * @param req
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.queryNoticeList", description = "群公告列表查询")
    public Response<List<OrganizeNoticeVo>> queryNoticeList(Long orgnId, PageReq req) {
        Response<List<OrganizeNoticeVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Map<String, Object> paras = new HashMap<>();
            paras.put("groupId", orgnId);
            paras.put("pageSize", req.getPageSize());
            paras.put("startPage", req.getStartPage());
            List<AppGroupNotice> appGroupNoticeList = groupManager.queryGroupNitice(paras);
            List<OrganizeNoticeVo> list = new ArrayList<>();
            if (appGroupNoticeList.size() > 0) {
                for (AppGroupNotice groupNotice : appGroupNoticeList) {
                    User user = userManager.queryUser(groupNotice.getPartyId());
                    OrganizeNoticeVo organizeNoticeVo = getOrganizeNoticeVo(groupNotice);
                    list.add(organizeNoticeVo);
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.error("queryNotice error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 发布群公告
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.publishNotice", description = "发布群公告")
    public Response<Boolean> publishNotice(OrganizeNoticeVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            AppGroupNotice appGroupNotice = new AppGroupNotice();
            appGroupNotice.setGroupId(vo.getOrganzeId());
            appGroupNotice.setContent(vo.getContent());
            appGroupNotice.setPartyId(partyId);
            appGroupNotice.setTitle(vo.getTitle());
            Boolean publishNotice = groupManager.publishNotice(appGroupNotice);
            resp.setResult(publishNotice);
        } catch (Exception e) {
            logger.error("publishNotice error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 公告明细
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.queryNotice", description = "公告明细")
    public Response<OrganizeNoticeVo> queryNotice(Long id) {
        Response<OrganizeNoticeVo> resp = ResponseUtils.successResponse();
        try {
            Long managerPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (managerPartyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询公告
            AppGroupNotice appGroupNotice = groupManager.GroupNotice(id);
            if (appGroupNotice != null) {
                User user = userManager.queryUser(appGroupNotice.getPartyId());
                OrganizeNoticeVo vo = getOrganizeNoticeVo(appGroupNotice);
                resp.setResult(vo);
            }
        } catch (Exception e) {
            logger.error("queryNotice error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 转让管理员
     *
     * @param partyId
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.assignManager", description = "转让管理员")
    public Response<Boolean> assignManager(Long partyId, Long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long managerPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (managerPartyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            AppGroup group = groupManager.Group(id);
            if (group != null && group.getGroupType().equals(Constant.GroupType.V.name())) {    //判断是否是村管理员转让
                //将当前用户修改成普通成员
                Map<String, Object> paras = new HashMap<>();
                paras.put("partyId", managerPartyId);
                paras.put("groupId", id);
                List<AppGroupMember> list = groupManager.queryMembers(paras);
                if (list.size() > 0) {
                    if (list.get(0).getType().equals(Constant.MemberType.MANAGER.name())) {
                        groupManager.updateGroupMember(AppGroupMember.builder().id(list.get(0).getId()).type(Constant.MemberType.MEMBER.name()).build());
                        //将被转让用户修改成管理员
                        paras.put("partyId", partyId);
                        List<AppGroupMember> members = groupManager.queryMembers(paras);
                        if (members.size() > 0) {
                            Boolean update = groupManager.updateGroupMember(AppGroupMember.builder().id(members.get(0).getId()).type(Constant.MemberType.MANAGER.name()).build());
                            resp.setResult(update);
                        }
                    } else {
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400031);
                        return resp;
                    }
                }
            } else {
                Boolean isManager = isManagerPartyId(managerPartyId, id);  //判断是否是管理员
                if (isManager) {
                    if (managerPartyId.equals(partyId)) {  //判断是否转让给自己
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400035);
                        return resp;
                    } else {
                        AppGroup appGroup = new AppGroup();
                        appGroup.setId(id);
                        appGroup.setManagePartyId(partyId);
                        Boolean update = groupManager.updateGroup(appGroup);
                        resp.setResult(update);
                    }
                } else {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400031);
                    return resp;
                }
            }
        } catch (Exception e) {
            logger.error("assignManager error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 退出组织
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.exit", description = "退出组织")
    public Response<Boolean> exit(Long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询组织是否存在
            AppGroup group = groupManager.Group(id);
            if (group != null) {  //判断组织是否存在
                if (isManagerPartyId(partyId, id)) {   //判断是否是管理员
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400023);
                    return resp;
                } else {
                    AppGroupMember appGroupMember = new AppGroupMember();
                    appGroupMember.setGroupId(id);
                    appGroupMember.setPartyId(partyId);
                    Map<String, Object> paras = new HashMap<>();
                    paras.put("groupId", id);
                    paras.put("partyId", partyId);
                    List<AppGroupMember> list = groupManager.queryMembers(paras);
                    if (list.size() > 0) {
                        Boolean exit = groupManager.deleteMember(list.get(0).getId());
                        resp.setResult(exit);
                    }
                }
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400021);
                return resp;
            }
        } catch (Exception e) {
            logger.error("exit error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 创建组织
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.create", description = "创建组织")
    public Response<Boolean> create(OrganizeVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String md5 = MD5.md5(partyId + JSON.toJSONString(vo));
            if (redisUtil.isRepeat(md5, 600)) {
                resp.setFacade(FacadeEnums.ERROR_REPEAT);
                return resp;
            }
            AppGroup appGroup = null;
            appGroup = AppGroup.builder().managePartyId(partyId).groupType(Constant.GroupType.O.name()).name(vo.getName()).imageUrl(SysConfigUtil.getStr("group_default_url")).createType(Constant.CreateType.PERSONAL.name()).operator(partyId).build();
            Boolean create = groupManager.create(appGroup);
            if (create) {
                User user = userManager.queryUser(partyId);
                AppGroupMember appGroupMember = null;
                appGroupMember = AppGroupMember.builder().tempName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).tempMobilePhone(user.getLoginName()).groupId(appGroup.getId()).operator(partyId).build();
                Boolean addMember = groupManager.insertGroupMeber(appGroupMember);
                resp.setResult(addMember);
            }
        } catch (Exception e) {
            logger.error("create error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 发短信邀请好友
     *
     * @param phone
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.sendMessage", description = "发短信邀请好友")
    public Response<Boolean> sendMessage(String phone) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String smsContentTemplateCode = sendSmsByPhone;
            String[] params = new String[]{};
            com.xianglin.xlStation.base.model.Response response = messageFacade.sendSmsByTemplateCode(phone, smsContentTemplateCode, params);
            if (response.getBussinessCode() == 2000) {
                resp.setResult(true);
            }
        } catch (Exception e) {
            logger.error("sendMessage error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }

        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.deleteApply", description = "单个删除组织申请")
    public Response<Boolean> deleteApply(Long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            AppGroupApply appGroupApply = groupManager.groupApply(id);
            if (appGroupApply != null) {
                //判断当前登录用户是否是组织的管理员
                if (isManagerPartyId(partyId, appGroupApply.getGroupId())) {
                    Boolean deleteGroupApply = groupManager.deleteGroupApply(AppGroupApply.builder().id(id).isDeleted("Y").build());
                    resp.setResult(deleteGroupApply);
                } else {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400040);
                    return resp;
                }
            }
        } catch (Exception e) {
            logger.error("deleteApply error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查询村top
     */
    @Override
    public Response<List<OrganizeVo>> queryOrganizeParas(OranginzeReq oranginzeReq) {
        Response<List<OrganizeVo>> resp = ResponseUtils.successResponse();
        try {
            List<OrganizeVo> oList = new ArrayList<>();
            Map<String, Object> paras = DTOUtils.queryMap();
            if (StringUtils.isNotEmpty(oranginzeReq.getName())) {
                paras.put("likeName", oranginzeReq.getName());
            }
            if (StringUtils.isNotEmpty(oranginzeReq.getDistrict())) {
                paras.put("likeDistrict", oranginzeReq.getDistrict());
            }
            if (StringUtils.isNotEmpty(oranginzeReq.getUserName())) {
                paras.put("tempName", oranginzeReq.getUserName());
            }
            if (StringUtils.isNotEmpty(oranginzeReq.getPhone())) {
                paras.put("tempMobilePhone", oranginzeReq.getPhone());
            }
            if (StringUtils.isNotEmpty(oranginzeReq.getStatus())) {
                paras.put("status", oranginzeReq.getStatus());
            }
            paras.put("startPage", oranginzeReq.getStartPage());
            paras.put("pageSize", oranginzeReq.getPageSize());
            paras.put("groupType", Constant.GroupType.V.name());
            List<AppGroup> list = groupManager.queryGroupByParas(paras);
            if (list.size() > 0) {
                for (AppGroup appGroup : list) {
                    OrganizeVo organizeVo = new OrganizeVo();
                    organizeVo.setId(appGroup.getId());
                    organizeVo.setName(appGroup.getName());
                    organizeVo.setManagerCount(appGroup.getManagerCount());
                    organizeVo.setMemberCount(appGroup.getMemberCount());
                    AreaVo area = JSON.parseObject(appGroup.getDistrict(), AreaVo.class);
                    String district = area.selectName(area);
                    organizeVo.setDistrict(district.replace("·", ""));
                    organizeVo.setStatus(appGroup.getStatus());
                    //同步村是否开通村务状态
                    if(organizeVo.getManagerCount()>0){
                        groupManager.updateGroup(AppGroup.builder().id(appGroup.getId()).status("Y").build());
                        organizeVo.setStatus("Y"); 
                    }else{
                        groupManager.updateGroup(AppGroup.builder().id(appGroup.getId()).status("Y").build());
                        organizeVo.setStatus("N");
                    }
                    /*if (StringUtils.isEmpty(appGroup.getStatus())) {
                        paras.clear();
                        paras.put("groupId", appGroup.getId());
                        paras.put("type", Constant.MemberType.MANAGER.name());
                        List<AppGroupMember> members = groupManager.queryAppMemberByParas(paras);
                        if (members.size() > 0) {
                            groupManager.updateGroup(AppGroup.builder().id(appGroup.getId()).status("Y").build());
                            organizeVo.setStatus("Y");
                        } else {
                            groupManager.updateGroup(AppGroup.builder().id(appGroup.getId()).status("N").build());
                            organizeVo.setStatus("N");
                        }
                    } else {
                        paras.clear();
                        paras.put("groupId", appGroup.getId());
                        paras.put("type", Constant.MemberType.MANAGER.name());
                        List<AppGroupMember> members = groupManager.queryAppMemberByParas(paras);
                        if (members.size() == 0) {
                            groupManager.updateGroup(AppGroup.builder().id(appGroup.getId()).status("N").build());
                            organizeVo.setStatus("N");
                        }else{
                            groupManager.updateGroup(AppGroup.builder().id(appGroup.getId()).status("Y").build());
                            organizeVo.setStatus("Y"); 
                        }
                    } */
                    oList.add(organizeVo);
                }
            }
            resp.setResult(oList);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.error("queryOrganizeParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查某个村的成员，按条件查询
     */
    @Override
    public Response<List<MemberVo>> queryMemberByParas(MemberReq memberReq) {
        Response<List<MemberVo>> resp = ResponseUtils.successResponse();
        try {
            List<MemberVo> mList = new ArrayList<>();
            Map<String, Object> paras = DTOUtils.queryMap();
            if (StringUtils.isNotEmpty(memberReq.getName())) {
                paras.put("likeName", memberReq.getName());
            }
            paras.put("startPage", memberReq.getStartPage());
            paras.put("pageSize", memberReq.getPageSize());
            paras.put("groupId", memberReq.getGroupId());
            List<AppGroupMember> list = groupManager.queryAppMemberByParas(paras);
            if (list.size() > 0) {
                for (AppGroupMember appGroupMember : list) {
                    MemberVo memberVo = new MemberVo();
                    User user = userManager.queryUser(appGroupMember.getPartyId());
                    String type = null;
                    String isManager = "false";
                    if (user != null) {
                        if (user.getUserType().equals(Constant.UserType.user.name())) {
                            type = "村民";
                        } else {
                            type = "村长";
                        }
                        if(StringUtils.isNotEmpty(user.getShowName())){
                            memberVo.setShowName(user.getShowName());
                            memberVo.setName(user.getShowName());
                        }else{
                            memberVo.setShowName(getShowName(user));
                            memberVo.setName(getShowName(user));
                        }
                    } else {
                        type = "村民";
                    }
                    if (StringUtils.isNotEmpty(type) && appGroupMember.getType() != null) {
                        if (appGroupMember.getType().equals(Constant.MemberType.MANAGER.name())) {
                            type = type + ",管理员";
                            isManager = "true";
                        }
                    }
                    memberVo.setType(type);
                    memberVo.setId(appGroupMember.getId());
                    if (appGroupMember.getPartyId() != null) {
                        memberVo.setPartyId(appGroupMember.getPartyId());
                    }
                    memberVo.setName(appGroupMember.getTempName());
                    memberVo.setMobilePhone(appGroupMember.getTempMobilePhone());
                    memberVo.setIsManager(isManager);
                    mList.add(memberVo);
                }
            }
            resp.setResult(mList);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.error("queryMemberByParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /***
     * 根据条件差村个数
     * @param memberVo
     * @return
     */
    @Override
    public Response<Integer> queryMemberCountByParas(MemberVo memberVo) {
        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            if (StringUtils.isNotEmpty(memberVo.getName())) {
                paras.put("likeName", memberVo.getName());
            }
            paras.put("groupId", memberVo.getGroupId());
            int count = groupManager.queryGroupMemberCountByParam(paras);
            resp.setResult(count);
        } catch (Exception e) {
            logger.error("queryVillageCountByParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /***
     * 设置或取消村的管理员
     */
    @Override
    public Response<Boolean> updateVillageUserType(MemberVo memberVo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            if (memberVo != null) {
                //查询当前用户
                AppGroupMember appGroupMember = groupManager.groupMember(memberVo.getId());
                if (appGroupMember != null) {
                    appGroupMember.setType(memberVo.getType());
                    Boolean update = groupManager.updateGroupMember(appGroupMember);
                    resp.setResult(update);
                    resp.setCode(1000);
                }
            }
        } catch (Exception e) {
            logger.error("updateVillageUserType error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 根据村ID查询村管理员
     *
     * @param id
     * @return
     */
    @Override
    public Response<List<String>> queryVillageManage(Long id) {
        Response<List<String>> resp = ResponseUtils.successResponse();
        List<String> list = new ArrayList<>();
        try {
            if (id != null) {
                //查询当前用户
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", id);
                paras.put("type", Constant.MemberType.MANAGER);
                List<AppGroupMember> groupMembers = groupManager.queryMembers(paras);
                if (groupMembers.size() > 0) {
                    for (AppGroupMember appGroupMember : groupMembers) {
                        list.add(appGroupMember.getTempName());
                    }
                }
            }
            resp.setResult(list);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.error("updateVillageUserType error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.listOrganizeV2", description = "获取当前用户的组织V2")
    public Response<List<OrganizeVo>> listOrganizeV2() {
        Response<List<OrganizeVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            List<AppGroupMember> appGroupMembers = groupManager.queryMembers(paras);
            String groupIdS = null;
            List<OrganizeVo> list = new ArrayList<>();
            if (appGroupMembers.size() > 0) {
                for (int i = 0; i < appGroupMembers.size(); i++) {
                    if (groupIdS != null) {
                        groupIdS = groupIdS + "," + appGroupMembers.get(i).getGroupId();
                    } else {
                        groupIdS = String.valueOf(appGroupMembers.get(i).getGroupId());
                    }
                }
                paras.remove("partyId");
                paras.put("ids", groupIdS);
                paras.put("groupType", Constant.GroupType.O.name());
                //paras.put("createType", Constant.CreateType.PERSONAL.name());
                List<AppGroup> appGroups = groupManager.queryGroup(paras);
                if (appGroups.size() > 0) {
                    for (AppGroup appGroup : appGroups) {
                        OrganizeVo organizeVo = null;
                        AreaVo area = JSON.parseObject(appGroup.getDistrict(), AreaVo.class);
                        AreaVo areaVo = new AreaVo();
                        String district = null;
                        if (area != null) {
                            district = areaVo.selectName(area);
                        }
                        User user = userManager.queryUser(appGroup.getManagePartyId());
                        if (user != null) {
                            organizeVo = OrganizeVo.builder().id(appGroup.getId()).name(appGroup.getName()).dateTime(DateUtils.formatDateTime("yyyy-MM-dd", appGroup.getCreateTime())).district(district).managerName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).isManager(isManagerPartyId(partyId, appGroup.getId())).build();
                            List<MemberVo> listM = new ArrayList<>();
                            paras.clear();
                            paras.put("groupId", organizeVo.getId());
                            paras.put("orderBy", "CREATE_TIME asc");
                            List<AppGroupMember> listMembers = groupManager.queryMembers(paras);
                            if (listMembers.size() > 0) {
                                for (AppGroupMember appGroupMember : listMembers) {
                                    MemberVo memberVo = getMemberVo(appGroupMember);
                                    if (memberVo != null) {
                                        listM.add(memberVo);
                                    }
                                }
                            }
                            organizeVo.setMembers(listM);
                            list.add(organizeVo);
                        }
                    }
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.error("listOrganizeV2 error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /***
     * 根据条件差村个数
     * @param organizeVo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.queryVillageCountByParas", description = "根据条件差村个数")
    public Response<Integer> queryVillageCountByParas(OrganizeVo organizeVo) {
        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            if (StringUtils.isNotEmpty(organizeVo.getName())) {
                paras.put("likeName", organizeVo.getName());
            }
            if (StringUtils.isNotEmpty(organizeVo.getDistrict())) {
                paras.put("likeDistrict", organizeVo.getDistrict());
            }
            if (StringUtils.isNotEmpty(organizeVo.getDistrict())) {
                paras.put("likeDistrict", organizeVo.getDistrict());
            }
            if (StringUtils.isNotEmpty(organizeVo.getUserName())) {
                paras.put("tempName", organizeVo.getUserName());
            }
            if (StringUtils.isNotEmpty(organizeVo.getPhone())) {
                paras.put("tempMobilePhone", organizeVo.getPhone());
            }
            if (StringUtils.isNotEmpty(organizeVo.getStatus())) {
                paras.put("status", organizeVo.getStatus());
            }
            if (organizeVo.getIsManager()) {
                paras.put("count", organizeVo.getIsManager());
            }
            paras.put("groupType", Constant.GroupType.V.name());
            int count = groupManager.queryCountByParam(paras);
            resp.setResult(count);
        } catch (Exception e) {
            logger.error("queryVillageCountByParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     *返回是否申请试点村 h5
     * @param
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.isApplyVillage", description = "返回是否申请试点村")
    public Response<Boolean> isApplyVillage() {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Long id = queryUserVillage(partyId);
            if (id != null && id > 0) {
                //查询是否已经是试点村，如果是，返回TRUE
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", id);
                paras.put("type", Constant.MemberType.MANAGER.name());
                List<AppGroupMember> members = groupManager.queryAppMemberByParas(paras);
                if (members.size() > 0) {
                    resp.setResult(true);
                }
                if (!resp.getResult()) {//查有没有申请记录
                    paras.remove("type");
                    paras.put("groupId", id);
                    paras.put("partyId", partyId);
                    List<AppGroupApply> appGroupApplyList = groupManager.queryGroupApply(paras);
                    if (appGroupApplyList.size() > 0) {
                        resp.setResult(true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("isApplyVillage error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /***
     * 申请试点村  h5
     * @param
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.ApplyVillage", description = " 申请试点村")
    public Response<Boolean> ApplyVillage() {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Long id = queryUserVillage(partyId);
            String md5 = MD5.md5(partyId + JSON.toJSONString(id + partyId));
            if (redisUtil.isRepeat(md5, 2)) {
                resp.setFacade(FacadeEnums.ERROR_REPEAT);
                return resp;
            }
            if (id != null && id > 0) {
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", id);
                //paras.put("partyId",partyId);
                List<AppGroupApply> appGroupApplyList = groupManager.queryGroupApply(paras);
                if (appGroupApplyList.size() > 0) { //已经申请过了，不能重复申请
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400046);
                    return resp;
                } else {
                    Boolean insert = groupManager.insertApply(AppGroupApply.builder().type(Constant.GroupType.V.name()).partyId(partyId).operator(partyId).groupId(id).build());
                    resp.setResult(insert);
                }
            }
        } catch (Exception e) {
            logger.error("ApplyVillage error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    //查询用户默认的村
    private Long queryUserVillage(Long partyId) {
        Long id = 0L;
        String code = null;
        //查询用户的信息
        User user = userManager.queryUser(partyId);
        if (user != null) {
            if(StringUtils.isNotEmpty(user.getDistrict())){
                AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                if (StringUtils.isNotEmpty(area.getVillage().getCode())) {
                    code = area.getVillage().getCode();
                } 
            }
            /*AreaVo areaVo = new AreaVo();
            code = areaVo.selectCode(area);*/
        }
        //查村，如果存不存在就创建存，并将当前登录用户添加到这个村里面
        if (StringUtils.isNotEmpty(code)) {
            //根据用户的code查询群的code
            Map<String, Object> paras = new HashMap<>();
            paras.put("districtCode", code);
            paras.put("groupType", Constant.GroupType.V.name());
            paras.put("createType", Constant.CreateType.SYSTEM.name());
            List<AppGroup> appGroupList = groupManager.queryGroup(paras);
            if (appGroupList.size() > 0) {
                id = appGroupList.get(0).getId();
            }
        }
        return id;
    }


    //查询类型用户默认的村或群
    private AppGroup queryUserVillage(Long partyId, String groupType, String createType) {
        AppGroup appGroup = null;
        String code = null;
        //查询用户的信息
        User user = userManager.queryUser(partyId);
        if (user != null && StringUtils.isNotEmpty(user.getDistrict())) {
            AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
            AreaVo areaVo = new AreaVo();
            code = areaVo.selectCode(area);
        }
        //查村，如果存不存在就创建存，并将当前登录用户添加到这个村里面
        if (StringUtils.isNotEmpty(code)) {
            //根据用户的code查询群的code
            Map<String, Object> paras = new HashMap<>();
            paras.put("districtCode", code);
            paras.put("groupType", groupType);
            paras.put("createType", createType);
            List<AppGroup> appGroupList = groupManager.queryGroup(paras);
            if (appGroupList.size() > 0) {
                appGroup = appGroupList.get(0);
            }
        }
        return appGroup;
    }

    /**
     * 根据条件查询村申请记录 top
     *
     * @param oranginzeApplyReq
     * @return
     */
    @Override
    public Response<List<OrganizeApplyVo>> queryApplyVillageByParas(OranginzeApplyReq oranginzeApplyReq) {
        Response<List<OrganizeApplyVo>> resp = ResponseUtils.successResponse();
        try {
            List<OrganizeApplyVo> list = new ArrayList<>();
            Map<String, Object> paras = new HashMap<>();
            if (StringUtils.isNotEmpty(oranginzeApplyReq.getStartDay())) {
                paras.put("startDay", oranginzeApplyReq.getStartDay());
            }
            if (StringUtils.isNotEmpty(oranginzeApplyReq.getEndDay())) {
                paras.put("endDay", oranginzeApplyReq.getEndDay());
            }
            if (StringUtils.isNotEmpty(oranginzeApplyReq.getStatus())) {
                paras.put("status", oranginzeApplyReq.getStatus());
            }
            paras.put("type", Constant.GroupType.V.name());
            paras.put("startPage", oranginzeApplyReq.getStartPage());
            paras.put("pageSize", oranginzeApplyReq.getPageSize());
            List<AppGroupApply> appGroupApplyList = groupManager.queryGroupApplyByParas(paras);
            if (appGroupApplyList.size() > 0) {
                for (AppGroupApply appGroupApply : appGroupApplyList) {
                    User user = userManager.queryUser(appGroupApply.getPartyId());
                    if (user != null) {
                        String district = null;
                        AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                        AreaVo areaVo = new AreaVo();
                        if (area != null) {
                            district = areaVo.selectName(area).replace("·", "");
                        }
                        Boolean isAuth=false;
                        if(StringUtils.isNotEmpty(user.getTrueName())){
                            isAuth=true;
                        }
                        String showName=getShowName(user);
                        OrganizeApplyVo organizeApplyVo = OrganizeApplyVo.builder().isAuth(isAuth).id(appGroupApply.getId()).userName(showName).mobilePhone(user.getLoginName()).dateTime(DateUtils.formatDateTime("yyyy-MM-dd HH:mm:ss", appGroupApply.getCreateTime())).district(district).status(appGroupApply.getStatus()).remark(appGroupApply.getRemark()).auditor(appGroupApply.getAuditor()).build();
                        list.add(organizeApplyVo);
                    }
                }
            }

            resp.setResult(list);
        } catch (Exception e) {
            logger.error("queryApplyVillageByParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 根据条件查询村申请数 top
     *
     * @param oranginzeApplyReq
     * @return
     */
    @Override
    public Response<Integer> queryApplyVillageCountByParas(OranginzeApplyReq oranginzeApplyReq) {
        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = new HashMap<>();
            if (StringUtils.isNotEmpty(oranginzeApplyReq.getStartDay())) {
                paras.put("startDay", oranginzeApplyReq.getStartDay());
            }
            if (StringUtils.isNotEmpty(oranginzeApplyReq.getEndDay())) {
                paras.put("endDay", oranginzeApplyReq.getEndDay());
            }
            if (StringUtils.isNotEmpty(oranginzeApplyReq.getStatus())) {
                paras.put("status", oranginzeApplyReq.getStatus());
            }
            paras.put("type", Constant.GroupType.V.name());
            int count = groupManager.queryGroupApplyCountByParas(paras);
            resp.setResult(count);
        } catch (Exception e) {
            logger.error("queryApplyVillageCountByParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 修改申请试点村状态 top 、h5
     *
     * @param organizeApplyVo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.updateApplyVillage", description = "修改申请试点村状态")
    public Response<Boolean> updateApplyVillage(OrganizeApplyVo organizeApplyVo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Boolean update = false;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //开通村务
            if (StringUtils.isNotEmpty(organizeApplyVo.getStatus()) && organizeApplyVo.getStatus().equals(YesNo.Y.name()) && organizeApplyVo.getId() != null) {  //开通村务
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", organizeApplyVo.getId());
                paras.put("type", Constant.MemberType.MANAGER.name());
                List<AppGroupMember> members = groupManager.queryAppMemberByParas(paras);
                User user = userManager.queryUser(partyId);
                String type = null;
                if (members.size() == 0) { //判断村是否有管理员，保证一个村只有一个管理员
                    type = Constant.MemberType.MANAGER.name();
                } else {
                    type = Constant.MemberType.MEMBER.name();
                }
                //查询该用户是否在村务里面
                paras.remove("type");
                paras.put("partyId", partyId);
                List<AppGroupMember> memberList = groupManager.queryAppMemberByParas(paras);
                if (memberList.size() == 0) {
                    groupManager.insertGroupMeber(AppGroupMember.builder().type(type).tempName(user.getTrueName()).tempMobilePhone(user.getLoginName()).operator(partyId).partyId(partyId).groupId(organizeApplyVo.getId()).build());
                } else {
                    groupManager.updateGroupMember(AppGroupMember.builder().type(type).id(memberList.get(0).getId()).build());
                }
                AppGroupApply appGroupApply = AppGroupApply.builder().groupId(organizeApplyVo.getId()).type(Constant.GroupType.V.name()).partyId(partyId).build();
                groupManager.insertApply(appGroupApply);
                groupManager.updateGroupApply(AppGroupApply.builder().id(appGroupApply.getId()).status("Y").build());
                update = true;
            }
            //开通意见调查
            if (StringUtils.isNotEmpty(organizeApplyVo.getOpinion())) {
                propExtendManager.queryAndInit(AppPropExtend.builder().relationId(partyId).ekey(MapVo.USER_OPINION).type(User.class.getSimpleName()).value(organizeApplyVo.getOpinion()).build());
                update = true;
            }
            //后台处理开通村务
            if (StringUtils.isNotEmpty(organizeApplyVo.getAuditor()) && organizeApplyVo.getId() != null) {
                update = groupManager.updateGroupApply(AppGroupApply.builder().id(organizeApplyVo.getId()).remark(organizeApplyVo.getRemark()).status(organizeApplyVo.getStatus()).auditor(organizeApplyVo.getAuditor()).opinion(organizeApplyVo.getOpinion()).build());
            }
            resp.setResult(update);
        } catch (Exception e) {
            logger.error("updateApplyVillage error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * h5 返回村开通状态和意见
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.openVillageState", description = "返回村开通状态和意见")
    public Response<OrganizeApplyVo> openVillageState() {
        Response<OrganizeApplyVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            OrganizeApplyVo organizeApplyVo = new OrganizeApplyVo();
            Map<String, Object> map = new HashMap<>();
            map.put("relationId", partyId);
            map.put("ekey", MapVo.USER_OPINION);
            map.put("type", User.class.getSimpleName());
            List<AppPropExtend> appPropExtends = propExtendManager.queryChannel(map);
            if (appPropExtends.size() > 0) {
                organizeApplyVo.setOpinion(appPropExtends.get(0).getValue());
            }
            Long id = queryUserVillage(partyId);
            if (id != null && id > 0) {
                organizeApplyVo.setId(id);
                organizeApplyVo.setStatus("N");
                //查询是否已经是试点村，如果是，返回TRUE
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", id);
                paras.put("type", Constant.MemberType.MANAGER.name());
                List<AppGroupMember> members = groupManager.queryAppMemberByParas(paras);
                if (members.size() > 0) {
                    organizeApplyVo.setStatus("Y");
                    //将所有申请状态改为已申请
                    //groupManager.updateGroupApply(AppGroupApply.builder().status("Y").id(id).build());
                }
                /*paras.remove("type");
                paras.put("groupId", id);
                List<AppGroupApply> appGroupApplyList = groupManager.queryGroupApply(paras);
                if (appGroupApplyList.size() > 0) {
                    organizeApplyVo.setStatus("Y");
                }
                if (members.size()==0 && appGroupApplyList.size() > 0) {//有申请记录，没有管理员，将第一个申请的用户变为管理员
                    paras.put("orderBy","CREATE_TIME ASC");
                    List<AppGroupMember> list = groupManager.queryMembers(paras);
                    if (list.size() > 0) {
                        paras.remove("type");
                        paras.put("partyId", list.get(0).getPartyId());
                        List<AppGroupMember> memberList = groupManager.queryMembers(paras);
                        groupManager.updateGroupMember(AppGroupMember.builder().id(memberList.get(0).getId()).type(Constant.MemberType.MANAGER.name()).build());
                    }
                }*/
            } else {
                organizeApplyVo.setRemark("N");
            }
            resp.setResult(organizeApplyVo);
        } catch (Exception e) {
            logger.error("isApplyVillage error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.OrganizeService.isVillageAndGroupManager", description = "返回是否是村和群管理员")
    public Response<VillageManagerVo> isVillageAndGroupManager() {
        Response<VillageManagerVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            VillageManagerVo villageManagerVo = new VillageManagerVo();
            villageManagerVo.setIsGroupManager(false);
            villageManagerVo.setIsVilageManager(false);
            //查是否是村的管理员
            Long id = queryUserVillage(partyId);
            villageManagerVo.setVillageId(id);
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("groupId", id);
            paras.put("type", Constant.MemberType.MANAGER.name());
            //查当前用户是不是管理员
            List<AppGroupMember> villageList = groupManager.queryMembers(paras);
            //查群成员的个数
            paras.remove("partyId");
            paras.remove("type");
            int memberCount = groupManager.queryGroupMemberCount(paras);
            if (villageList.size() > 0 && memberCount > 1) {//如果是管理员并且村群成员为1人时，返回Y
                villageManagerVo.setIsVilageManager(true);
            }
            //查是否是村群的管理员和群人员是否为1人，为1人也返回Y
            AppGroup group = queryUserVillage(partyId, Constant.GroupType.G.name(), Constant.CreateType.SYSTEM.name());
            if (group != null) {
                paras.clear();
                paras.put("groupId", group.getId());
                List<AppGroupMember> groupList = groupManager.queryMembers(paras);
                if (group != null && group.getManagePartyId().equals(partyId) && groupList.size() > 1) {
                    villageManagerVo.setIsGroupManager(true);
                }
                if (group != null) {
                    villageManagerVo.setGroupId(group.getId());
                    villageManagerVo.setRyGroupId(group.getRyGroupId());
                }
            }
            resp.setResult(villageManagerVo);
        } catch (Exception e) {
            logger.error("isVillageAndGroupManager error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

}
