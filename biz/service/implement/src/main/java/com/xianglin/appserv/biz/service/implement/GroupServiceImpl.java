/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.GroupManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppUserRelationMapper;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.GroupService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.AreaVo;
import com.xianglin.appserv.common.service.facade.model.vo.GroupVo;
import com.xianglin.appserv.common.service.facade.model.vo.MemberVo;
import com.xianglin.appserv.common.util.*;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;


/**
 * 群组接口实现
 */
@Service
@ServiceInterface
public class GroupServiceImpl implements GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private static final int version353 = 353;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 创建群可拉入群用户查询
     * 合并组织和联系人
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.createMembers", description = "创建群可拉入群用户查询")
    public Response<List<MemberVo>> createMembers(long id) {
        Response<List<MemberVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            List<MemberVo> list = new ArrayList<>();
            //查当前用用户的关注中的成员列表和群成员列表
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            if (id > 0) {
                paras.put("groupId", id);
            }
            List<AppGroupMember> appGroupMembers = groupManager.queryFollowAndQueryGroup(paras);
            if (appGroupMembers.size() > 0) {
                for (AppGroupMember appGroupMember : appGroupMembers) {
                    MemberVo memberVo = new MemberVo();
                    memberVo.setPartyId(appGroupMember.getPartyId());
                    memberVo.setMobilePhone(appGroupMember.getTempMobilePhone());
                    memberVo.setName(appGroupMember.getTempName());
                    User user = userManager.getUserByLoginAccount(appGroupMember.getTempMobilePhone());
                    if (user != null) {
                        if (user != null) {
                            //memberVo.setName(getUserName(user.getPartyId(),user.getNikerName(),user.getTrueName()));
                            if (user.getHeadImg() != null) {
                                memberVo.setImageUrl(user.getHeadImg());
                            } else {
                                memberVo.setImageUrl(SysConfigUtil.getStr("default_user_headimg"));
                            }
                            if(StringUtils.isNotEmpty(user.getTrueName())){
                                memberVo.setIsAuth(true);
                            }
                            if(StringUtils.isNotEmpty(user.getShowName())){
                                memberVo.setShowName(user.getShowName());
                                memberVo.setName(user.getShowName());
                            }else{
                                memberVo.setShowName(getShowName(user));
                                memberVo.setName(getShowName(user));
                            }
                        }
                        memberVo.setActiveStatus("Y");
                    } else {
                        memberVo.setImageUrl(SysConfigUtil.getStr("default_user_headimg"));
                        memberVo.setActiveStatus("N");
                    }
                    list.add(memberVo);
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("createMembers ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
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
     * 创建群时本地联系人分析
     *
     * @param list
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.createNativeMembers", description = "创建群时本地联系人分析")
    public Response<List<MemberVo>> createNativeMembers(List<MemberVo> list) {
        Response<List<MemberVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            if (list.size() > 0) {
                for (MemberVo memberVo : list) {
                    Map<String, Object> paras = new HashMap<>();
                    String phone = memberVo.getMobilePhone().replace("-", "").replace("+", "").replace(" ", "").replace("+86", "");
                    User systemUser = userManager.getUserByLoginAccount(phone);
                    if (systemUser != null) {  //是通讯好友
                        memberVo.setPartyId(systemUser.getPartyId());
                        memberVo.setActiveStatus("Y");
                        //查询我的此用户是否在我的关注列表里
                        Map<String, Object> map = new HashMap<>();
                        map.put("fromPartyId", systemUser.getPartyId());
                        map.put("toPartyId", partyId);
                        if(StringUtils.isNotEmpty(systemUser.getShowName())){
                            memberVo.setShowName(systemUser.getShowName());
                            memberVo.setName(systemUser.getShowName());
                        }else{
                            memberVo.setShowName(getShowName(systemUser));
                            memberVo.setName(getShowName(systemUser));
                        }
                    } else {   //不是通讯录好友，未关注未激活
                        memberVo.setActiveStatus("N");
                        memberVo.setFollwoStatus("N");
                    }
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("createNativeMembers ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 创建群
     *
     * @param list
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.create", description = "创建群")
    public Response<GroupVo> create(List<MemberVo> list) {
        Response<GroupVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String md5 = MD5.md5(partyId+JSON.toJSONString(list));
            if(redisUtil.isRepeat(md5,600)){
                resp.setFacade(FacadeEnums.ERROR_REPEAT);
                return resp;
            }
            User user = userManager.queryUser(partyId);
            String userName = getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName());
            StringBuffer name = new StringBuffer();
            name.append(userName);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    //User u=userManager.queryUser(list.get(i).getPartyId());
                    if (name.toString().length() < 30) {
                        if (user != null) {
                            String uName = list.get(i).getName();
                            if (StringUtils.isNotEmpty(uName)) {
                                if (!name.toString().contains(uName)) {
                                    name.append(uName);
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
            String groupName = null;
            if (name.toString().length() > 30) {
                groupName = name.toString().substring(0, 30);
            } else {
                groupName = name.toString();
            }
            
            AppGroup appGroup = AppGroup.builder().managePartyId(partyId).groupType(Constant.GroupType.G.name()).name(groupName).synName(groupName).imageUrl(SysConfigUtil.getStr("group_default_url")).createType(Constant.CreateType.PERSONAL.name()).operator(partyId).ryGroupId(UUID.randomUUID().toString()).build();
            Boolean create = groupManager.create(appGroup);
            if (create) {
                //更新群二维码 
                String qrCode=updateGoupQrCode(appGroup);
                if (RyUtil.create(partyId, appGroup.getRyGroupId(), name.toString())) {
                    RyUtil.join(partyId, appGroup.getRyGroupId(), appGroup.getName());
                    AppGroupMember appGroupMember = getAppGroupMember(appGroup.getId(), partyId, userName, user.getLoginName());
                    Boolean addMember = groupManager.insertGroupMeber(appGroupMember);
                    if (addMember) {
                        for (MemberVo memberVo : list) {
                            Map<String, Object> paras = new HashMap<>();
                            paras.put("partyId", memberVo.getPartyId());
                            AppGroupMember appGroupMember1 = getAppGroupMember(appGroup.getId(), partyId, memberVo.getName(), memberVo.getMobilePhone());
                            Boolean add = groupManager.insertGroupMeber(appGroupMember1);
                            if (add) {
                                RyUtil.join(memberVo.getPartyId(), appGroup.getRyGroupId(), memberVo.getName());
                                
                            }

                        }
                    }
                    GroupVo groupVo = GroupVo.builder().id(appGroup.getId()).name(appGroup.getName()).ryGroupId(appGroup.getRyGroupId()).groupType(appGroup.getGroupType()).createType(appGroup.getCreateType()).qrCode(qrCode).build();
                    resp.setResult(groupVo);
                }
            }
        } catch (Exception e) {
            logger.warn("create ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 创建并更新群二维码
     * @param appGroup
     * @return
     */
    private String updateGoupQrCode(AppGroup appGroup) {
        try {
            String qrCode=createGroupQrCode(appGroup.getId(),appGroup.getRyGroupId(), appGroup.getImageUrl());
            logger.info("创建群二维码:"+appGroup.getId()+"----"+qrCode);
            if(StringUtils.isNotEmpty(qrCode)){
                groupManager.updateGroup(AppGroup.builder().qrCode(qrCode).updateTime(new Date()).id(appGroup.getId()).build());
                return qrCode;
            }
        } catch (Exception e) {
            logger.warn("create GoupQrCode", e);
        }
        return null; 
    }

    /**
     * 创建群二维码
     * @param id
     * @param imageUrl
     * @return
     */
    private String createGroupQrCode(Long id,String rUId, String imageUrl) {
        String aa ="APP:GROUP:" + id+":"+rUId;
        aa = Base64.getEncoder().encodeToString(aa.getBytes());
        File file = QRUtils.createQRCode("XL:"+aa, imageUrl);
        String jsonBody = HttpUtils.httpImgUpload("https://appfile.xianglin.cn/file/upload", file);
        JSONObject jsonObj = JSON.parseObject(jsonBody);
        String qrUrl = jsonObj.getString(file.getName());
        file.delete();
        return qrUrl;
    }

    private AppGroupMember getAppGroupMember(Long id, Long partyId, String userName, String loginName) {
        AppGroupMember appGroupMember = new AppGroupMember();
        appGroupMember.setTempMobilePhone(loginName);
        appGroupMember.setTempName(userName);
        appGroupMember.setGroupId(id);
        appGroupMember.setOperator(partyId);
        return appGroupMember;
    }


    /**
     * 查询当前用户所有群
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.list", description = "查询当前用户所有群")
    public Response<List<GroupVo>> list() {
        Response<List<GroupVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            List<AppGroupMember> appGroupMembers = groupManager.queryMembers(paras);
            List<GroupVo> list = new ArrayList<>();
            String ids = null;
            if (appGroupMembers.size() > 0) {
                for (AppGroupMember appGroupMember : appGroupMembers) {
                    if (StringUtils.isNotEmpty(ids)) {
                        ids = ids + "," + appGroupMember.getGroupId();
                    } else {
                        ids = appGroupMember.getGroupId() + "";
                    }
                }
                paras.clear();
                paras.put("ids", ids);
                paras.put("groupType", Constant.GroupType.G.name());
                List<AppGroup> appGroupList = groupManager.queryGroup(paras);
                for (AppGroup appGroup : appGroupList) {
                    int count = 0;
                    //根据融云群ID查询群成员个数
                    String partyIds = RyUtil.query(appGroup.getRyGroupId());
                    if (StringUtils.isNotEmpty(partyIds)) {
                        count = partyIds.split(",").length;
                        GroupVo groupVo = GroupVo.builder().managerPartyId(appGroup.getManagePartyId()).ryGroupId(appGroup.getRyGroupId()).id(appGroup.getId()).imageUrl(appGroup.getImageUrl()).name(appGroup.getName()).isManager(isManagerPartyId(partyId, appGroup.getId())).count(count).createType(appGroup.getCreateType()).groupType(appGroup.getGroupType()).build();
                        list.add(groupVo);
                    } else {         //如果融云群成员为空就直接删除群
                        groupManager.updateGroup(AppGroup.builder().id(appGroup.getId()).isDeleted("Y").build());
                    }
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("list ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 更新群信息
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.update", description = "更新群信息")
    public Response<Boolean> update(GroupVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询群是否存在
            AppGroup appGroup = groupManager.Group(vo.getId());
            if (appGroup != null) {
                //判断当前登录用户是否是管理员
                Boolean isManager = isManagerPartyId(partyId, vo.getId());
                if (isManager) {
                    //查询群是不是系统创建
                    if(appGroup.getGroupType().equals(Constant.GroupType.G.name()) && appGroup.getCreateType().equals(Constant.CreateType.SYSTEM.name()) && StringUtils.isEmpty(vo.getImageUrl())){
                        //判断当前用户的版本号，如果低于3.5.2就toast文案提示“请升级至最新版本后操作”
                        String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                        int ver=Integer.valueOf(version.replace(".",""));
                        if(ver<version353){
                            resp.setFacade(FacadeEnums.ERROR_CHAT_400055);
                            return resp;
                        }else{
                            resp.setFacade(FacadeEnums.ERROR_CHAT_400047);
                            return resp;
                        }
                    }else {
                        if (RyUtil.refresh(appGroup.getRyGroupId(), vo.getName())) {
                            AppGroup group = new AppGroup();
                            group.setName(vo.getName());
                            group.setId(vo.getId());
                            group.setImageUrl(vo.getImageUrl());
                            //创建群二维码
                            if((StringUtils.isEmpty(appGroup.getQrCode()) || StringUtils.isNotEmpty(vo.getImageUrl())) && StringUtils.isNotEmpty(appGroup.getRyGroupId())){
                                appGroup.setImageUrl(vo.getImageUrl());
                                group.setQrCode(updateGoupQrCode(appGroup));
                            }else{
                                group.setQrCode(appGroup.getQrCode());
                            }
                            Boolean updateGroup = groupManager.updateGroup(group);
                            resp.setResult(updateGroup);
                        }
                    }
                } else {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400027);
                    return resp;
                }
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400025);
                return resp;
            }
            resp.setResult(true);
        } catch (Exception e) {
            logger.warn("create ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 添加群成员
     * 从createMembers结果中排除本群成员
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.addMembers", description = "添加群成员")
    public Response<List<MemberVo>> addMembers(long id) {
        Response<List<MemberVo>> resp = ResponseUtils.successResponse();
        try {
            List<MemberVo> list = new ArrayList<>();
            MemberVo vo = new MemberVo();
            list.add(vo);
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("addMembers ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 添加群成员时本地联系人分析
     * 从createNativeMembers结果中排除本群成员
     *
     * @param list
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.addNativeMembers", description = "批量添加群成员时")
    public Response<List<MemberVo>> addNativeMembers(List<MemberVo> list, long id) {
        Response<List<MemberVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String md5 = MD5.md5(partyId+JSON.toJSONString(list));
            if(redisUtil.isRepeat(md5,10)){
                resp.setFacade(FacadeEnums.ERROR_REPEAT);
                return resp;
            }
            //查询群是否存在
            List<MemberVo> listVo = new ArrayList<>();
            AppGroup group = groupManager.Group(id);
            if (group != null) {
                //查询群是不是系统创建
                if(group.getGroupType().equals(Constant.GroupType.G.name()) && group.getCreateType().equals(Constant.CreateType.SYSTEM.name())){
                    //判断当前用户的版本号，如果低于3.5.2就toast文案提示“请升级至最新版本后操作”
                    String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                    int ver=Integer.valueOf(version.replace(".",""));
                    if(ver<version353){
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400055);
                        return resp;
                    }else{
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400052);
                        return resp;
                    }
                }else{
                    //判断当前登录用户是否是管理员
                    if (list.size() > 0) {
                        for (MemberVo memberVo : list) {
                            if (RyUtil.join(memberVo.getPartyId(), group.getRyGroupId(), group.getName())) {
                                AppGroupMember appGroupMember = AppGroupMember.builder().partyId(memberVo.getPartyId()).tempMobilePhone(memberVo.getMobilePhone()).tempName(memberVo.getName()).groupId(id).operator(partyId).build();
                                groupManager.insertExceptMember(appGroupMember);
                                //同步群名称
                                updateGroupName(group);
                            }
                        }
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("groupId", id);
                    List<AppGroupMember> appGroupMembers = groupManager.queryMembers(map);
                    for (AppGroupMember groupMember : appGroupMembers) {
                        MemberVo memberVo = getMemberVo(groupMember);
                        if(memberVo != null){
                            listVo.add(memberVo);
                        }
                    }
                    resp.setResult(listVo);
                }
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400025);
                return resp;
            }
        } catch (Exception e) {
            logger.warn("addNativeMembers ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 返回所有群成员
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.listMembers", description = "返回所有群成员")
    public Response<List<MemberVo>> listMembers(long id) {
        Response<List<MemberVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            List<MemberVo> listM = new ArrayList<>();
            //查询群
            AppGroup appGroup = groupManager.Group(id);
            if (appGroup != null) {
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", id);
                if(!appGroup.getGroupType().equals(Constant.GroupType.V.name())){//查村的时候排降序，查群成员时升序
                    paras.put("orderBy", "CREATE_TIME asc");
                }else{
                    paras.put("orderBy", "CONVERT( type USING gbk ),Create_time desc");
                }
                if(appGroup.getGroupType().equals(Constant.GroupType.G.name())){
                    String partyIds = RyUtil.query(appGroup.getRyGroupId());
                    paras.put("partyIds", partyIds);
                }
                //根据群ID和融云得到的群成员的partyIDID查询群成员
                List<AppGroupMember> list = groupManager.queryMembers(paras);
                if (list.size() > 0) {
                    for (AppGroupMember appGroupMember : list) {
                        MemberVo memberVo = getMemberVo(appGroupMember);
                        if(memberVo != null){
                            listM.add(memberVo);
                        }
                    }
                    resp.setResult(listM);
                }
            } else {
                //群不存在
                resp.setFacade(FacadeEnums.ERROR_CHAT_400025);
                return resp;
            }

        } catch (Exception e) {
            logger.error("listMembers ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private MemberVo getMemberVo(AppGroupMember appGroupMember) {
        MemberVo memberVo = null;
        String isManager = null;
        if (appGroupMember.getPartyId() != null) {
            //查找用户的头像，如果为空就返回默认的头像
            User user = userManager.queryUser(appGroupMember.getPartyId());
            String headImg = null;
            if (user != null) {
                if (StringUtils.isNotEmpty(user.getHeadImg())) {
                    headImg = user.getHeadImg();
                } else {
                    headImg = SysConfigUtil.getStr("default_user_headimg");
                }
                if(StringUtils.isNotEmpty(appGroupMember.getType())){
                    if(appGroupMember.getType().equals(Constant.MemberType.MANAGER.name())){
                        isManager = "Y";
                    }else{
                        isManager = "N";
                    }
                }else{
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
                memberVo = MemberVo.builder().id(appGroupMember.getId()).name(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).mobilePhone(user.getLoginName()).imageUrl(headImg).partyId(appGroupMember.getPartyId()).isAuth(isAuth).groupId(appGroupMember.getGroupId()).activeStatus(appGroupMember.getStatus()).isManager(isManager).build();
                if(StringUtils.isNotEmpty(user.getShowName())){
                    memberVo.setShowName(user.getShowName());
                    memberVo.setName(user.getShowName());
                }else{
                    memberVo.setShowName(getShowName(user));
                    memberVo.setName(getShowName(user));
                }
            }else{
                //删除不存在用户，并在融云群里面删除
                AppGroup appGroup=groupManager.Group(appGroupMember.getGroupId());
                RyUtil.quit(appGroupMember.getPartyId(),appGroup.getRyGroupId());
                groupManager.deleteMember(appGroupMember.getId());
            }
        } else {
            //根据手机号查询用户
            User user = userManager.getUserByLoginAccount(appGroupMember.getTempMobilePhone());
            if (user != null) {
                String imageUrl = null;
                if (StringUtils.isNotEmpty(user.getHeadImg())) {
                    imageUrl = user.getHeadImg();
                } else {
                    imageUrl = SysConfigUtil.getStr("default_user_headimg");
                }
                if(StringUtils.isNotEmpty(appGroupMember.getType())){
                    if(appGroupMember.getType().equals(Constant.MemberType.MANAGER.name())){
                        isManager = "Y";
                    }else{
                        isManager = "N";
                    }
                }
                Boolean isAuth=false;
                if(StringUtils.isNotEmpty(user.getTrueName())){
                    isAuth=true;
                }
                memberVo = MemberVo.builder().id(appGroupMember.getId()).name(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).mobilePhone(user.getLoginName()).imageUrl(imageUrl).partyId(appGroupMember.getPartyId()).isAuth(isAuth).groupId(appGroupMember.getGroupId()).activeStatus(appGroupMember.getStatus()).isManager(isManager).build();
                if(StringUtils.isNotEmpty(user.getShowName())){
                    memberVo.setShowName(user.getShowName());
                    memberVo.setName(user.getShowName());
                }else{
                    memberVo.setShowName(getShowName(user));
                    memberVo.setName(getShowName(user));
                }
            }else{ //删除不存在用户，并在融云群里面删除
                AppGroup appGroup=groupManager.Group(appGroupMember.getGroupId());
                RyUtil.quit(appGroupMember.getPartyId(),appGroup.getRyGroupId());
                groupManager.deleteMember(appGroupMember.getId());
            }
        }
        return memberVo;
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

    /**
     * 转让群管理
     *
     * @param partyId
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.assignManager", description = "转让群管理")
    public Response<Boolean> assignManager(long partyId, long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long managerPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (managerPartyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Boolean isManager = isManagerPartyId(managerPartyId, id);
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
                resp.setFacade(FacadeEnums.ERROR_CHAT_400030);
                return resp;
            }
        } catch (Exception e) {
            logger.warn("assignManager", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 退出群
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.exit", description = "退出群")
    public Response<Boolean> exit(long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询群是否存在
            AppGroup group = groupManager.Group(id);
            if (group != null) {
                if (isManagerPartyId(partyId, id)) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400028);
                    return resp;
                } else {
                    if(group.getGroupType().equals(Constant.GroupType.G.name()) && group.getCreateType().equals(Constant.CreateType.SYSTEM.name())){
                        //判断当前用户的版本号，如果低于3.5.2就toast文案提示“请升级至最新版本后操作”
                        String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                        int ver=Integer.valueOf(version.replace(".",""));
                        if(ver<version353){
                            resp.setFacade(FacadeEnums.ERROR_CHAT_400055);
                            return resp;
                        }else{
                            resp.setFacade(FacadeEnums.ERROR_CHAT_400050);
                            return resp;
                        }
                    }else{
                        if (RyUtil.quit(partyId, group.getRyGroupId())) {
                            AppGroupMember appGroupMember = new AppGroupMember();
                            appGroupMember.setGroupId(id);
                            appGroupMember.setPartyId(partyId);
                            Map<String, Object> paras = new HashMap<>();
                            paras.put("groupId", id);
                            paras.put("partyId", partyId);
                            List<AppGroupMember> list = groupManager.queryMembers(paras);
                            if (list.size() > 0) {
                                Boolean exit = groupManager.deleteMember(list.get(0).getId());
                                //同步群名称
                                updateGroupName(group);
                                resp.setResult(exit);
                            }
                        }
                    }
                }
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400032);
                return resp;
            }
        } catch (Exception e) {
            logger.warn("exit error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 同步群名称
     * @param group
     */
    private void updateGroupName(AppGroup group) {
        if(StringUtils.equals(group.getName(),group.getSynName())){
            Map<String, Object> paras = new HashMap<>();
            //替换新的群名称
            paras.put("groupId", group.getId());
            paras.put("orderBy", "CREATE_TIME asc");
            List<AppGroupMember> members = groupManager.queryMembers(paras);
            String name = createGroupName(members);
            if (RyUtil.refresh(group.getRyGroupId(), name)){
                groupManager.updateGroup(AppGroup.builder().name(name).synName(name).id(group.getId()).build());
            }

        }
    }

    /**
     * 删除群成员
     *
     * @param memberId
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.deleteMember", description = "删除群成员")
    public Response<Boolean> deleteMember(long memberId, long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long loginPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (loginPartyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查出群是否存在
            AppGroup appGroup = groupManager.Group(id);
            if (appGroup != null) {
                if(appGroup.getGroupType().equals(Constant.GroupType.G.name()) && appGroup.getCreateType().equals(Constant.CreateType.SYSTEM.name())){
                    //判断当前用户的版本号，如果低于3.5.2就toast文案提示“请升级至最新版本后操作”
                    String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                    int ver=Integer.valueOf(version.replace(".",""));
                    if(ver<version353){
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400055);
                        return resp;
                    }else{
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400053);
                        return resp;
                    }
                }else{
                    //查询组织
                    Boolean isManager = isManagerPartyId(loginPartyId, id);
                    if (isManager) {
                        AppGroupMember appGroupMember = groupManager.groupMember(memberId);
                        if (appGroupMember != null) {
                            if (loginPartyId.equals(appGroupMember.getId())) {   //判断当前用户是否删除的是自己
                                //提示用户不能删除自己
                                resp.setFacade(FacadeEnums.ERROR_CHAT_400036);
                                return resp;
                            } else {
                                if (RyUtil.quit(memberId, appGroup.getRyGroupId())) {
                                    Boolean deleteMember = groupManager.deleteMember(memberId);
                                    resp.setResult(deleteMember);
                                    //同步群名称
                                    updateGroupName(appGroup);
                                }
                            }
                        } else {
                            resp.setFacade(FacadeEnums.ERROR_CHAT_400022);
                            return resp;
                        }
                    } else {
                        //提示用户无权删除组织成员
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400026);
                        return resp;
                    }
                }
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400025);
                return resp;
            }

        } catch (Exception e) {
            logger.warn("deleteMember ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 批量删除群成员
     *
     * @param memberIds
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.batchDeleteMember", description = "批量删除群成员")
    public Response<Boolean> batchDeleteMember(List<Long> memberIds, long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询群
            AppGroup appGroup = groupManager.Group(id);
            if (appGroup != null) {

                if(appGroup.getGroupType().equals(Constant.GroupType.G.name()) && appGroup.getCreateType().equals(Constant.CreateType.SYSTEM.name())){
                    //判断当前用户的版本号，如果低于3.5.2就toast文案提示“请升级至最新版本后操作”
                    String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                    int ver=Integer.valueOf(version.replace(".",""));
                    if(ver<version353){
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400055);
                        return resp;
                    }else{
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400053);
                        return resp;
                    }
                }else{
                    Boolean isManager = isManagerPartyId(partyId, id);
                    if (isManager) {
                        if (memberIds.size() > 0) {
                            //查询成员删除列表中是否有自己
                            Map<String, Object> paras = new HashMap<>();
                            paras.put("groupId", id);
                            paras.put("partyId", partyId);
                            List<AppGroupMember> appGroupMembers = groupManager.queryMembers(paras);
                            if (appGroupMembers != null) {
                                AppGroupMember groupMember = appGroupMembers.get(0);
                                if (memberIds.contains(groupMember.getId())) {
                                    //提示不能删除自己
                                    resp.setFacade(FacadeEnums.ERROR_CHAT_400036);
                                    return resp;
                                } else {
                                    for (Long memberId : memberIds) {
                                        AppGroupMember appGroupMember = groupManager.groupMember(memberId);
                                        if (RyUtil.quit(appGroupMember.getPartyId(), appGroup.getRyGroupId())) {
                                            resp.setResult(groupManager.updateGroupMember(AppGroupMember.builder().id(memberId).isDeleted("Y").build()));
                                            //同步群名称
                                            updateGroupName(appGroup);
                                        }
                                    }
                                }
                            }

                        }
                    } else {
                        //提示用户无权删除组织成员
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400033);
                        return resp;
                    }
                }
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400032);//群不存在
                return resp;
            }

        } catch (Exception e) {
            logger.warn("batchDeleteMember ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查询群信息
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.queryGroup", description = "查询群信息")
    public Response<GroupVo> queryGroup(long id) {
        Response<GroupVo> resp = ResponseUtils.successResponse();
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        if (partyId == null) {
            resp.setResonpse(ResponseEnum.SESSION_INVALD);
            return resp;
        }
        GroupVo groupVo = null;
        Boolean isExist = false;
        AppGroup appGroup = groupManager.Group(id);
        List<MemberVo> memberVos = new ArrayList<>();
        if (appGroup != null) {
            int count = 0;
            //查询融云群成员
            String partyIds = RyUtil.query(appGroup.getRyGroupId());
            if (partyIds != null) {
                count = partyIds.split(",").length;
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", id);
                paras.put("partyIds", partyIds);
                paras.put("orderBy", "CREATE_TIME asc");
                List<AppGroupMember> list = groupManager.queryMembers(paras);
                if (list.size() > 0 && list.size() > 5) {
                    for (int i = 0; i < 5; i++) {
                        MemberVo memberVo = getMemberVo(list.get(i));
                        if(memberVo != null){
                            memberVos.add(memberVo);
                        }
                        if(memberVo.getPartyId().equals(partyId)){
                            isExist =true;
                        }
                    }
                } else if (list.size() > 0 && list.size() < 0) {
                    for (AppGroupMember appGroupMember : list) {
                        MemberVo memberVo = getMemberVo(appGroupMember);
                        if(memberVo != null){
                            memberVos.add(memberVo);
                        }
                        if(memberVo.getPartyId().equals(partyId)){
                            isExist = true;
                        }
                    }
                }
            }
            
            groupVo = GroupVo.builder().imageUrl(appGroup.getImageUrl()).isExist(isExist).id(appGroup.getId()).name(appGroup.getName()).ryGroupId(appGroup.getRyGroupId()).qrCode(appGroup.getQrCode()).count(count).managerPartyId(appGroup.getManagePartyId()).members(memberVos).groupType(appGroup.getGroupType()).createType(appGroup.getCreateType()).build();
            //创建群二维码
            if(StringUtils.isEmpty(appGroup.getQrCode()) && StringUtils.isNotEmpty(appGroup.getRyGroupId())){
                groupVo.setQrCode(updateGoupQrCode(appGroup));
            }else{
                groupVo.setQrCode(appGroup.getQrCode());
            }
        }
        resp.setResult(groupVo);
        return resp;
    }

    /**
     * 根据融云群ID查询群信息
     *
     * @param rUId
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.queryGroupByRUId", description = "根据融云群ID查询群信息")
    public Response<GroupVo> queryGroupByRUId(String rUId) {
        Response<GroupVo> resp = ResponseUtils.successResponse();
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        if (partyId == null) {
            resp.setResonpse(ResponseEnum.SESSION_INVALD);
            return resp;
        }
        GroupVo groupVo = null;
        Boolean isExist = false;
        Map<String, Object> paras = new HashMap<>();
        paras.put("ryGroupId", rUId);
        List<AppGroup> appGroupList = groupManager.queryGroup(paras);
        List<MemberVo> memberVos = new ArrayList<>();
        if(appGroupList.size() == 0){
            resp.setFacade(FacadeEnums.ERROR_CHAT_400025);
            return resp; 
        }
        int count = 0;
        //查询融云群成员
        String partyIds = RyUtil.query(appGroupList.get(0).getRyGroupId());
        if (partyIds != null) {
            count = partyIds.split(",").length;
            paras.remove("ryGroupId");
            paras.put("groupId", appGroupList.get(0).getId());
            paras.put("partyIds", partyIds);
            paras.put("orderBy", "CREATE_TIME asc");
            List<AppGroupMember> list = groupManager.queryMembers(paras);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    MemberVo memberVo = getMemberVo(list.get(i));
                    if(memberVo != null){
                        //如果是村群，同步村群人员，如果地址不是这个村，让用户退出这个村群
                        if(appGroupList.get(0).getGroupType().equals(Constant.GroupType.G.name()) && appGroupList.get(0).getCreateType().equals(Constant.CreateType.SYSTEM.name())){
                            User user = userManager.queryUser(memberVo.getPartyId());
                            if(user != null){
                                String userDistrictCode=null;
                                AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                                if (StringUtils.isNotEmpty(area.getVillage().getCode())) {
                                    userDistrictCode = area.getVillage().getCode();
                                }
                                if(appGroupList.get(0).getDistrictCode().equals(userDistrictCode)){
                                    memberVos.add(memberVo);
                                }else{   //如果用户的地址和群地址不相同将用户退出群
                                    if (RyUtil.quit(memberVo.getPartyId(), appGroupList.get(0).getRyGroupId())) {
                                        groupManager.updateGroupMember(AppGroupMember.builder().id(memberVo.getId()).isDeleted("Y").build());
                                    }
                                }
                            }
                        }else {
                            memberVos.add(memberVo);
                        }
                        if(memberVo.getPartyId().equals(partyId)){
                            isExist = true;
                        }
                    }
                }
            }
        }
        
        groupVo = GroupVo.builder().imageUrl(appGroupList.get(0).getImageUrl()).isExist(isExist).id(appGroupList.get(0).getId()).name(appGroupList.get(0).getName()).ryGroupId(appGroupList.get(0).getRyGroupId()).count(count).managerPartyId(appGroupList.get(0).getManagePartyId()).members(memberVos).createType(appGroupList.get(0).getCreateType()).groupType(appGroupList.get(0).getGroupType()).build();
        //创建群二维码
        if(StringUtils.isEmpty(appGroupList.get(0).getQrCode())){
            groupVo.setQrCode(updateGoupQrCode(appGroupList.get(0)));
        }else{
            groupVo.setQrCode(appGroupList.get(0).getQrCode());
        }
        resp.setResult(groupVo);
        return resp;
    }


    /**
     * 根据群的ID解散群
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.dismiss", description = "解散群")
    public Response<Boolean> dismiss(long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long loginPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (loginPartyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查出群是否存在
            AppGroup appGroup = groupManager.Group(id);
            if (appGroup != null) {
                if(appGroup.getGroupType().equals(Constant.GroupType.G.name()) && appGroup.getCreateType().equals(Constant.CreateType.SYSTEM.name())){
                    //判断当前用户的版本号，如果低于3.5.2就toast文案提示“请升级至最新版本后操作”
                    String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                    int ver=Integer.valueOf(version.replace(".",""));
                    if(ver<version353){
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400055);
                        return resp;
                    }else{
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400051);
                        return resp;
                    }
                }else{
                    Boolean isManager = isManagerPartyId(loginPartyId, id); //判断是否是群主
                    if (isManager) {
                        if (RyUtil.dismiss(loginPartyId, appGroup.getRyGroupId())) {
                            Boolean dismiss = groupManager.updateGroup(AppGroup.builder().id(id).isDeleted("Y").build());
                            resp.setResult(dismiss);
                        }
                    } else {
                        //提示用户无权解散群
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400034);
                        return resp;
                    }
                }
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400025);
                return resp;
            }
        } catch (Exception e) {
            logger.warn("deleteMember ", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.isGroupManager", description = "查询当前用户是否是管理员")
    public Response<Boolean> isGroupManager(long id) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        if (partyId == null) {
            resp.setResonpse(ResponseEnum.SESSION_INVALD);
            return resp;
        }
        AppGroup appGroup =groupManager.Group(id);
        if(appGroup == null){
            resp.setFacade(FacadeEnums.ERROR_CHAT_400025);
            return resp;
        }
        if(appGroup.getGroupType().equals(Constant.GroupType.V.name()) && appGroup.getCreateType().equals(Constant.CreateType.SYSTEM.name())){//为系统村根据成员表里面的type字段判断是否是管理员
            Map<String, Object> paras= new HashMap<>();
            paras.put("groupId",id);
            paras.put("partyId",partyId);
            paras.put("type",Constant.MemberType.MANAGER.name());
            List<AppGroupMember> list=groupManager.queryMembers(paras);
            if(list.size()>0){
                resp.setResult(true);
            }
        }else{
            if(isManagerPartyId(partyId,appGroup.getId())){
                resp.setResult(true);
            }
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GroupService.joinGroup", description = "当前用户加入群")
    public Response<Boolean> joinGroup(long id) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String md5 = MD5.md5(partyId+JSON.toJSONString(id));
        if(redisUtil.isRepeat(md5,2)){
            resp.setFacade(FacadeEnums.ERROR_REPEAT);
            return resp;
        }
        User user = userManager.queryUser(partyId);
        String userName = getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName());
        //查询群是否存在
        AppGroup group = groupManager.Group(id);
        if(group == null){  //群不存在
            resp.setFacade(FacadeEnums.ERROR_CHAT_400025);
            return resp;
        }
        if (RyUtil.join(partyId, group.getRyGroupId(), group.getName())) {
            AppGroupMember appGroupMember = AppGroupMember.builder().partyId(partyId).tempMobilePhone(user.getLoginName()).tempName(userName).groupId(id).operator(partyId).build();
            groupManager.insertExceptMember(appGroupMember);
            //同步群名称
            updateGroupName(group);
            resp.setResult(true);
        }
        return resp;
    }

    private Boolean isManagerPartyId(Long partyId, long groupId) {
        Boolean isManage = false;
        AppGroup appGroup = groupManager.Group(groupId);
        if (appGroup != null) {
            if (partyId.equals(appGroup.getManagePartyId())) {
                isManage = true;
            }
        }
        return isManage;
    }

    /**
     * 创建默认的群名称
     * @param list
     * @return
     */
    private String createGroupName(List<AppGroupMember> list){
        StringBuffer name = new StringBuffer();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (name.toString().length() < 30) {
                    String uName = list.get(i).getTempName();
                    if (StringUtils.isNotEmpty(uName)) {
                        name.append(uName);
                    }
                } else {
                    break;
                }
            }
        }
        String groupName = null;
        if (name.toString().length() > 30) {
            groupName = name.toString().substring(0, 30);
        } else {
            groupName = name.toString();
        }
        return groupName;
    }
}
