package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Maps;
import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.UserGenealogyManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.constant.GenealogyLinkStatus;
import com.xianglin.appserv.common.dal.constant.GenealogyLinkType;
import com.xianglin.appserv.common.dal.dataobject.AppUserGenealogy;
import com.xianglin.appserv.common.dal.dataobject.AppUserGenerlogyLinkDO;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.app.UserGenealogyService;
import com.xianglin.appserv.common.service.facade.model.GenealogyLinkDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.GenealogyLinkVO;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.UserGenealogyVo;
import com.xianglin.appserv.common.service.facade.model.vo.WechatShareInfo;
import com.xianglin.appserv.common.util.*;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.xlStation.base.enums.XLStationEnums;
import com.xianglin.xlStation.base.model.SmsResponse;
import com.xianglin.xlStation.common.service.facade.MessageService;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Describe :
 * Created by xingyali on 2017/11/9 11:15.
 * Update reason :
 */
@Service("userGenealogyService")
@ServiceInterface
public class UserGenealogyServiceImpl implements UserGenealogyService {

    public static final String LOCK_PREFIX = "内链添加家谱成员";

    public static final String SHARE_TITLE = "@%s 邀请你加入Ta的家谱";

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    MessageService messageService;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private UserGenealogyManager userGenealogyManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MessageManager messageManager;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.query", description = "查询家谱个人详情")
    public Response<UserGenealogyVo> query(Long id) {

        Response<UserGenealogyVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            UserGenealogyVo vo = null;
            if (id != null) {
                AppUserGenealogy appUserGenealogy = userGenealogyManager.query(id);
                vo = DTOUtils.map(appUserGenealogy, UserGenealogyVo.class);
            }
            resp.setResult(vo);
        } catch (Exception e) {
            logger.warn("query error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.update", description = "修改家谱成员信息")
    public Response<Boolean> update(UserGenealogyVo userGenealogyVo) {

        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            AppUserGenealogy appUserGenealogy = DTOUtils.map(userGenealogyVo, AppUserGenealogy.class);
            AppUserGenealogy userGenealogy = userGenealogyManager.query(appUserGenealogy.getId());
            if (userGenealogy != null) {
                Boolean updateUserGenealogy = userGenealogyManager.update(appUserGenealogy);
                resp.setResult(updateUserGenealogy);
            }
        } catch (Exception e) {
            logger.warn("update error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.add", description = "添加家谱成员")
    public Response<Boolean> add(UserGenealogyVo userGenealogyVo) {

        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String md5 = MD5.md5(partyId + JSON.toJSONString(userGenealogyVo));
            if (redisUtil.isRepeat(md5, 30)) {
                resp.setCode(300006); //要插入的数据已存在
                resp.setFacade(FacadeEnums.INSERT_DUPLICATE);
                return resp;
            }
            AppUserGenealogy appUserGenealogy = userGenealogyManager.query(userGenealogyVo.getId());
            if (appUserGenealogy != null) {
                AppUserGenealogy userGenealogy = DTOUtils.map(userGenealogyVo, AppUserGenealogy.class);
                userGenealogy.setCreator(partyId);
                if (userGenealogyVo.getParentId() != null && userGenealogyVo.getParentId().equals(1L)) { //子辈
                    userGenealogy.setId(null);
                    userGenealogy.setParentId(appUserGenealogy.getId());
                    Boolean insertUserGenealogy = userGenealogyManager.insert(userGenealogy);
                    resp.setResult(insertUserGenealogy);
                } else if (userGenealogyVo.getParentId() != null && userGenealogyVo.getParentId().equals(0L)) {  //同辈
                    //判断用户是否有父辈
                    if (appUserGenealogy.getParentId().equals(0L)) {     //没有父辈，需要创建默认的父辈
                        AppUserGenealogy genealogy = AppUserGenealogy.builder().gender("男").parentId(0L).name("无姓名").creator(partyId).build();
                        userGenealogyManager.insert(genealogy);//创建默认的父辈
                        userGenealogy.setParentId(genealogy.getId());
                        userGenealogy.setId(null);
                        Boolean flag = userGenealogyManager.insert(userGenealogy); //创建同辈
                        //将parentId改为新增的用户的ID，除新增的父辈
                        Map<String, Object> map = new HashMap<>();
                        map.put("parentId", 0L);
                        map.put("creator", partyId);
                        List<AppUserGenealogy> userGenealogies = userGenealogyManager.queryUserGenealogyParas(map);
                        if (userGenealogies.size() > 0) {
                            for (AppUserGenealogy userGenealogy1 : userGenealogies) {
                                if (!userGenealogy1.getId().equals(genealogy.getId())) {
                                    userGenealogyManager.update(AppUserGenealogy.builder().parentId(genealogy.getId()).id(userGenealogy1.getId()).build());
                                }
                            }

                        }
                        resp.setResult(flag);
                    } else {
                        userGenealogy.setParentId(appUserGenealogy.getParentId());
                        userGenealogy.setId(null);
                        Boolean flag = userGenealogyManager.insert(userGenealogy);
                        resp.setResult(flag);
                    }
                } else if (userGenealogyVo.getParentId() != null && userGenealogyVo.getParentId().equals(2L)) { //添加父辈
                    if (appUserGenealogy.getParentId() > 0) { //已有父辈
                        resp.setCode(400043);
                        resp.setResult(false);
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400043);
                        return resp;
                    } else {   //只有parentId为0时才能添加父辈
                        userGenealogy.setParentId(0L);
                        userGenealogy.setId(null);
                        Boolean insertUserGenealogy = userGenealogyManager.insert(userGenealogy);
                        //将用户的同辈查出来，将同辈的parentId改为新增的用户的ID
                        Map<String, Object> map = new HashMap<>();
                        map.put("parentId", 0L);
                        map.put("creator", partyId);
                        List<AppUserGenealogy> userGenealogies = userGenealogyManager.queryUserGenealogyParas(map);
                        if (userGenealogies.size() > 0) {
                            for (AppUserGenealogy genealogy : userGenealogies) {
                                if (!genealogy.getId().equals(userGenealogy.getId())) {
                                    userGenealogyManager.update(AppUserGenealogy.builder().parentId(userGenealogy.getId()).id(genealogy.getId()).build());
                                }
                            }

                        }
                        resp.setResult(insertUserGenealogy);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("add error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.queryUserGenealogyVo", description = "查询我的家谱")
    public Response<List<UserGenealogyVo>> queryUserGenealogyVo() {

        Response<List<UserGenealogyVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            List<UserGenealogyVo> userGenealogyVos = getUserGenealogyVos(partyId);
            resp.setResult(userGenealogyVos);
        } catch (Exception e) {
            logger.warn("queryUserGenealogyVo error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    public Response<List<UserGenealogyVo>> queryUserGenealogyVoByParytId(Long partyId) {

        Response<List<UserGenealogyVo>> resp = ResponseUtils.successResponse();
        try {
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            List<UserGenealogyVo> userGenealogyVos = getUserGenealogyVos(partyId);
            resp.setResult(userGenealogyVos);
        } catch (Exception e) {
            logger.warn("queryUserGenealogyVo error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    private List<UserGenealogyVo> getUserGenealogyVos(Long partyId) throws Exception {

        List<UserGenealogyVo> userGenealogyVos = new ArrayList<>();
        Map<String, Object> paras = new HashMap<>();
        paras.put("creator", partyId);
        paras.put("parentId", 0L);
        List<AppUserGenealogy> appUserGenealogy = userGenealogyManager.queryParas(paras);
        if (appUserGenealogy.size() > 0) {
            userGenealogyVos = DTOUtils.map(appUserGenealogy, UserGenealogyVo.class);
        } else {
            User user = userManager.queryUser(partyId);
            if (user != null) {
                AppUserGenealogy userGenealogy = AppUserGenealogy.builder().name(getShowName(user)).parentId(0L).gender(user.getGender()).creator(partyId).build();
                Boolean insertUserGenealogy = userGenealogyManager.insert(userGenealogy);
                if (insertUserGenealogy) {
                    userGenealogy = userGenealogyManager.query(userGenealogy.getId());
                    userGenealogyVos.add(DTOUtils.map(userGenealogy, UserGenealogyVo.class));
                }
            }
        }
        return userGenealogyVos;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.deleteUserGenealogyVo", description = "删除家谱成员")
    public Response<Boolean> deleteUserGenealogyVo(Long id) {

        Response<Boolean> resp = ResponseUtils.successResponse(false);
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        if (partyId == null) {
            resp.setResonpse(ResponseEnum.SESSION_INVALD);
            return resp;
        }
        //查询家谱成员是否存在
        AppUserGenealogy userGenealogy = userGenealogyManager.query(id);
        if (userGenealogy != null) {
            //查询家谱成员是否有子辈
            Map<String, Object> map = new HashMap<>();
            map.put("parentId", userGenealogy.getId());
            List<AppUserGenealogy> list = userGenealogyManager.queryUserGenealogyParas(map);
            if (list.size() > 0) {   //该成员有子辈，请先删除其子辈
                resp.setFacade(FacadeEnums.ERROR_CHAT_400044);
                return resp;
            } else {
                Boolean delete = userGenealogyManager.delete(id);
                resp.setResult(delete);
            }
        }
        return resp;
    }

    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.privateCopyGenealogysId", description = "拷贝家谱请求id")
    public Response<GenealogyLinkVO> privateCopyGenealogysId(Long toPartyId) {

        Response<GenealogyLinkVO> resp = ResponseUtils.successResponse();

        try {
            checkArgument(toPartyId != null, "非法参数");

            Long fromPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);

            checkArgument(!Objects.equals(fromPartyId, toPartyId), "拷贝失败，无法拷贝给自己");

            //入库
            Date updateTime = new Date();
            AppUserGenerlogyLinkDO userGenerlogyLinkDO = new AppUserGenerlogyLinkDO();

            userGenerlogyLinkDO.setCreateTime(updateTime);
            userGenerlogyLinkDO.setUpdateTime(updateTime);
            userGenerlogyLinkDO.setGenealogyUser(fromPartyId);
            userGenerlogyLinkDO.setPartyId(toPartyId);
            userGenerlogyLinkDO.setType(GenealogyLinkType.PRIVATE_COPY.getValue());
            userGenerlogyLinkDO.setStatus(GenealogyLinkStatus.WAIT.getValue());
            userGenerlogyLinkDO.setIsDeleted("N");

            userGenealogyManager.insertLink(userGenerlogyLinkDO);
            logger.info("===========[[ { 家谱连接保存成功 } ]]===========");
            //返回
            User user = userManager.queryUser(fromPartyId);
            GenealogyLinkVO linkVO = GenealogyLinkVO
                    .builder()
                    .fromPartyId(fromPartyId)
                    .genealogyOwner(fromPartyId)
                    .linkId(userGenerlogyLinkDO.getId())
                    .genealogyOwnerName(getShowName(user))
                    .build();

            resp.setResult(linkVO);
            resp.setTips("成功");
        } catch (IllegalArgumentException e) {
            logger.info("===========拷贝家谱请求id 异常 toPartyId：[[ {} ]]===========", toPartyId, e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setMemo("非法参数");
            resp.setTips(e.getMessage());
        } catch (Exception e) {
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setTips("失败");
            logger.warn("===========拷贝家谱请求id 异常 toPartyId：[[ {} ]]===========", toPartyId, e);
        }
        return resp;
    }

    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.privateCopyGenealogys", description = "拷贝家谱")
    public Response<Boolean> privateCopyGenealogys(Long linkId) {

        Response<Boolean> resp = ResponseUtils.successResponse();

        try {
            checkArgument(linkId != null, "非法参数");

            final Long toPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            AppUserGenerlogyLinkDO linkDO = userGenealogyManager.queryLink(linkId);

            Long toPartyIdFromDb = linkDO.getPartyId();

            if (toPartyIdFromDb == null
                    || !Objects.equals(toPartyId, toPartyIdFromDb)) {
                throw new UnsupportedOperationException("拷贝失败");
            }

            userGenealogyManager.truncateGenealogysByPartyId(toPartyId); //清空
            logger.info("===========删除家谱成功[[ {} ]]===========", toPartyId);
            Map<String, Object> params = Maps.newHashMap();
            params.put("creator", linkDO.getGenealogyUser());
            params.put("parentId", 0L);


            List<AppUserGenealogy> fromList = userGenealogyManager.queryParas(params);

            if (fromList.size() != 0) {
                AppUserGenealogy rootAppUserGenealogy = fromList.get(0);
                copyGenealogy(rootAppUserGenealogy, 0L);
            }

            userGenealogyManager.finishLink(linkId, "");

            User user = userManager.queryUser(toPartyId);
            messageManager.sendMsg(MsgVo
                    .builder()
                    .partyId(linkDO.getGenealogyUser()).msgTitle("家谱通知 ")
                    .isSave(Constant.YESNO.YES)
                    .message(String.format("%s成功拷贝了您的家谱。", getShowName(user)))
                    .msgType(Constant.MsgType.USER_GENERLOGY_TIP.name())
                    .loginCheck(Constant.YESNO.NO.code).
                            passCheck(Constant.YESNO.NO.code)
                    .expiryTime(0)
                    .isDeleted("N")
                    .msgSource(Constant.MsgType.USER_GENERLOGY_TIP.name())
                    .build());

            logger.info("===========家谱复制成功 from -> to[[ {} -> {} ]]===========", linkDO.getGenealogyUser(), toPartyId);
            resp.setResult(true);
            resp.setTips("拷贝家谱成功");
        } catch (IllegalArgumentException e) {
            logger.info("===========拷贝家谱 参数错误 linkId：[[ {} ]]===========", linkId, e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setTips(e.getMessage());
            resp.setMemo("非法参数");
        } catch (UnsupportedOperationException e) {
            logger.info("===========拷贝家谱 重复点击 linkId：[[ {} ]]===========", linkId, e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setTips(e.getMessage());
            resp.setMemo("非法参数");
        } catch (Exception e) {
            logger.warn("===========拷贝家谱 异常 linkId：[[ {} ]]===========", linkId, e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 递归拷贝家谱
     *
     * @param rootAppUserGenealogy
     * @param parentId
     */
    private void copyGenealogy(AppUserGenealogy rootAppUserGenealogy, Long parentId) {

        final Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        final Date createDate = new Date();

        rootAppUserGenealogy.setId(null);
        rootAppUserGenealogy.setParentId(parentId);
        rootAppUserGenealogy.setCreateTime(createDate);
        rootAppUserGenealogy.setCreator(partyId);

        userGenealogyManager.insert(rootAppUserGenealogy);

        List<AppUserGenealogy> subUsers = rootAppUserGenealogy.getSubUsers();
        if (!subUsers.isEmpty()) {
            for (AppUserGenealogy subUser : subUsers) {
                copyGenealogy(subUser, rootAppUserGenealogy.getId());
            }
        }
    }

    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.publicAddGenealogyId", description = "获取添加家谱id")
    public Response<GenealogyLinkVO> publicAddGenealogyId(GenealogyLinkDTO linkDTO) {

        Response<GenealogyLinkVO> resp = ResponseUtils.successResponse();
        try {
            checkArgument(linkDTO != null, "非法参数");

            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Long genealogyOwner = linkDTO.getGenealogyUser();
            Long memberPartyId = linkDTO.getPartyId();
            Long parentPartyId = linkDTO.getParentId();

            checkArgument(genealogyOwner != null, "非法参数");
            checkArgument(parentPartyId != null, "非法参数");
            checkArgument(memberPartyId != null, "非法参数");

            if (!partyId.equals(linkDTO.getGenealogyUser())) {
                throw new IllegalArgumentException("非法请求");
            }

            AppUserGenealogy parent = userGenealogyManager.query(parentPartyId);
            if (parent == null && parentPartyId > 0) {
                throw new IllegalArgumentException("父节点不存在");
            }

            AppUserGenerlogyLinkDO userGenerlogyLinkDO = new AppUserGenerlogyLinkDO();

            //入库
            Date updateTime = new Date();
            userGenerlogyLinkDO.setPartyId(memberPartyId);
            userGenerlogyLinkDO.setType(GenealogyLinkType.PRIVATE_ADD.getValue());
            userGenerlogyLinkDO.setParentId(parentPartyId);
            userGenerlogyLinkDO.setCreateTime(updateTime);
            userGenerlogyLinkDO.setUpdateTime(updateTime);
            userGenerlogyLinkDO.setGenealogyUser(genealogyOwner);
            userGenerlogyLinkDO.setStatus(GenealogyLinkStatus.WAIT.getValue());
            userGenerlogyLinkDO.setIsDeleted("N");

            userGenealogyManager.insertLink(userGenerlogyLinkDO);

            logger.info("===========[[ { 家谱连接保存成功 } ]]===========");
            User user = userManager.queryUser(partyId);
            //返回
            GenealogyLinkVO linkVO = GenealogyLinkVO.builder()
                    .genealogyOwner(genealogyOwner)
                    .genealogyOwnerName(getShowName(user))
                    .linkId(userGenerlogyLinkDO.getId())
                    .build();
            resp.setResult(linkVO);
            resp.setTips("成功");
        } catch (IllegalArgumentException e) {
            logger.info("===========获取添加家谱id 参数错误 linkDTO：[[ {} ]]===========", JSON.toJSONString(linkDTO), e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setTips(e.getMessage());
            resp.setMemo(e.getMessage());
        } catch (UnsupportedOperationException e) {
            logger.info("===========获取添加家谱id 重复点击 linkDTO：[[ {} ]]===========", JSON.toJSONString(linkDTO), e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setTips(e.getMessage());
            resp.setMemo(e.getMessage());
        } catch (Exception e) {
            logger.warn("===========获取添加家谱id 异常 linkDTO：[[ {} ]]===========", JSON.toJSONString(linkDTO), e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.privateAddGenealogy", description = "添加家谱-内链")
    public Response<Boolean> privateAddGenealogy(Long linkId) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        AppUserGenerlogyLinkDO linkDO = null;
        try {
            checkArgument(linkId != null, "参数不合法");
            linkDO = userGenealogyManager.queryLink(linkId);
            checkArgument(linkDO != null, "linkId错误，分享信息不存在");

            if (!GenealogyLinkType.PRIVATE_ADD.getValue().equals(linkDO.getType())) {
                throw new IllegalArgumentException("参数不合法");
            }

            addGenerlogyMember(linkDO);

            userGenealogyManager.finishLink(linkId, "");

            try {
                sendMsg(linkDO);
            } catch (Exception e) {
                logger.error("===========发送消息异常：[[ {} ]]===========", e);
            }
            resp.setResult(true);
            resp.setTips("添加家谱成功");
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.info("===========获取添加家谱id 参数错误 linkDTO：[[ {} ]]===========", JSON.toJSONString(linkDO), e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setTips(e.getMessage());
            resp.setMemo(e.getMessage());
        } catch (UnsupportedOperationException e) {
            logger.info("===========获取添加家谱id 重复点击 linkDTO：[[ {} ]]===========", JSON.toJSONString(linkDO), e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setTips(e.getMessage());
            resp.setMemo(e.getMessage());
        } catch (Exception e) {
            logger.warn("===========获取添加家谱id 异常 linkDTO：[[ {} ]]===========", JSON.toJSONString(linkDO), e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }

        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.finishPublicAdd", description = "确认添加家谱-外链")
    public void finishPublicAdd() {

        final Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String mobilePhone = sessionHelper.getSessionProp(SessionConstants.LOGIN_NAME, String.class);
        checkArgument(!Strings.isNullOrEmpty(mobilePhone), "手机号码非法");
        List<AppUserGenerlogyLinkDO> appUserGenerlogyLinkDOS = userGenealogyManager.queryLinkByPhone(mobilePhone);

        if (appUserGenerlogyLinkDOS.isEmpty()) {
            logger.info("===========该用户没有需要处理的家谱外链添加[[ {} ]]===========", mobilePhone);
            return;
        }

        for (AppUserGenerlogyLinkDO input : appUserGenerlogyLinkDOS) {

            input.setPartyId(partyId);
            try {
                addGenerlogyMember(input);
            } catch (Exception e) {
                logger.info("===========添加成员失败：[[ {} ]]===========", "该成员已存在");
                userGenealogyManager.finishLink(input.getId(), "添加成员失败,该成员已存在");
                continue;
            }
            userGenealogyManager.finishLink(input.getId(), "");
            logger.info("===========我的家谱：外链添加家谱成员{}成功：linkId[[ {} ]]===========", mobilePhone, input.getId());
            try {
                sendMsg(input);
            } catch (Exception e) {
                logger.warn("===========发送消息异常：[[ {} ]]===========", e);
            }
        }
    }

    private Boolean addGenerlogyMember(AppUserGenerlogyLinkDO linkDO) {

        //锁 保证幂等性
        Long id = linkDO.getId();
        String lockKey = LOCK_PREFIX + id;
        try {
            if (redisUtil.isRepeat(lockKey, 30)) {
                throw new UnsupportedOperationException("添加正在进行，请30秒后重试");
            }
            User memberUser = userManager.queryUser(linkDO.getPartyId());

            Long parentId = linkDO.getParentId();
            Map<String, Object> params = Maps.newHashMap();
            Long genealogyUser = linkDO.getGenealogyUser();
            //String trueName = memberUser.getTrueName();
            String trueName = getShowName(memberUser);
            //根节点添加同辈时添加默认父节点
            if (parentId < 0L) {

                AppUserGenealogy brother = userGenealogyManager.query(-parentId);
                Long brotherParentId = brother.getParentId();

                if (brotherParentId == 0L) {
                    AppUserGenealogy defaultFatherGenealogy = AppUserGenealogy.builder().gender("男").parentId(0L).name("无姓名").creator(genealogyUser).build();
                    userGenealogyManager.insert(defaultFatherGenealogy);

                    AppUserGenealogy build = AppUserGenealogy.builder()
                            .id(brother.getId())
                            .parentId(defaultFatherGenealogy.getId())
                            .build();

                    userGenealogyManager.update(build);

                    parentId = defaultFatherGenealogy.getId();
                } else {
                    parentId = brotherParentId;
                }
            }

            params.clear();
            params.put("parentId", parentId);
            params.put("creator", genealogyUser);
            params.put("name", trueName);
            List<AppUserGenealogy> appUserGenealogies = userGenealogyManager.queryUserGenealogyParas(params);

            if (!appUserGenealogies.isEmpty()) {
                throw new IllegalStateException("已加入");
            }

            AppUserGenealogy appUserGenealogy = new AppUserGenealogy();
            appUserGenealogy.setUpdateTime(new Date());
            appUserGenealogy.setCreateTime(new Date());
            appUserGenealogy.setParentId(parentId);
            appUserGenealogy.setCreator(genealogyUser);
            appUserGenealogy.setBirthday(memberUser.getBirthday());
            appUserGenealogy.setGender(memberUser.getGender());
            //appUserGenealogy.setDistrict(memberUser.getDistrict());
            //appUserGenealogy.setHeadImg(memberUser.getHeadImg());
            appUserGenealogy.setIsDeleted("N");
            appUserGenealogy.setName(trueName);
            //appUserGenealogy.setDescs(memberUser.getDescs());
            appUserGenealogy.setCreateTime(new Date());

            userGenealogyManager.insert(appUserGenealogy);
            return true;
        } finally {
            redisUtil.delete(lockKey);
        }
    }


    @Override
    public Response<Boolean> h5GenerateGenealogyId(GenealogyLinkDTO linkDTO) {

        Response<Boolean> resp = ResponseUtils.successResponse();

        try {
            checkArgument(linkDTO != null, "非法的参数");

            String mobilePhone = linkDTO.getMobilePhone();
            Long genealogyUser = linkDTO.getGenealogyUser();
            Long parentPartyId = linkDTO.getParentId();
            String captcha = linkDTO.getCaptcha();

            checkArgument(!Strings.isNullOrEmpty(mobilePhone), "非法的参数");
            checkArgument(!Strings.isNullOrEmpty(captcha), "非法的参数");
            checkArgument(parentPartyId != null, "非法的参数");
            checkArgument(genealogyUser != null, "非法的参数");

            SmsResponse smsResponse = messageService.checkSmsCode(mobilePhone, captcha, true);

            if (XLStationEnums.ResultSuccess.getCode() != smsResponse.getBussinessCode()) {
                throw new IllegalStateException("验证码错误");
            }

            User userByLoginAccount = userManager.getUserByLoginAccount(mobilePhone);


            AppUserGenerlogyLinkDO linkDO = new AppUserGenerlogyLinkDO();
            linkDO.setMobile(mobilePhone);
            linkDO.setParentId(parentPartyId);
            linkDO.setGenealogyUser(genealogyUser);

            List<AppUserGenerlogyLinkDO> appUserGenerlogyLinkDOS = userGenealogyManager.queryLinkByParams(linkDO);
            if (!appUserGenerlogyLinkDOS.isEmpty()) {
                throw new UnsupportedOperationException("重复添加");
            }

            linkDO.setCreateTime(new Date());
            linkDO.setUpdateTime(new Date());
            linkDO.setType(GenealogyLinkType.PUBLIC_ADD.getValue());
            linkDO.setIsDeleted("N");
            linkDO.setStatus(GenealogyLinkStatus.WAIT.getValue());

            if (userByLoginAccount == null) {  //未注册
                userGenealogyManager.insertLink(linkDO);
                resp.setResult(true);
                resp.setTips("成功");
                return resp;
            } else {  //已注册
                Long partyId = userByLoginAccount.getPartyId();
                linkDO.setPartyId(partyId);
                linkDO.setStatus(GenealogyLinkStatus.FINISH.getValue());
                userGenealogyManager.insertLink(linkDO);
                Boolean isSuccess = addGenerlogyMember(linkDO);

                if (isSuccess) {
                    sendMsg(linkDO);
                    resp.setResult(true);
                    resp.setTips("成功");
                    return resp;
                } else {
                    resp.setResult(false);
                    resp.setTips("失败");
                    return resp;
                }
            }

        } catch (IllegalStateException | UnsupportedOperationException e) {
            logger.info("===========异常：[[ {} ]]===========", e);
            resp.setFacade(FacadeEnums.UPDATE_INVALID);
            resp.setTips(e.getMessage());
        } catch (Exception e) {
            logger.error("===========异常：[[ {} ]]===========", e);
            resp.setFacade(FacadeEnums.UPDATE_INVALID);
            resp.setTips(e.getMessage());
        }
        resp.setResult(false);
        return resp;
    }

    /**
     * 获取外链卡片参数
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.UserGenealogyService.publicShareLinkParam", description = "获取外链卡片参数")
    public Response<WechatShareInfo> publicShareLinkParam() {

        Response<WechatShareInfo> response = ResponseUtils.successResponse();

        try {
            User user = userManager.queryUser(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class));
            WechatShareInfo shareInfo = WechatShareInfo
                    .builder()
                    .title(String.format(SHARE_TITLE, getShowName(user)))
                    .url(SysConfigUtil.getStr("H5WECHAT_GENERLOGY_SHARE_URL") + "?partyId=" + user.getPartyId())
                    .titieImg(SysConfigUtil.getStr("article_share_img") + "?200_200")
                    .content("乡邻APP - 上乡邻,啥都有,发家致富不用愁!")
                    .build();

            response.setResult(shareInfo);
            response.setTips("成功");
        } catch (Exception e) {
            logger.error("===========获取外链卡片参数异常 ：[[ {} ]]===========", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    private void sendMsg(AppUserGenerlogyLinkDO linkDO) throws Exception {

        User ownerUser = userManager.queryUser(linkDO.getGenealogyUser());
        User memberUser = userManager.queryUser(linkDO.getPartyId());

        if (ownerUser == null || memberUser == null) {
            logger.info("===========我的家谱：发送消息失败，查不到用户：家谱拥有者：[[ {} ]]，添加成员:[[ {} ]]===========", linkDO.getGenealogyUser(), linkDO.getPartyId());
            return;
        }

        //String memberUserTrueName = memberUser.getTrueName();
        String memberUserTrueName = getShowName(memberUser);

        messageManager.sendMsg(MsgVo
                .builder()
                .partyId(ownerUser.getPartyId()).msgTitle("家谱通知 ")
                .isSave(Constant.YESNO.YES)
                .message(String.format("%s加入了您的家谱，可至“我的-我的家谱”查看。", memberUserTrueName))
                .msgType(Constant.MsgType.USER_GENERLOGY_TIP.name())
                .loginCheck(Constant.YESNO.NO.code).
                        passCheck(Constant.YESNO.NO.code)
                .expiryTime(0)
                .isDeleted("N")
                .msgSource(Constant.MsgType.USER_GENERLOGY_TIP.name())
                .build());
    }

    /**
     * 获取家谱名字
     *
     * @param user
     * @return
     */
    private String getShowName(User user) {

        if (user == null) {
            return "";
        }
        //使用统一showName
        return user.showName();
    }
}
