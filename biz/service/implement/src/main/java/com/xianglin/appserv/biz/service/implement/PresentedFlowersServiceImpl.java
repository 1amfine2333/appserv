package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.UserDAO;
import com.xianglin.appserv.common.service.facade.PresentedFlowersService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.AccountNodeManagerVo;
import com.xianglin.appserv.common.service.facade.model.vo.BankImportVo;
import com.xianglin.appserv.common.service.facade.model.vo.NodeVo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.common.util.UrlPendingUtil;
import com.xianglin.appserv.core.model.BaseUrl;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.xlnodecore.common.service.facade.BankImportService;
import com.xianglin.xlnodecore.common.service.facade.NodeService;
import com.xianglin.xlnodecore.common.service.facade.base.CommonListResp;
import com.xianglin.xlnodecore.common.service.facade.base.CommonPageReq;
import com.xianglin.xlnodecore.common.service.facade.model.*;
import com.xianglin.xlnodecore.common.service.facade.req.BankImportReq;
import com.xianglin.xlnodecore.common.service.facade.req.NodeReq;
import com.xianglin.xlnodecore.common.service.facade.resp.NodeResp;
import com.xianglin.xlnodecore.common.service.facade.vo.*;
import com.xianglin.xlnodecore.common.service.integration.FlowersServiceClient;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 献花
 * @dateTime 2016年11月3日 下午1:45:25
 *
 */
@ServiceInterface(PresentedFlowersService.class)
public class PresentedFlowersServiceImpl implements PresentedFlowersService {
	private final static Logger logger = LoggerFactory.getLogger(PresentedFlowersServiceImpl.class);

	//经营管理
	private final static String	OPERATION_MANAGER_URL="OPERATION_MANAGER_URL";
	//业绩收益
	private final static String	INCOME_PROFIT_URL="INCOME_PROFIT_URL";
	//我的排名
	private final static String	MY_RANK_URL="MY_RANK_URL";
	//打赏中间参数页
	private final static String	REWARD_PARAM_URL="REWARD_PARAM_URL";

	@Autowired
    private MessageManager messageManager;
	@Autowired
    private NodeService nodeService;
	@Autowired
	private BankImportService bankImportService;

	@Autowired
	private FlowersServiceClient flowersServiceClient;
	@Autowired
	private LoginAttrUtil loginAttrUtil;

	@Autowired
	private UserDAO userDAO;
	@Override
	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.PresentedFlowersService.insertFlowersLog",description = "献花")
	public Response<FlowersVo> insertFlowersLog(FlowersVo flowersVo) {
		Response<FlowersVo> response = ResponseUtils.successResponse();
		FlowersDTO flowersDTO ;
		try {
			flowersVo.setFromNodeManagerId(loginAttrUtil.getPartyId());
			flowersDTO = DTOUtils.map(flowersVo, FlowersDTO.class);
		} catch (Exception e) {
			logger.warn("插入献花记录出错",e);
			response.setFacade(FacadeEnums.INSERT_FAIL);
			return response;
		}
		AccountNodeManagerVo vo = loginAttrUtil.getAccountNodeManager();
		flowersDTO.setCreator(vo.getTrueName());
		flowersDTO.setCreateDate(new Date());
		flowersDTO.setIsDeleted("0");
		flowersDTO.setSendDate(new Date());
		com.xianglin.xlnodecore.common.service.facade.base.Response<FlowersDTO> responseNc = flowersServiceClient.insertFlowersLog(flowersDTO);
		if(responseNc.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code){
			logger.warn("献花失败");
			response.setFacade(FacadeEnums.INSERT_FAIL);
			response.setMemo(responseNc.getMsg());
            response.setTips(responseNc.getTips());
			return response;
		}
		FlowersVo flowersVoResp ;
		try {
			flowersVoResp = DTOUtils.map(responseNc.getResult(),FlowersVo.class);
			response.setResult(flowersVoResp);
			//消息推送
			//Long toPartyId = Long.valueOf(flowersVo.);

			NodeVo nodeVo = loginAttrUtil.getNodeInfo();
			if(nodeVo == null){
				logger.warn("查询行政区域失败,不进行消息推送");
				return response;
			}
			//List<Map<String,String>> list =	nodeService.queryNodeManagerList(null);
			com.xianglin.xlnodecore.common.service.facade.vo.NodeVo param = new com.xianglin.xlnodecore.common.service.facade.vo.NodeVo();
			param.setNodePartyId(flowersVo.getToNodePartyId());
			NodeReq nodeReq =new NodeReq();
			nodeReq.setVo(param);
			NodeResp resp = nodeService.getNode(nodeReq);
			if(FacadeEnums.OK.code != resp.getCode()){
				logger.warn("根据nodePartyId未查到站点信息 {}",flowersVo.getToNodePartyId());
				return response;
			}
			com.xianglin.xlnodecore.common.service.facade.vo.NodeVo coreNodeVo = resp.getVo();
			com.xianglin.xlnodecore.common.service.facade.base.Response<Map<String, Object>> distrinctFullResult = nodeService.queryDistrictCodeFull(loginAttrUtil.getNodePartyId(), loginAttrUtil.getPartyId());
			if(distrinctFullResult.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code
					|| distrinctFullResult.getResult() == null){
				logger.warn("查询行政区域失败,不进行消息推送");
				return response;
			}
			Map<String, Object> distrinctFullMap = distrinctFullResult.getResult();
			String nodeManagerName = distrinctFullMap.get("nodeManagerName") == null ? " " :
					distrinctFullMap.get("nodeManagerName").toString();
			String cityName = String.valueOf(distrinctFullMap.get("cityName"));
			String msgTitle = "献花提醒";
			String message = String.format("%s%s站长为您献上了一朵大红花哦， 真是羡煞旁人！ ", cityName,nodeManagerName.substring(0, 1));
			this.sendAppMsg(coreNodeVo.getNodeManagerPartyId(), msgTitle, message);
		}catch (Exception e){
			logger.warn("献花返回出问题",e);
		}
		return response;
	}

	@Override
	@ServiceMethod(description = "当天献花列表查询")
	public Response<List<Map<String, Object>>> queryTodayRecordNode(Long nodePartyId, Long nodeManagerPartyId) {
		Response<List<Map<String, Object>>> response = ResponseUtils.successResponse();
		com.xianglin.xlnodecore.common.service.facade.base.Response<List<Map<String, Object>>> responseNc = flowersServiceClient.queryTodayRecordNode(nodePartyId, nodeManagerPartyId);
		if(responseNc.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code){
			logger.warn("当天献花列表查询失败");
			response.setFacade(FacadeEnums.RETURN_EMPTY);
			return response;
		}
		response.setResult(responseNc.getResult());
		return response;
	}

	@Override
	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.PresentedFlowersService.queryHistoryRecordNode",description = "献花列表查询，包含历史数据")
	public Response<CommonResp<List<Map<String, Object>>>> queryHistoryRecordNode(FlowersVo flowersVo) {
		Response<CommonResp<List<Map<String, Object>>>> response = ResponseUtils.successResponse();
		try {
			CommonPageReq<com.xianglin.xlnodecore.common.service.facade.model.FlowersDTO> req  = DTOUtils.map(flowersVo, CommonPageReq.class);
			com.xianglin.xlnodecore.common.service.facade.model.FlowersDTO flowersDTO =DTOUtils.map(flowersVo, com.xianglin.xlnodecore.common.service.facade.model.FlowersDTO.class);

			req.setReqVo(flowersDTO);
			com.xianglin.xlnodecore.common.service.facade.base.CommonListResp<Map<String, Object>> responseNc =
					flowersServiceClient.queryHistoryRecordNode(req);
			if (responseNc.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code) {
				logger.warn("献花列表查询失败");
				response.setFacade(FacadeEnums.RETURN_EMPTY);
				return response;
			}
			CommonResp<List<Map<String,Object>>> resp =new CommonResp<>();
			resp.setResult(responseNc.getVos());
			resp.setCurPage(responseNc.getCurPage());
			resp.setTotalCount(responseNc.getTotalCount());
			response.setResult(resp);
		} catch (Exception e) {
			logger.warn("献花列表查询出现问题", e);
			response.setFacade(FacadeEnums.RETURN_EMPTY);
		}
		return response;
	}

	/**
	 * App消息推送
	 * @dateTime 2016年11月4日 上午10:03:39
	 * @param partyId
	 * @param msgTitle
	 * @param message
	 */
	private void sendAppMsg(Long partyId,String msgTitle,String message){
		MsgVo msgVo = new MsgVo();
		msgVo.setPartyId(partyId);
		msgVo.setMsgTitle(msgTitle);
		msgVo.setMsgType(Constant.MsgType.ARTICLE.name());
		msgVo.setMessage(message);
		try {
			messageManager.sendMsg(msgVo);
		} catch (Exception e) {
			logger.warn("发送消息出现问题",e);
		}
	}

	@Override
	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.PresentedFlowersService.getAgentDetail", description = "当天站点数据汇总")
	public Response<AgentDetailVo> getAgentDetail(BankImportVo bankImportVo) {
		Response<AgentDetailVo> response = ResponseUtils.successResponse();
		BankImportReq bankImportReq;
		AgentDetailVo agentDetailVo;
		try {
			//String districtcode = loginAttrUtil.getSessionStr(SessionConstants.DISTRICT_CODE);
			//bankImportVo.setDistrictCode(districtcode);
			bankImportVo.setNodePartyId(loginAttrUtil.getNodePartyId());
			bankImportReq = DTOUtils.map(bankImportVo, BankImportReq.class);

			com.xianglin.xlnodecore.common.service.facade.base.Response<AgentDetailDTO> responseNc = bankImportService.getAgentDetail(bankImportReq);
			if(responseNc.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code){
				logger.warn("当天站点数据汇总失败");
			//	response.setFacade(FacadeEnums.RETURN_EMPTY);
				agentDetailVo = new AgentDetailVo();
			//	return response;
			}else {
				agentDetailVo = DTOUtils.map(responseNc.getResult(), AgentDetailVo.class);
			}
			response.setResult(agentDetailVo);
		}catch (Exception e){
			logger.warn("当日站点数据汇总出现问题",e);
			agentDetailVo = new AgentDetailVo();
		}
		agentDetailVo.setOperateUrl(SysConfigUtil.getStr(OPERATION_MANAGER_URL));
		agentDetailVo.setProfitUrl(SysConfigUtil.getStr(INCOME_PROFIT_URL));
		agentDetailVo.setMyRankUrl(SysConfigUtil.getStr(MY_RANK_URL));
		agentDetailVo.setRewardParamUrl(SysConfigUtil.getStr(REWARD_PARAM_URL));
		return response;
	}
	

	@Override
	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.PresentedFlowersService.queryRankList" ,description = "当天站点排行")
	public Response<CommonResp<List<Map<String,Object>>>> queryRankList(Map<String, Object> param) {
		Response<CommonResp<List<Map<String,Object>>>> response =ResponseUtils.successResponse();

		CommonListResp<Map<String, Object>>  commonListResp = bankImportService.queryRankList(param);
		if(commonListResp.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code){
			logger.warn("当天站点排行查询失败");
			response.setFacade(FacadeEnums.RETURN_EMPTY);
			return response;
		}else{
			List<Map<String,Object>> listMap = commonListResp.getVos();
			List<Long> partyIds = new ArrayList<>(listMap.size());
			for (Map<String,Object> resultMap:listMap){
				partyIds.add(Long.valueOf(resultMap.get("nodeManagerPartyId").toString()));
			}
			if(CollectionUtils.isNotEmpty(partyIds)){
				List<Map<String,Object>> usersInfo = userDAO.getNickNameByPartyId(partyIds);

				if(CollectionUtils.isNotEmpty(usersInfo)){

					outer:	for(Map<String,Object> flowers:listMap){
						for(Map<String,Object> users:usersInfo){
							if(flowers.get("nodeManagerPartyId").toString().equals(String.valueOf(users.get("party_id")))){
								flowers.put("headImg",users.get("head_img"));
								flowers.put("hasLogin","yes");
								continue outer;
							}
						}
					}
				}
			}
		}
		CommonResp<List<Map<String,Object>>> resp = new CommonResp<>();
		resp.setResult(commonListResp.getVos());
		resp.setCurPage(commonListResp.getCurPage());
		resp.setTotalCount(commonListResp.getTotalCount());
		response.setResult(resp);
		return response;
	}
	

}
