/**
 *
 */
package com.xianglin.appserv.biz.shared.impl;

import com.xianglin.appserv.biz.shared.ActivityManager;
import com.xianglin.appserv.biz.shared.BusinessManager;
import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.common.dal.daointerface.*;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgPushType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.AccountNodeManagerVo;
import com.xianglin.appserv.common.service.facade.model.vo.AppPushVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.PartyAttrAccountService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.cif.common.service.facade.model.enums.SystemType;
import com.xianglin.cif.common.service.facade.resp.PartyAttrAccountResp;
import com.xianglin.cif.common.service.facade.vo.PartyAttrAccountVo;
import com.xianglin.fala.session.Session;
import com.xianglin.merchant.common.service.facade.MerchantServiceFacade;
import com.xianglin.merchant.common.service.facade.dto.MerchantPartyIdBindDTO;
import com.xianglin.merchant.common.service.facade.dto.ResponseDTO;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wanglei 2016年8月19日下午3:21:16
 */
@Service
public class UserManagerImpl implements UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserMsgDAO userMsgDAO;

    @Autowired
    private AppPushDAO appPushDAO;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private UserFeedbackDAO userFeedbackDAO;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private AppUserCreditApplyDAO appUserCreditApplyDAO;

    @Autowired
    private MerchantServiceFacade merchantServiceFacade;

    @Autowired
    private AppGroupMemberDAO appGroupMemberDAO;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private PartyAttrAccountService partyAttrAccountService;

    @Autowired
    private AppLoginServiceClient appLoginServiceClient;

    @Autowired
    private BusinessManager businessManager;

    /**
     * @see com.xianglin.appserv.biz.shared.UserManager#addUpdateUser(com.xianglin.appserv.common.dal.dataobject.User)
     */
    @Override
    public User addUpdateUser(User user) {
        //转义emoji by sonjialin
        EmojiEscapeUtil.escapeEmojiString(user);

        logger.info("add User user {}", user);
        User result = user;
        User systemUser = userDAO.selectByPartyId(user.getPartyId());
        if (systemUser != null) {
            User u = systemUser;
            if (StringUtils.isNotEmpty(user.getPassword())) {
                u.setPassword(user.getPassword());
            }
            if (StringUtils.isNotEmpty(user.getLoginName())) {
                u.setLoginName(user.getLoginName());
            }
            if (!StringUtils.equalsIgnoreCase(u.getUserType(), user.getUserType())) {
                u.setUserType(user.getUserType());
            }
            String adminAccount = SysConfigUtil.getStr(Constant.BusiVisitKey.app_ios_xl_admin_account.code);//测试账号,针对测试账号不互踢
            if (!StringUtils.equals(user.getLoginName(), adminAccount) && !StringUtils.equals(user.getDeviceId(), u.getDeviceId())) {// 用户换机登陆
                u.setDeviceId(user.getDeviceId());
                // 给前一个用户发送离线通知
                if (StringUtils.isNotEmpty(u.getSessionId())) {
                    logger.info("send offlineMessage user:{}",u);
                    try {
                        MsgVo msg = MsgVo.builder().build();
                        msg.setPartyId(user.getPartyId());
                        msg.setMsgTitle(MsgType.OFFLINE.desc);
                        msg.setMsgType(MsgType.OFFLINE.name());
                        msg.setMessage(MessageFormat.format(SysConfigUtil.getStr("offlineMessage"), DateUtils.formatDate(new Date(), DateUtils.TIME_FMT)));
                        msg.setIsDeleted(YESNO.YES.code);
                        messageManager.sendMsg(msg);
                    } catch (Exception e) {
                        logger.error("send offline user {} partyId {} msg error {}", user.getLoginName(), user.getPartyId(), e);
                    }
                }
                // 清理前一个用户session
                sessionHelper.removeSession(u.getSessionId());
            }
            u.setSessionId(user.getSessionId());
            userDAO.updateByPrimaryKeySelective(u);
            result = u;
            u.setDeviceId(user.getDeviceId());
            user = u;
        } else {
            user.setRyToken(RyUtil.getUserToken(user.getPartyId(), null, null));
            user.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));//设置默认头像
            user.setNikerName(StringUtils.substring(user.getLoginName(), 0, 3) + "***" + StringUtils.substring(user.getLoginName(), 7));
            userDAO.insert(user);
        }
        //采用异步的方式进行用户相关信息同步
        asynchUser(user);
        return result;
    }

    /**
     * 同步用户信息（异步方式）
     *
     * @param user
     */
    @Async
    void asynchUser(User user) {
        try {
            //缓存对象（兼容）
            saveNodeSession(user);
            //查询业务
            businessManager.getBusiList(user.getUserType());
            // 推送手机绑定，将该用户partyId绑定到此台手机，之前的（如果有）绑定关系解除
            appPushDAO.unBindDevice(user.getPartyId());
            appPushDAO.bindDevice(user.getDeviceId(), user.getPartyId());
            // 将绑定到该手机上的未确定消息绑定到此用户
            Integer val = userMsgDAO.updateUnBindMsg(user.getDeviceId(), user.getPartyId());
            logger.debug("bind msg device:{},party:{},val:{}", user.getDeviceId(), user.getPartyId(), val);
            userDAO.unBindDevice(user.getDeviceId(), user.getPartyId());

            appGroupMemberDAO.updateMemberPartyId(user.getLoginName(), user.getPartyId());//同步组织中未激活用户的partyId 20170927

            // 初始化资金账户
            initAccount(user.getPartyId());
            //同步电商信息
            synchMerchant(user);

            RyUtil.updateUserInfo(user.getPartyId(), user.getShowName(), user.getNikerName(), user.getHeadImg());//同步融云头像信息

            initTrueNameAndRy(user);

            //关闭发放电商奖励 20180523
//            rewardEshop(user);
        } catch (Exception e) {
            logger.warn("asynchUser ", e);
        }
    }

    /**
     * 发放电商注册奖励
     */
    private void rewardEshop(User user) {
        try {
            //只针对第一次用户发送奖励
            logger.info("begin to rewardEshop user:{}", user);
            if (user.getCreateTime() != null) {
                return;
            }
            Map<String, String> param = new HashMap<>();
            String app_key = "xianglin".concat(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO)).concat("@#_$&");
            param.put("party_id", "" + user.getPartyId());
            app_key = app_key.concat(SHAUtil.getSortString(param));
            param.put("app_key", app_key);
            param.put("sign", SHAUtil.shaEncode(MD5.encode(app_key)));
            String json = HttpUtils.executePost(SysConfigUtil.getStr("ESHOP_REWARD_URL"), param, 2000);
            logger.info("rewardEshop:{}", json);
        } catch (Exception e) {
            logger.warn("rewardEshop", e);
        }
    }

    /**
     * 异步保存的session信息
     *
     * @param u
     */
    private void saveNodeSession(User u) {
        Session session = sessionHelper.getSession();
        com.xianglin.xlnodecore.common.service.facade.vo.NodeVo node = appLoginServiceClient.queryNodeInfoByNodeManagerPartyId(u.getPartyId());
        //兼容
        if (node != null) {
            session.setAttribute(SessionConstants.NODE_CODE, node.getNodeCode());

            session.setAttribute(SessionConstants.DISTRICT_CODE, node.getDistrictCode());
            session.setAttribute(SessionConstants.node_party_id, node.getNodePartyId());
            session.setAttribute(SessionConstants.NODE_INFO, node);
            session.setAttribute(SessionConstants.NODE_MANAGER_INFO, node);
        }
        session.setAttribute(SessionConstants.USER_TYPE, u.getUserType());//兼容

        //兼容
        AccountNodeManagerVo managerVo = new AccountNodeManagerVo();
        managerVo.setUserRole(u.getUserType());
        managerVo.setPartyId(u.getPartyId());
        managerVo.setTrueName(u.getTrueName());
        managerVo.setMobilePhone(u.getLoginName());
        session.setAttribute(SessionConstants.XL_QY_USER, managerVo);
        sessionHelper.saveSession(session);
    }


    /**
     * 初始化资金账户
     *
     * @param partyId
     */
    private void initAccount(Long partyId) {

        try {
            PartyAttrAccountResp cifResp = partyAttrAccountService.getPartyAttrAccount(partyId);
            logger.info("query getPartyAttrAccount partyID:{},resp:{}", partyId, ToStringBuilder.reflectionToString(cifResp));
            if (cifResp.getPartyAttrAccountVo() == null) {
                PartyAttrAccountVo vo = new PartyAttrAccountVo();
                vo.setPartyId(partyId);
                vo.setAppId("app");
                vo.setCreator("" + partyId);
                partyAttrAccountService.addPartyAttrAccount(vo);
            }
        } catch (Exception e) {
            logger.warn("initAccount", e);
        }
    }

    /**
     * 创建一个新手奖励
     *
     * @param user
     * @return
     */
    private void createNewGift(User user) {

        try {
            AppActivityTask task = new AppActivityTask();
            task.setPartyId(user.getPartyId());
            task.setMobilePhone(user.getLoginName());
            task.setDeviceId(user.getDeviceId());
            task.setDaily(DateFormatUtils.format(new Date(), "yyyyMMdd"));
            task.setActivityCode(Constant.ActivityType.NEW_GIFT.code);
            task.setTaskCode(Constant.ActivityTaskType.NEWgIFT.name());
            task.setTaskDailyId(task.getPartyId() + task.getActivityCode() + task.getDaily() + "1");
            task.setTaskName(Constant.ActivityTaskType.NEWgIFT.desc);
            task.setTaskStatus(YESNO.YES.code);
            task.setUseStatus(YESNO.NO.code);
            activityManager.saveUpdateActivityTask(task);
        } catch (Exception e) {
            logger.warn("createNewGift", e);
        }
    }

    @Override
    public int deleteUser(Long id) {

        return userDAO.deleteByPrimaryKey(id);
    }

    /**
     * 同步用户真实姓名和融云信息
     *
     * @param user
     * @return
     */
    private User initTrueNameAndRy(User user) {
        try {
            if (user.getId() == null) {//仅针对非新用户
                return null;
            }
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp2 = customersInfoService.selectCustomsAlready2Auth(user.getPartyId());
            logger.info("initTrueNameAndRy selectCustomsAlready2Auth resp:{} ", ToStringBuilder.reflectionToString(resp2));
            if (resp2.getResult() != null) {
                if (StringUtils.isNotEmpty(resp2.getResult().getAuthLevel()) && StringUtils.isNotEmpty(resp2.getResult().getCustomerName())) {
                    user.setTrueName(resp2.getResult().getCustomerName());
                } else {
                    user.setTrueName("");
                }
            }
            user.setRyToken(RyUtil.getUserToken(user.getPartyId(), null, null));
            userDAO.updateByPrimaryKeySelective(User.builder().id(user.getId()).trueName(user.getTrueName()).ryToken(user.getRyToken()).build());
        } catch (Exception e) {
            logger.warn("initTrueNameAndRy", e);
        }
        return user;
    }

    /**
     * 同步用户和乡邻账房关系
     *
     * @param user
     */
    private void synchMerchant(User user) {
        try {
            MerchantPartyIdBindDTO dto = new MerchantPartyIdBindDTO();
            dto.setPartyId(user.getPartyId() + "");
            dto.setPhone(user.getLoginName());
            ResponseDTO<Object> resp = merchantServiceFacade.bindMerchantPartyId(dto);
            logger.info("bind merchant resp {}", ToStringBuilder.reflectionToString(resp));
        } catch (Exception e) {
            logger.warn("bind merchant error ", e);
        }
    }

    /**
     * @see com.xianglin.appserv.biz.shared.UserManager#logout(com.xianglin.appserv.common.dal.dataobject.User)
     */
    @Override
    public void logout(User user) {
        User systemUser = userDAO.selectByPartyId(user.getPartyId());
        if (systemUser != null) {
            User u = systemUser;
//			sessionHelper.removeSession(u.getSessionId());
            u.setDeviceId(user.getDeviceId());
            u.setSessionId("");
            userDAO.updateByPrimaryKeySelective(u);
        }
        // 解除用户和设备的绑定关系
        appPushDAO.unBindDevice(user.getPartyId());
    }

    /**
     * @throws Exception
     * @see com.xianglin.appserv.biz.shared.UserManager#saveUpdatePush(com.xianglin.appserv.common.service.facade.model.vo.AppPushVo)
     */
    @Override
    public void saveUpdatePush(AppPushVo push) throws Exception {

        AppPush ap = DTOUtils.map(push, AppPush.class);
        if (StringUtils.isEmpty(ap.getSystemType())) {
            if (StringUtils.equals(push.getPushType(), MsgPushType.JPUSH.name())) {
                ap.setSystemType(SystemType.ANDROID.name().toUpperCase());
            } else {
                ap.setSystemType(SystemType.iOS.name().toUpperCase());
            }
        }

//        if (StringUtils.isEmpty(ap.getPushToken())) {
//            ap.setPushToken("");
//        }
        int result = appPushDAO.updateByDeviceId(ap);

//        if (result == 0) {
//            result = appPushDAO.updateBandIOS8(ap.getDeviceId(), ap.getPushToken());
//        }
//        // 针对ios8系统优化
//        if (result == 0) {
//            if (StringUtils.isEmpty(ap.getPushType())) {
//                ap.setPushType(MsgPushType.JPUSH.name());
//                ap.setPushToken(push.getDeviceId());
//            }
//            appPushDAO.insert(ap);
//        }
    }

    @Override
    public User getUserByLoginAccount(String loginAccount) {

        User user = userDAO.selectByMobilePhone(loginAccount);

        //反转义emoji by songjialin
        EmojiEscapeUtil.deEscapeEmojiString(user);
        return user;
    }

    @Override
    public List<User> getUsersByParam(Map<String, Object> map) {

        List<User> list = new ArrayList<>();
        try {
            list = userDAO.query(map);
        } catch (Exception e) {
            logger.error("getUsersByParam ", e);
        }

        //反转义emoji by songjialin
        list = list.stream()
                .map(input -> {
                    EmojiEscapeUtil.deEscapeEmojiString(input);
                    return input;
                })
                .collect(Collectors.toList());

        return list;
    }

    @Override
    public int updateUser(User user) {

        //转义emoji by songjialin
        EmojiEscapeUtil.escapeEmojiString(user);
        return userDAO.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据partyId查询用户
     *
     * @param partyId
     * @return
     */
    @Override
    public User queryUser(Long partyId) {

        User user = userDAO.selectByPartyId(partyId);
        //反转义emoji by songjialin
        EmojiEscapeUtil.deEscapeEmojiString(user);
        return user;
    }

    @Override
    public boolean addUserFeedback(UserFeedback feedback) {

        return userFeedbackDAO.insert(feedback) == 1;
    }

    /**
     * 查询反馈数
     *
     * @param paras
     * @return
     */
    @Override
    public int queryFeddbackCount(Map<String, Object> paras) {

        return userFeedbackDAO.selectCount(paras);
    }

    @Override
    public Integer queryMsgCount(Map<String, Object> paras) {

        return userMsgDAO.queryCount(paras);
    }

    @Override
    public Boolean addUserCreaditApply(AppUserCreditApply appUserCreaditApply) {

        return appUserCreditApplyDAO.insert(appUserCreaditApply) == 1;
    }

    @Override
    public Integer updateUserMsg(Map<String, Object> paras) {

        return userMsgDAO.batchDelUserMsg(paras);
    }

    @Override
    public List<UserFeedback> selectList(Map<String, Object> paras) {

        return userFeedbackDAO.selectList(paras);
    }

    @Override
    public Boolean updateUserFeedback(UserFeedback map) {

        return userFeedbackDAO.updateByPrimaryKeySelective(map) == 1;
    }

    @Override
    public UserFeedback queryFeedback(Long id) {

        return userFeedbackDAO.selectByPrimaryKey(id);
    }

    @Override
    public Map queryAnnualReport(Long partyId) {

        return userDAO.queryAnnualReport(partyId);
    }

    @Override
    public boolean queryAndSynRealName(Long partyId) {
        boolean result = false;
        User user = userDAO.selectByPartyId(partyId);
        if (StringUtils.isNotEmpty(user.getTrueName())) {
            result = true;
        } else {
            initTrueNameAndRy(user);
            result = StringUtils.isNotEmpty(user.getTrueName());
            if (result) {
                userDAO.updateByPrimaryKeySelective(user);
            }
        }
        return result;
    }

    @Override
    public User channelRegister(User userInfo) throws BusiException {
        if (StringUtils.isEmpty(userInfo.getLoginName()) || userInfo.getPartyId() == null) {
            throw new BusiException(FacadeEnums.E_C_USER_INVALID);
        }
        User user = userDAO.selectByPartyId(userInfo.getPartyId());
        if (user != null) {
            user.setLoginName(userInfo.getLoginName());
            user.setNikerName(userInfo.getNikerName());
            user.setHeadImg(userInfo.getHeadImg());
            userDAO.updateByPrimaryKeySelective(user);
        } else {
            userInfo.setRyToken(RyUtil.getUserToken(userInfo.getPartyId(), userInfo.getNikerName(), userInfo.getHeadImg()));
            userDAO.insert(userInfo);
        }
        RyUtil.updateUserInfo(userInfo.getPartyId(), userInfo.getNikerName(), userInfo.getNikerName(), userInfo.getHeadImg());
        return userInfo;
    }

    @Override
    public List<User> selectNewArticleUser(Map<String, Object> param) {
        List<User> users = userDAO.selectNewArticleUser(param);
        return users;
    }

    @Override
    public List<User> selectUser(List<Long> partyIds) {
        return userDAO.selectUser(partyIds);
    }

//    public static void main(String[] args) throws Exception{
//        Map<String, String> param = new HashMap<>();
//        String app_key = "xianglin".concat(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO)).concat("@#_$&");
//        param.put("party_id", "1000000000002710");
//        app_key = app_key.concat(SHAUtil.getSortString(param));
//        param.put("app_key", app_key);
//        param.put("sign", SHAUtil.shaEncode(MD5.encode(app_key)));
//        String json = HttpUtils.executePost("https://mai-dev2.xianglin.cn/index.php/wap/lottery-registerSendCoupons.html", param, 2000);
//        logger.info("reward:{}", json);
//    }
}
