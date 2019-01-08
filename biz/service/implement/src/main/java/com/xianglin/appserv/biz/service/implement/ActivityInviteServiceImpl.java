/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.ActivityInviteManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.ActivityInvite;
import com.xianglin.appserv.common.dal.dataobject.ActivityInviteDetail;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.app.ActivityInviteService;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.ActivityInviteSource;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.ActivityInviteStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityInviteDetailVo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityInviteVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.ActivityInviteDetailReq;
import com.xianglin.appserv.common.util.*;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.cif.common.service.facade.model.RoleDTO;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人邀请服务接口实现
 *
 * @author wanglei 2016年12月13日下午4:30:41
 */
@ServiceInterface
public class ActivityInviteServiceImpl implements ActivityInviteService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityInviteServiceImpl.class);

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private ActivityInviteManager activityInviteManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private CustomersInfoService customersInfoService;

    /**
     * @see com.xianglin.appserv.common.service.facade.app.ActivityInviteService#inviteInfo()
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityInviteService.inviteInfo", description = "查询个人推荐基本信息")
    public Response<ActivityInviteVo> inviteInfo() {
        Response<ActivityInviteVo> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            ActivityInviteVo vo = null;
            Map<String, String> wxContent = new HashMap<String, String>();
            Map<String, String> cfContent = new HashMap<String, String>();
            cfContent.put("desc", SysConfigUtil.getStr("activity.invite.desc_url"));
            if (partyId != null) {
                vo = DTOUtils.map(activityInviteManager.queryInit(partyId), ActivityInviteVo.class);
                vo.setRecAmtStr(NumberUtil.amountFormat(vo.getRecAmt()));
                response.setResult(vo);

                wxContent.put("title", SysConfigUtil.getStr("activity.invite_share_title"));
                wxContent.put("msg", SysConfigUtil.getStr("activity.invite_share_msg"));
                wxContent.put("img", SysConfigUtil.getStr("activity.invite_share_img"));
                wxContent.put("wxHref", SysConfigUtil.getStr("activity.invite_url") + "?source=" + ActivityInviteSource.WX.name() + "&pid=" + vo.getPartyId());
                wxContent.put("pyqHref", SysConfigUtil.getStr("activity.invite_url") + "?source=" + ActivityInviteSource.PYQ.name() + "&pid=" + vo.getPartyId());

                cfContent.put("detail", SysConfigUtil.getStr("activity.invite.detail_url"));
                cfContent.put("amt", SysConfigUtil.getStr("activity.invite.amt_url") + "?markFlag=true&mobilePhone=" + sessionHelper.getSessionProp(SessionConstants.LOGIN_NAME, Long.class));
            } else {//游客
                vo = new ActivityInviteVo();
                response.setResult(vo);
            }
            vo.setWxContent(wxContent);
            vo.setCfContent(cfContent);
        } catch (Exception e) {
            logger.warn("inviteInfo error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }


    /**
     * @see com.xianglin.appserv.common.service.facade.app.ActivityInviteService#inviteUserRanking()
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityInviteService.inviteUserRanking", description = "查询当前用户排名")
    public Response<ActivityInviteVo> inviteUserRanking() {
        Response<ActivityInviteVo> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> req = DTOUtils.queryMap();
            req.put("partyId", partyId);
            List<ActivityInvite> list = activityInviteManager.queryRanking(req);
            if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
                ActivityInviteVo vo = DTOUtils.map(list.get(0), ActivityInviteVo.class);
                vo.setRecAmtStr(NumberUtil.amountFormat(vo.getRecAmt()));
                response.setResult(vo);
            }
        } catch (Exception e) {
            logger.warn("inviteUserRanking error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.ActivityInviteService#inviteRanking(com.xianglin.appserv.common.service.facade.model.vo.req.PageReq)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityInviteService.inviteRanking", description = "分页查询邀请排行榜")
    public Response<List<ActivityInviteVo>> inviteRanking(PageReq req) {
        Response<List<ActivityInviteVo>> response = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            paras.put("startPage", req.getCurPage());
            List<ActivityInvite> list = activityInviteManager.queryRanking(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                response.setResult(DTOUtils.map(list, ActivityInviteVo.class));
                for (ActivityInviteVo vo : response.getResult()) {
                    vo.setRecAmtStr(NumberUtil.amountFormat(vo.getRecAmt()));
                }
            } else {
                response.setResult(new ArrayList<ActivityInviteVo>());
            }

        } catch (Exception e) {
            logger.warn("inviteUserRanking error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.ActivityInviteService#inviteAlert()
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityInviteService.inviteAlert", description = "用户登陆后查询该段时间推荐成功的用户")
    public Response<String> inviteAlert() {
        Response<String> response = ResponseUtils.successResponse("");
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                throw new Exception("未登录用户");
            }

            List<ActivityInviteDetail> list = activityInviteManager.queryUpdateRemind(partyId);
            if (CollectionUtils.isNotEmpty(list)) {
                BigDecimal total = BigDecimal.ZERO;
                for (ActivityInviteDetail d : list) {
                    total = total.add(d.getAmt());
                }
                response.setResult(MessageFormat.format(SysConfigUtil.getStr("activity.invite_alert_msg"), list.size(), NumberUtil.amountFormat(total)));
            }
        } catch (Exception e) {
            logger.info("inviteUserRanking error {}", e.getMessage());
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.ActivityInviteService#inviteDetail(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityInviteService.inviteDetail", description = "查询当前用户推荐人列表")
    public Response<List<ActivityInviteDetailVo>> inviteDetail(Request<PageReq> req) {
        Response<List<ActivityInviteDetailVo>> response = ResponseUtils.successResponse();
        try {
            Long partyId = 0L;
            if (req.getUserPartyId() != null) {
                partyId = req.getUserPartyId();
            }
            if (partyId == null || partyId == 0) {
                partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            }
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("activityCode", 106);
            paras.put("recPartyId", partyId);
            if (req.getPartyId() != null) {
                paras.put("partyId", req.getPartyId());
            }
            paras.put("startPage", req.getReq().getStartPage());
            paras.put("pageSize", req.getReq().getPageSize());
            List<ActivityInviteDetail> list = activityInviteManager.queryInvateDetail(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                response.setResult(DTOUtils.map(list, ActivityInviteDetailVo.class));
            } else {
                response.setResult(new ArrayList<ActivityInviteDetailVo>());
            }
        } catch (Exception e) {
            logger.warn("inviteDetail error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityInviteService.inviteDetailCount", description = "查询当前用户推荐用户成功数")
    public Response<Integer> inviteDetailCount(Long partyId) {
        Response<Integer> response = ResponseUtils.successResponse();
        try {
            int count = activityInviteManager.queryDetailCount(ActivityInviteDetail.builder().recPartyId(partyId).activityCode("106").status("S").build());
            response.setResult(count);
        } catch (Exception e) {
            logger.warn("inviteDetail error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }


    /**
     * @see com.xianglin.appserv.common.service.facade.app.ActivityInviteService#invite(com.xianglin.appserv.common.service.facade.model.vo.ActivityInviteDetailVo)
     */
    @Override
    public Response<Boolean> invite(ActivityInviteDetailVo vo) {
        long start = System.currentTimeMillis();
        Response<Boolean> response = ResponseUtils.successResponse(false);
        try {
            int result = 0;
            //先判断用户是否存在，如果不存在再判断是否是已经登录用户
            Map<String, Object> req = DTOUtils.queryMap();
            req.put("activityCode", vo.getActivityCode());
            req.put("loginName", vo.getLoginName());
            List<ActivityInviteDetail> list = activityInviteManager.queryInvateDetail(req);
            if (CollectionUtils.isNotEmpty(list)) {
                for (ActivityInviteDetail activityInviteDetail : list) {
                    activityInviteDetail.setIsDeleted("Y");
                    activityInviteManager.updateInviteDetail(activityInviteDetail);
                }
            }
            //需要判断是否已经是app客户
            User user = userManager.getUserByLoginAccount(vo.getLoginName());
            if (user != null) {
                ActivityInviteDetail ad = new ActivityInviteDetail();
                ad.setLoginName(user.getLoginName());
                ad.setPartyId(user.getPartyId());
                ad.setRecPartyId(vo.getRecPartyId());
                ad.setCommentName(SerialNumberUtil.phoneNumberEncrypt(user.getLoginName()));
                ad.setAmt(BigDecimal.ZERO);
                ad.setActivityCode(vo.getActivityCode());
                ad.setDeviceId(user.getDeviceId());
                ad.setStatus(ActivityInviteStatus.F.name());
                ad.setMsgStatus(YESNO.NO.name());
                ad.setComments("已登陆用户");
                activityInviteManager.addInvateDetail(ad);
                response.setResult(true);
            } else {
                vo.setActivityCode(vo.getActivityCode());
                vo.setStatus(ActivityInviteStatus.I.name());
                vo.setMsgStatus(YESNO.NO.name());
                result = activityInviteManager.addInvateDetail(DTOUtils.map(vo, ActivityInviteDetail.class));

                if(result > 0){
                    CustomersDTO customersDTO = new CustomersDTO();
                    customersDTO.setMobilePhone(vo.getLoginName());
//                    customersDTO.setCreator(vo.getLoginName());
                    customersDTO.setCreator("app");
                    List<RoleDTO> roleDTOs = new ArrayList<>();
//                    RoleDTO role = new RoleDTO();
//                    role.setRoleCode("APP_USER");
//                    role.setRoleName("APP用户");
//                    roleDTOs.add(role);
//                    customersDTO.setRoleDTOs(roleDTOs);

                    logger.info("cif openAccount req:{}", ToStringBuilder.reflectionToString(customersDTO));
                    com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> cifResp = customersInfoService.openAccount(customersDTO, "app");
                    logger.info("cif openAccount resp:{}", ToStringBuilder.reflectionToString(cifResp));

                    customersDTO = cifResp.getResult();
                    customersDTO.setInvitationPartyId(vo.getRecPartyId());
                    customersInfoService.syncInvitationCustomer(customersDTO);
                }
            }
            if (result == 1) {
                response.setResult(true);
            }
        } catch (Exception e) {
            logger.warn("invite error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        logger.info("============ invite startTime:{}，spend：{}", start, (System.currentTimeMillis() - start));
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityInviteService.inviteDetailByParas", description = "根据条件分页查询邀请列表")
    public Response<List<ActivityInviteDetailVo>> inviteDetailByParas(ActivityInviteDetailReq activityInviteDetailReq) {
        Response<List<ActivityInviteDetailVo>> response = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            if (StringUtils.isNotEmpty(activityInviteDetailReq.getActivityCode())) {
                paras.put("activityCode", activityInviteDetailReq.getActivityCode());
            }
            if (activityInviteDetailReq.getRecPartyId() != null) {
                paras.put("recPartyId", activityInviteDetailReq.getRecPartyId());
            }
            if (activityInviteDetailReq.getPartyId() != null) {
                paras.put("partyId", activityInviteDetailReq.getPartyId());
            }
            paras.put("startPage", activityInviteDetailReq.getStartPage());
            paras.put("pageSize", activityInviteDetailReq.getPageSize());
            List<ActivityInviteDetail> list = activityInviteManager.queryInvateDetail(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                response.setResult(DTOUtils.map(list, ActivityInviteDetailVo.class));
            } else {
                response.setResult(new ArrayList<ActivityInviteDetailVo>());
            }
        } catch (Exception e) {
            logger.warn("inviteDetail error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityInviteService.inviteDetailCountByParas", description = "查询当前用户邀请用户成功数")
    public Response<Integer> inviteDetailCountByParas(ActivityInviteDetailVo activityInviteDetailVo) {
        Response<Integer> response = ResponseUtils.successResponse(0);
        try {
            if (activityInviteDetailVo != null) {
                int count = activityInviteManager.queryDetailCount(DTOUtils.map(activityInviteDetailVo, ActivityInviteDetail.class));
                response.setResult(count);
            }
        } catch (Exception e) {
            logger.warn("inviteDetail error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

}
