package com.xianglin.appserv.core.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.common.dal.daointerface.AppActiveShareMapper;
import com.xianglin.appserv.common.dal.daointerface.AppPrizeUserRelModelMapper;
import com.xianglin.appserv.common.dal.dataobject.AppActiveShareExample;
import com.xianglin.appserv.common.dal.dataobject.AppPrizeUserRelModel;
import com.xianglin.appserv.common.dal.dataobject.AppPrizeUserRelModelExample;
import com.xianglin.appserv.common.service.facade.RedPacketService;
import com.xianglin.appserv.common.service.facade.model.AppPrizeUserRelDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizesActivityRuleDTO;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.TransactionStatus;
import com.xianglin.appserv.common.service.facade.model.enums.DataStatusEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo;
import com.xianglin.appserv.core.service.enums.PrizeTypeEnum;
/**
 * 转盘核心逻辑处理
 * @author gengchaogang
 * @dateTime 2017年1月3日 下午5:15:01
 *
 */
@Service
@Transactional(rollbackFor = {BusiException.class,RuntimeException.class})
public class LuckWheelMakeCoreService extends AbstractDistributeLock<AppPrizeUserRelDTO,AppPrizeUserRelDTO> {

	private final Logger logger = LoggerFactory.getLogger(LuckWheelMakeCoreService.class);
	private static final String LUCKWHEEL_COUPON_URL="luckwheel.coupon.url";
	private static final String ecshop_key_prefix="xianglin";
	private static final String ecshop_key_mid_prefix="@#_$&";
	private static final String RED_PACKET_RAND_LUCK_WHEEL = "RED_PACKET_RAND_LUCK_WHEEL";
	private static final String LUCK_WHEEL_MAX_APPLY = "LUCK_WHEEL_MAX_APPLY";
	private static final String LUCK_WHEEL_FLAG = "LUCK_WHEEL_FLAG";
	
	@Autowired
	private LuckWheelCoreServivce luckWheelCoreServivce;
	@Autowired
	private AppPrizeUserRelModelMapper appPrizeUserRelModelMapper;
	@Autowired
	private AppActiveShareMapper appActiveShareMapper;
	@Autowired
	private CoreRedPacketService coreRedPacketService;
	@Autowired
	private RedPacketService redPacketService;

	public static String getLuckWheelMaxApply() {
		return LUCK_WHEEL_MAX_APPLY;
	}

	@Override
	AppPrizeUserRelDTO process(AppPrizeUserRelDTO appPrizeUserRelDTO) throws BusiException {
		String flag = SysConfigUtil.getStr(LUCK_WHEEL_FLAG, "N");
		if(DataStatusEnums.N.getCode().equals(flag)){
			throw new BusiException(ResponseEnum.BUSI_INVALD,"大转盘活动已经结束，感谢您的关注！");
		}
		//业务校验
		AppPrizeUserRelModelExample appPrizeUserRelModelExample = new AppPrizeUserRelModelExample();
		appPrizeUserRelModelExample.createCriteria().andActiveidEqualTo(appPrizeUserRelDTO.getActiveid()).andPrizetimeEqualTo(new Date())
		.andPartyidEqualTo(appPrizeUserRelDTO.getPartyid()).andDataStatusEqualTo(DataStatusEnums.Y.getCode());
		int hasApplyCount = appPrizeUserRelModelMapper.countByExample(appPrizeUserRelModelExample);//当天参加次数
		int maxApply = SysConfigUtil.getInt(LUCK_WHEEL_MAX_APPLY,2);//最大申请次数=正常申请次数+分享增加1次
		if(hasApplyCount >= maxApply){//首次+分享
			throw new BusiException(ResponseEnum.BUSI_INVALD_LUCKWHEEL_NO);
		}
		if(hasApplyCount == (maxApply - 1)){//判断分享数据
			AppActiveShareExample appActiveShareExample = new AppActiveShareExample();
			appActiveShareExample.createCriteria().andActiveidEqualTo(appPrizeUserRelDTO.getActiveid()).andSharetimeEqualTo(new Date())
			.andPartyidEqualTo(appPrizeUserRelDTO.getPartyid()).andDataStatusEqualTo(DataStatusEnums.Y.getCode());
			int shareAccount = appActiveShareMapper.countByExample(appActiveShareExample);
			if(shareAccount == 0){
				throw new BusiException(ResponseEnum.BUSI_INVALD_LUCKWHEEL_SHARE);
			}
		}
		//开始抽奖
		AppPrizesActivityRuleDTO appPrizesActivityRuleDTO = this.makePrize(appPrizeUserRelDTO);
		//插入记录表
		AppPrizeUserRelModel appPrizeUserRelModel = new AppPrizeUserRelModel();
		appPrizeUserRelModel.setActiveid(appPrizesActivityRuleDTO.getActiveId().longValue());
		appPrizeUserRelModel.setPrizeid(appPrizesActivityRuleDTO.getPrizeId().longValue());
		appPrizeUserRelModel.setPname(appPrizesActivityRuleDTO.getPname());
		appPrizeUserRelModel.setPartyid(appPrizeUserRelDTO.getPartyid());
		appPrizeUserRelModel.setPrizetime(new Date());
		appPrizeUserRelModel.setCreateTime(new Date());
		appPrizeUserRelModel.setDataStatus(DataStatusEnums.Y.getCode());
		appPrizeUserRelModelMapper.insertSelective(appPrizeUserRelModel);
		
		AppPrizeUserRelDTO appPrizeUserRelDTOResult = new AppPrizeUserRelDTO();
		try {
			BeanUtils.copyProperties(appPrizeUserRelDTOResult, appPrizeUserRelModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("对象转换异常",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
		appPrizeUserRelDTOResult.setPmoney(appPrizesActivityRuleDTO.getPmoney());
		return appPrizeUserRelDTOResult;
	}
	/**
	 * 抽奖
	 * @author gengchaogang
	 * @dateTime 2017年1月5日 下午3:48:48
	 * @param appPrizeUserRelDTO
	 * @return
	 * @throws BusiException
	 */
	private AppPrizesActivityRuleDTO makePrize(AppPrizeUserRelDTO appPrizeUserRelDTO) throws BusiException {
		int random = -1;
		List<AppPrizesActivityRuleDTO> appPrizesActivityRuleDTOs = null;
		try {
			AppPrizesActivityRuleDTO appPrizesActivityRuleDTO = new AppPrizesActivityRuleDTO();
			appPrizesActivityRuleDTO.setActiveId(new BigDecimal(appPrizeUserRelDTO.getActiveid()));
			appPrizesActivityRuleDTOs = luckWheelCoreServivce.getAppPrizesActivityRule(appPrizesActivityRuleDTO);
			// 计算总权重
			BigDecimal sumWeight = new BigDecimal("0");
			BigDecimal sumPamount = new BigDecimal("0");
			for (AppPrizesActivityRuleDTO appPrizesActivityRuleDTO2 : appPrizesActivityRuleDTOs) {
				sumWeight = sumWeight.add(appPrizesActivityRuleDTO2.getPweight());
				sumPamount = sumPamount.add(appPrizesActivityRuleDTO2.getPamount());
			}
			if(sumPamount.longValue() == 0){
				throw new BusiException(ResponseEnum.BUSI_INVALD,"奖品数量规则配置错误");
			}
			random = getRandomIndex(appPrizesActivityRuleDTOs, sumWeight);
		} catch (Exception e) {
			logger.error("生成抽奖随机数出错，出错原因：", e);
			throw new BusiException(ResponseEnum.BUSI_INVALD);
		}
		AppPrizesActivityRuleDTO appPrizesActivityRuleDTO = appPrizesActivityRuleDTOs.get(random);
		switch (PrizeTypeEnum.getEnumByPtype(appPrizesActivityRuleDTO.getPtype())) {
			case R1://红包
				double pmoney = this.initLuckRedPacket(appPrizeUserRelDTO);
				appPrizesActivityRuleDTO.setPmoney(new BigDecimal(pmoney));
				appPrizesActivityRuleDTO.setPname(pmoney + "元红包");
				break;
			case Y1://发放优惠劵
				this.initCouponVo(appPrizeUserRelDTO,appPrizesActivityRuleDTO.getPmoney(),"2");
				break;
			case H1://发放话费劵
				this.initCouponVo(appPrizeUserRelDTO,appPrizesActivityRuleDTO.getPmoney(),"1");
				break;
			default:
				break;
		}
		return appPrizesActivityRuleDTO;
	}
	/**
	 * 红包入账
	 * @author gengchaogang
	 * @dateTime 2017年1月5日 下午3:48:40
	 * @param appPrizeUserRelDTO
	 * @return
	 * @throws BusiException
	 */
	private double initLuckRedPacket(AppPrizeUserRelDTO appPrizeUserRelDTO) throws BusiException{
		double amount = this.getRedPacketMoney();
		//调用资金账户
		RedPacketVo redPacketVo = new RedPacketVo();
		redPacketVo.setAccountName(appPrizeUserRelDTO.getLoginName());
		redPacketVo.setAmount(new BigDecimal(amount));
		redPacketVo.setDescription(Constant.RedPacketType.LUCKWHEEL_RED_PACKET.getDesc());
		redPacketVo.setCreateDate(new Date());
		redPacketVo.setEffectiveDate(new Date());
		redPacketVo.setExpiredDate(new Date());
		redPacketVo.setIsDeleted("0");
		redPacketVo.setOrderNumber(coreRedPacketService.getSeqNumber());
		redPacketVo.setPartyId(Long.valueOf(appPrizeUserRelDTO.getPartyid()));
		redPacketVo.setSource("app");
		redPacketVo.setTransactionStatus(TransactionStatus.SUCCESS.name());
		redPacketVo.setStatus(Constant.RedPacketStatus.USED.name());
		redPacketVo.setType(Constant.RedPacketType.LUCKWHEEL_RED_PACKET.name());
		redPacketVo.setUserName(appPrizeUserRelDTO.getNikerName());
		Request<RedPacketVo> request = new Request<>();
		request.setReq(redPacketVo);
		Response<Boolean> response = redPacketService.cashRedPacketInAccount(request);
		if(!response.getResult()){
			throw new BusiException(ResponseEnum.BUSI_INVALD,"红包入账失败");
		}
		return amount;
	}
	/**
	 * 获取电商优惠劵
	 * @author gengchaogang
	 * @dateTime 2017年1月6日 上午11:37:01
	 * @param appPrizeUserRelDTO
	 * @param pmoney
	 * @throws BusiException
	 */
	private void initCouponVo(AppPrizeUserRelDTO appPrizeUserRelDTO,BigDecimal pmoney,String type) throws BusiException{
		Map<String,String> param = new HashMap<>();
		String app_key = ecshop_key_prefix.concat(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO))
				.concat(ecshop_key_mid_prefix);
		try {
			param.put("party_id", appPrizeUserRelDTO.getPartyid());
			param.put("type", type);
			param.put("amount", pmoney.toPlainString());
			app_key = app_key.concat(SHAUtil.getSortString(param));
			logger.debug("app_key:{}",app_key);
			param.put("sign", SHAUtil.shaEncode(MD5.encode(app_key)));
			String json =	HttpUtils.executePost(PropertiesUtil.getProperty(LUCKWHEEL_COUPON_URL), param,2000);
			logger.info(json);
			if(StringUtils.isEmpty(json)){
				throw new BusiException(ResponseEnum.BUSI_INVALD,"获取优惠劵失败");
			}
			JSONObject object = JSONObject.parseObject(json);
			if(!"1".equals(object.getString("error_code"))){
				logger.error("未得到电商优惠券，原因:{}",object.getString("error"));
				throw new BusiException(ResponseEnum.BUSI_INVALD,"获取优惠劵失败");
			}
		} catch (Exception e) {
			logger.error("获取电商优惠券有问题，使用现金券",e);
			throw new BusiException(ResponseEnum.BUSI_INVALD,"获取优惠劵失败");
		}
	}
	/**
	 * 检查最大数量
	 * @author gengchaogang
	 * @dateTime 2017年1月5日 下午3:48:15
	 * @param appPrizesActivityRuleDTO
	 * @return
	 */
	private boolean checkMaxAmount(AppPrizesActivityRuleDTO appPrizesActivityRuleDTO){
		AppPrizeUserRelModelExample appPrizeUserRelModelExample = new AppPrizeUserRelModelExample();
		appPrizeUserRelModelExample.createCriteria().andActiveidEqualTo(appPrizesActivityRuleDTO.getActiveId().longValue())
		.andPrizeidEqualTo(appPrizesActivityRuleDTO.getPrizeId().longValue())
		.andPrizetimeEqualTo(new Date()) .andDataStatusEqualTo(DataStatusEnums.Y.getCode());
		int count = appPrizeUserRelModelMapper.countByExample(appPrizeUserRelModelExample);
		return count >= appPrizesActivityRuleDTO.getPamount().longValue();
	}
	/**
	 * 随机生成区间数算法
	 * @author gengchaogang
	 * @dateTime 2017年1月5日 下午3:47:59
	 * @return
	 * @throws BusiException
	 */
	private double getRedPacketMoney() throws BusiException{
		String rand = SysConfigUtil.getStr(RED_PACKET_RAND_LUCK_WHEEL, "0.1-0.5");
		if(!rand.contains("-")){
			logger.error("红包金额区间配置有误");
			throw new BusiException(ResponseEnum.BUSI_INVALD);
		}
		String[] rands = rand.split("-");
		double min = Double.valueOf(rands[0]);
		double max = Double.valueOf(rands[1]);
		return new BigDecimal(RandomUtils.nextDouble(min, max)).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	/**
	 * 获取奖品算法
	 * 千万不要将所有奖品配置成空（预留一个谢谢参与奖品或者将奖品最大数量设置为无限大），否则会递归死循环拖死服务器
	 * @author gengchaogang
	 * @dateTime 2017年1月10日 下午4:40:24
	 * @param appPrizesActivityRuleDTOs
	 * @param sumWeight
	 * @return
	 */
	private int getRandomIndex(List<AppPrizesActivityRuleDTO> appPrizesActivityRuleDTOs,BigDecimal sumWeight){
		int random = -1;
		// 产生随机数
		double randomNumber = Math.random();
		// 根据随机数在所有奖品分布的区域并确定所抽奖品
		double d1 = 0;
		double d2 = 0;
		for (int i = 0; i < appPrizesActivityRuleDTOs.size(); i++) {
			if(appPrizesActivityRuleDTOs.get(i).getPamount().intValue() <= 0){//单日最大总量
				continue;
			}
			if(this.checkMaxAmount(appPrizesActivityRuleDTOs.get(i))){
				continue;
			}
			
			if(i != 0){
				d1 += appPrizesActivityRuleDTOs.get(i - 1).getPweight().divide(sumWeight).doubleValue();
			}
			d2 += appPrizesActivityRuleDTOs.get(i).getPweight().divide(sumWeight).doubleValue();
			
			if (randomNumber >= d1 && randomNumber < d2) {
				random = i;
				break;
			}
		}
		if(random != -1){
			return random;
		}else{
			return getRandomIndex(appPrizesActivityRuleDTOs, sumWeight);
		}
	}

}
