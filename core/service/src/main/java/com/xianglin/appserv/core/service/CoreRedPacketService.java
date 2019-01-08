/**
 * 
 */
package com.xianglin.appserv.core.service;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo;

/**
 * 
 * 
 * @author zhangyong 2016年10月11日上午11:36:49
 */

public interface CoreRedPacketService {

	 void  push(String key,String date,List<String> list,Integer timeout);
	 
	 boolean  add(String key,String date,Object obj,Integer timeout);

	 String get(String key,String date);
	 
	 boolean isExist(String key,String date);
	 
	 boolean isReady(String key,String date,Integer step,Integer count,Integer timeout);
	 
	 boolean hmset(String key ,String date,Map<String,Object> map ,Integer timeout);
	/**
	 * 是否还存在红包
	 * 
	 * 
	 * @param date
	 * @return
	 */
	boolean checkRemains(String date);
	/**
	 * 检查是否参与过
	 * 
	 * 
	 * @param date
	 * @param partyId
	 * @return
	 */
	Map<String,Object> checkHasParticipate(String date,String partyId);
	/**
	 * 红包剩余数量
	 * 
	 * 
	 * @param date
	 * @return
	 */
	int remaindNum(String date);
	/**
	 * 查询红包
	 * 
	 * 
	 * @param vo
	 * @return
	 */
	
	List<RedPacketVo> getRedPacketList(RedPacketVo vo);

	/**
	 * 删除过期红包
	 * 
	 * 
	 * @param vo
	 * @return
	 */
	int deletedExpiredRedPacket(RedPacketVo vo);
	
	
	/**
	 * 消耗红包
	 * 
	 * 
	 * @param vo
	 * @return
	 */
	Boolean consumeRedPacket(RedPacketVo vo);
	/**
	 * 删除缓存
	 * 
	 *
	 */
	void deleteCache(String date);
	
	/**
	 * 活动是否开始
	 * 
	 * 
	 * @param date
	 * @return
	 */
	boolean isActivityStart(String date);
	
	
	String getSeqNumber();
	
	/**
	 * 
	 * 
	 * @param key
	 * @return
	 */
	Long nextAvailable(String key);

	/**
	 * 
	 * 
	 * @param id
	 * @param date
	 * @return
	 */
	RedPacketVo detailInfo(Long id, String date);
	/**
	 * 查询已经参与的人
	 * 缓存到redis一定时间
	 * 
	 * 
	 * @param vo
	 * @return
	 */
	List<RedPacketVo> getParticipateUser(RedPacketVo vo);

	/**
	 *
	 * @param key
	 * @param timeout
     * @return
     */
	boolean isRepeat(String key,int timeout);

	/**
	 * 数据库开关
	 * @return
     */
	boolean isActivityOpen();


	/** 根据红包id查询明细
	 * @param id
	 * @return
	 * @throws Exception
	 */
	RedPacketVo queryById(Long id) throws Exception;

	/** 新增红包
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	RedPacketVo addRedPacket(RedPacketVo vo) throws Exception;

	/** 更新红包信息
	 * @param vo
	 * @throws Exception
	 */
	void updatedRedPacket(RedPacketVo vo) throws Exception;
}
