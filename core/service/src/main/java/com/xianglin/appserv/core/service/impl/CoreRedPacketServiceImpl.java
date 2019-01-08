/**
 * 
 */
package com.xianglin.appserv.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.daointerface.SystemConfigMapper;
import com.xianglin.appserv.common.dal.dataobject.SystemConfigModel;
import com.xianglin.appserv.common.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.common.dal.daointerface.RedPacketPoolDAO;
import com.xianglin.appserv.common.dal.dataobject.RedPacketPool;
import com.xianglin.appserv.common.service.facade.RedPacketService;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo;
import com.xianglin.appserv.core.service.CoreRedPacketService;

/**
 * 
 * 
 * @author zhangyong 2016年10月11日下午1:28:57
 */
public class CoreRedPacketServiceImpl implements CoreRedPacketService {

	private static final Logger logger = LoggerFactory.getLogger(CoreRedPacketServiceImpl.class);
	@Autowired
	private RedPacketPoolDAO poolDAO;
	
	private RedisUtil redisUtil;

	@Autowired
	SystemConfigMapper systemConfigMapper;
	private String getStuffixKey(String key,String stuffix){
		return key+"_"+stuffix;
	}



	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#push(java.lang.String,java.lang.String,java.util.List,java.lang.Integer)
	 */
	@Override
	public void push(String key,String date, List<String> list,Integer timeout) {
		 redisUtil.push(getStuffixKey(key, date),timeout,
				 list.toArray(new String[list.size()]));
	}
	@Override
	public boolean add(String key,String date,Object obj,Integer timeout){
		return redisUtil.add(getStuffixKey(key, date),
				String.valueOf(obj),timeout);
	}
	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#get(java.lang.String, java.lang.String)
	 */
	@Override
	public String get(String key, String date) {
		return redisUtil.get(getStuffixKey(key, date));
	}
	@Override
	public boolean isExist(String key,String date){
		return redisUtil.isExist(getStuffixKey(key, date));
	}
	@Override
	public boolean isReady(String key,String date,Integer step,Integer count,Integer timeout){
		return redisUtil.isReady(getStuffixKey(key,date), step, count, timeout);
	}
	
	public boolean hmset(String key ,String date,Map<String,Object> map ,Integer timeout){
		return	redisUtil.hmset(getStuffixKey(key,date), map, timeout);
	}
	@Override
	public boolean checkRemains(String date){
		logger.debug("checkRemains num {}",redisUtil.get(getStuffixKey(RedPacketService.RED_PACKET_TOTAL_NUM, date)));
		boolean remains = redisUtil.isReady(getStuffixKey(RedPacketService.RED_PACKET_TOTAL_NUM, date), -1, 1, -1);
		logger.debug("checkRemains:{}",remains);
		if(!remains){
			logger.debug("查看是否还有红包");
			deleteCache(date);
		}
		return remains;
	}
	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#checkHasParticipate(String,java.lang.String)
	 */
	@Override
	public Map<String,Object> checkHasParticipate(String date,String partyId) {
		String key = RedPacketService.RED_PACKET_PARTY_RELATION;
		Map<String,Object> result = new HashMap<>();
		boolean b =	redisUtil.isRepeat(key+partyId, 10);
//		boolean b = redisUtil.sismember(getStuffixKey(RedPacketService.RED_ACTIVITY_MEMBER, date), partyId);
		logger.debug("检查partyId:{},是否抢购过date:{}的红包,结果：{}",partyId,date,b);
		List<RedPacketPool> existList =new ArrayList<>();
		if(!b){
			Map<String,Object> params= new HashMap<>();
			params.put("partyId",Long.valueOf(partyId));
			List<String> types = new ArrayList<>();
			types.add(Constant.RedPacketType.CASH.name());
			types.add(Constant.RedPacketType.COUPON.name());
			params.put("types",types);
			params.put("effectiveDate", DateUtils.formatDate(DateUtils.getNow(),DateUtils.DATE_TPT_TWO));
			existList = poolDAO.getParticipateUser(params);
			if(CollectionUtils.isNotEmpty(existList)){
				b = true;
			}
		}
		result.put("flag", b);
		if(b){
			RedPacketVo vo = null;
			String jsonObj = redisUtil.hget(getStuffixKey(key, date), partyId);
			if(jsonObj == null){
				Map<String,Object> packet_party_relation = new HashMap<>();
				RedPacketPool redPacket = existList.get(0);
				packet_party_relation.put(redPacket.getPartyId().toString(),redPacket);
				try {
					vo = DTOUtils.map(redPacket, RedPacketVo.class);
				} catch (Exception e) {
					logger.error("");
				}
				redisUtil.hmset(getStuffixKey(key, date), packet_party_relation, 15*60);
			}else{
				vo = JSON.parseObject(jsonObj, RedPacketVo.class);
			}
			result.put(key ,vo);
		}
		return result;
	}
	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#remaindNum(java.lang.String)
	 */
	public int remaindNum(String date){
		String remindNum = redisUtil.get(getStuffixKey(RedPacketService.RED_PACKET_TOTAL_NUM, date));
		if(null == remindNum){
			return 0;
		}else{
			return Integer.valueOf(remindNum);
		}
	}
	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#getRedPacketList(com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo)
	 */
	
	@Override
	public List<RedPacketVo> getRedPacketList(RedPacketVo vo) {
		Map<String, Object> map;
		try {
			map = DTOUtils.beanToMap(vo);
			if(vo.getEffectiveDate()!=null){
				map.put("effectiveDate", DateUtils.formatDate(vo.getEffectiveDate(), DateUtils.DATE_TPT_TWO));
			}
			List<RedPacketPool> list = poolDAO.getRedPacketPool(map);
			
			if(CollectionUtils.isNotEmpty(list)){
				return DTOUtils.map(list, RedPacketVo.class);
			}
		} catch (Exception e) {
			logger.error("查询列表出现问题",e);
		}
		return null;
	}

	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#deletedExpiredRedPacket(com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo)
	 */
	@Override
	public int deletedExpiredRedPacket(RedPacketVo vo) {
		Map<String,Object> params = new HashMap<>();
		params.put("status", Constant.RedPacketStatus.EFFECTIVE.name());
		params.put("expiredDate", vo.getExpiredDate());
		params.put("createDate", DateUtils.formatDate(vo.getCreateDate(), DateUtils.DATE_TPT_TWO));
		params.put("updateDate", DateUtils.formatDate(vo.getUpdateDate(), DateUtils.DATE_TPT_TWO));
		return poolDAO.expiredPacketPool(params);
	}

	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#consumeRedPacket(com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo)
	 */
	@Override
	public Boolean consumeRedPacket(RedPacketVo vo) {
		logger.debug("抢购红包，partyId:{},红包类型：{},金额大小:{}",vo.getPartyId(),vo.getType(),vo.getAmount());
		try {
			Date now = DateUtils.getNow();
			String dateStr = DateUtils.formatDate(now, DateUtils.DATE_TPT_TWO);
			Map<String,Object> updateParams = new HashMap<>();
			
			updateParams.put("id", vo.getId());
			updateParams.put("status",vo.getStatus());
			updateParams.put("orderNumber", getSeqNumber());
			updateParams.put("partyId", vo.getPartyId());
			updateParams.put("transactionStatus",vo.getTransactionStatus());
			updateParams.put("userName",vo.getUserName());
			
			updateParams.put("accountName",vo.getAccountName());
			
			
			int seconds =	DateUtils.getIntervalSeconds(now, DateUtils.getLastestTimeOfDay(now));
			/*	int decreaseNum = 0;
			if(Constant.RedPacketType.CASH.name().equals(vo.getType())){
				decreaseNum =Integer.valueOf(PropertiesUtil.getProperty(RedPacketService.CASH_DECREASE_STEP))-1;
			}else if(Constant.RedPacketType.COUPON.name().equals(vo.getType())){
				decreaseNum =Integer.valueOf(PropertiesUtil.getProperty(RedPacketService.COUPON_DECREASE_STEP))-1;
			}
			//不能放在数据库之后，更新数据库有时间，查询redis数量满足条件，则进行更新，会出现抢的超量
			logger.debug("before consume remain num {}",redisUtil.get(getStuffixKey(RedPacketService.RED_PACKET_TOTAL_NUM, dateStr)));
			//当红包减少时，如果此时红包数量为1，现金减去3 则为-2  判断>-3，优惠券减去2，则为-1,判断>-2,在cau减
			boolean b = redisUtil.isReady(getStuffixKey(RedPacketService.RED_PACKET_TOTAL_NUM, dateStr), decreaseNum*(-1),0, -1);//全部消费完
			logger.debug("after consume remain num {}, flag {}",redisUtil.get(getStuffixKey(RedPacketService.RED_PACKET_TOTAL_NUM, dateStr)),b);
			if(!b){
				logger.debug("活动结束，删除缓存");
				deleteCache(dateStr);
				redisUtil.add(getStuffixKey(RedPacketService.RED_ACTIVITY_FLAG,dateStr), Constant.ActivityStatus.END.name(), seconds);
				redisUtil.add(getStuffixKey(RedPacketService.RED_ACTIVITY_DESC,dateStr), Constant.ActivityResultDesc.TOMORROR.getDesc(), seconds);
				return false;
			}*/
			int updateNumber = poolDAO.updateRedPacketPool(updateParams);
			//  数据库控制总数量，执行热点数据更新，乐观锁
			if(updateNumber == 1){//更新成功
				RedPacketPool redPacket =poolDAO.selectByPrimaryKey(vo.getId());
				Map<String,Object> packet_party_relation = new HashMap<>();
				packet_party_relation.put(redPacket.getPartyId().toString(),redPacket);
				redisUtil.hmset(getStuffixKey(RedPacketService.RED_PACKET_PARTY_RELATION,dateStr), packet_party_relation, seconds);
				redisUtil.sadd(getStuffixKey(RedPacketService.RED_ACTIVITY_MEMBER, dateStr),seconds,String.valueOf(vo.getPartyId()));
				return true;
			}else{
				logger.info("红包id：{},已经消费",vo.getId());
			}
		} catch (Exception e) {
			logger.error("消费红包出现问题,",e);
			
		}
		return false;
	}

	
	@Override
	public void deleteCache(String date){
		logger.info("===deleteCache ===");
		redisUtil.delete(getStuffixKey(RedPacketService.RED_ACTIVITY_FLAG,date));
		redisUtil.delete(getStuffixKey(RedPacketService.CASH_RED_PACKET_INFO_KEY, date));
		redisUtil.delete(getStuffixKey(RedPacketService.RED_CASH_ID_KEY,date));
		redisUtil.delete(getStuffixKey(RedPacketService.RED_COUPON_ID_KEY,date));
		redisUtil.delete(getStuffixKey(RedPacketService.RED_PACKET_INFO_KEY,date));
		//redisUtil.delete(getStuffixKey(RedPacketService.RED_PACKET_NUM,date));
//		redisUtil.delete(getStuffixKey(RedPacketService.RED_PACKET_TOTAL_NUM, date));
//		redisUtil.delete(getStuffixKey(RedPacketService.RED_ACTIVITY_MEMBER,date));
//		redisUtil.delete(getStuffixKey(RedPacketService.RED_ACTIVITY_DESC, date));
	}

	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#isActivityStart(java.lang.String)
	 */
	@Override
	public boolean isActivityStart(String date) {
		String value = redisUtil.get(getStuffixKey(RedPacketService.RED_ACTIVITY_FLAG,date));
		return Constant.ActivityStatus.RUNNING.name().equals(value);
	
	}

	/**
	 * 序列号
	 * 
	 * 
	 * @return
	 */
	@Override
	public String getSeqNumber(){
		Long seq = poolDAO.generateRedPacketSeq();
		return  SerialNumberUtil.getOrderSerial(String.valueOf(seq));
		
	}
	public RedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#nextAvailable(String)
	 */
	@Override
	public Long nextAvailable(String key) {
		String value = redisUtil.pop(getStuffixKey(RedPacketService.RED_CASH_ID_KEY, key));
		if(value !=null){
			return Long.valueOf(value);
		}
		return null;
	}
	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#detailInfo(java.lang.Long,String)
	 */
	@Override
	public RedPacketVo detailInfo(Long id,String date) {
		RedPacketVo vo = null;
		String json =	redisUtil.hget(getStuffixKey(RedPacketService.CASH_RED_PACKET_INFO_KEY, date), String.valueOf(id));
		if(json !=null){
			vo = JSON.parseObject(json, RedPacketVo.class);
		}
		return vo;
	
	}
	/**
	 * @see com.xianglin.appserv.core.service.CoreRedPacketService#getParticipateUser(com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo)
	 */
	@Override
	public List<RedPacketVo> getParticipateUser(RedPacketVo vo) {
		
		String dateStr = DateUtils.formatDate(vo.getEffectiveDate(),DateUtils.DATE_TPT_TWO);
		String key = getStuffixKey(RedPacketService.RED_ACTIVITY_MEMBER_INFO,dateStr);
		String member_info =	redisUtil.get(key);
		List<RedPacketVo>  result = new ArrayList<>();
		if(StringUtils.isEmpty(member_info)){
			logger.info("get participateUser from db");
			Map<String,Object> param = new HashMap<>();
			
			try {
				param = DTOUtils.beanToMap(vo);
				param.put("pageSize", 20);
				param.put("startPage",0);
				param.put("effectiveDate",dateStr);
				List<String> types = new ArrayList<>();
				types.add(Constant.RedPacketType.CASH.name());
				types.add(Constant.RedPacketType.COUPON.name());
				param.put("types", types);
				List<RedPacketPool>	 list =poolDAO.getParticipateUser(param);

				if(CollectionUtils.isNotEmpty(list)){
					result = DTOUtils.map(list,RedPacketVo.class);
					redisUtil.add(key, JSON.toJSONString(result),60);//保存60秒
				}
				
			} catch (Exception e) {
				logger.error("object to map occur error",e);
			}
		
		}else{
			result = JSON.parseArray(member_info, RedPacketVo.class);
		
		}
		return result;
	}

	@Override
	public boolean isRepeat(String key, int timeout) {
		return redisUtil.isRepeat(key,timeout);
	}

	@Override
	public boolean isActivityOpen() {

		SystemConfigModel model = systemConfigMapper.getSysConfigByKey(Constant.BusiVisitKey.red_activity_switch.code);
		if(model == null){
			return false;
		}
		String para_value = model.getValue();
		if("1".equals(para_value)){
			return true;
		}
		return false;
	}

	@Override
	public RedPacketVo queryById(Long id) throws Exception {
		RedPacketPool pool = poolDAO.selectByPrimaryKey(id);
		if(pool != null){
			return DTOUtils.map(pool,RedPacketVo.class);
		}
		return null;
	}

	@Override
	public RedPacketVo addRedPacket(RedPacketVo vo) throws Exception {
		RedPacketPool pool = DTOUtils.map(vo, RedPacketPool.class);
		poolDAO.insert(pool);
		vo.setId(pool.getId());
		return vo;
	}

	@Override
	public void updatedRedPacket(RedPacketVo vo) throws Exception {
		RedPacketPool pool = DTOUtils.map(vo, RedPacketPool.class);
		poolDAO.updateByPrimaryKeySelective(pool);
	}


}
