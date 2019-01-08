/**
 * 
 */
package com.xianglin.appserv.biz.shared;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.AppActivityModel;
import com.xianglin.appserv.common.dal.dataobject.Msg;
import com.xianglin.appserv.common.dal.dataobject.Page;
import com.xianglin.appserv.common.dal.dataobject.UserMsg;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.PushMsgCheckVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;

/**
 * 
 * 
 * @author wanglei 2016年8月15日上午10:11:15
 */
public interface MessageManager {

	/**
	 * 查询当前用户消息
	 * @param req
	 * @param userPartyId 当前用户partyId
	 * @return
	 * @throws Exception 
	 */
	public List<MsgVo> list(MsgQuery req,Long userPartyId) throws Exception;
	
	/**
	 * 查询消息详情
	 * @param req
	 * @param userPartyId 当前用户partyId
	 * @return
	 * @throws Exception 
	 */
	public MsgVo detail(Long req,Long userPartyId) throws Exception;
	
	/**
	 * 更新/新增,根据req中是否有id确定
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public Long saveUpdate(MsgVo req) throws Exception;
	
	/**
	 * 消息发送
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public Boolean sendMsg(MsgVo req);

	/**
	 * 批量发送消息，指定用户推送推荐该接口
	 * @param req
	 * @param partyIds
	 * @return
	 * @throws Exception
	 */
	public Boolean sendMsg(MsgVo req,List<Long> partyIds) throws BusiException;
	
	/**
	 * 阅读消息，返回消息详细
	 * @param id
	 * @param userPartyId 当前用户partyId
	 * @return
	 * @throws Exception
	 */
	public MsgVo read(Long id,Long userPartyId) throws Exception;

	/**标记类型消息为已读
	 * @param id
	 * @param msgType
	 * @return
	 * @throws Exception
	 */
	public int read(Long id,String msgType) throws Exception;

	/**
	 * 消息点赞
	 * @param id
	 * @param userPartyId 当前用户partyId
	 * @return
	 * @throws Exception
	 */
	public Boolean praise(Long id,Long userPartyId) throws Exception;

	/**
	 * 同步推送消息状态
	 * 
	 * @param req
	 * @return
	 */
	public Boolean updatePushMsg(PushMsgCheckVo req);
	
	/**
	 * 查询消息，不和用户关联
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	List<MsgVo> listNews(MsgQuery req);

	/**按条件查询消息数
	 * @param paras
	 * @return
	 */
	int queryMsgCount(Map<String,Object> paras);

	/**查询设备关联的消息
	 * @param paras
	 * @param page
	 * @return
	 */
	List<Msg> listDeviceNews(UserMsg paras, Page page);

	/**推荐新闻
	 * @param deviceid
	 * @param msgTag
	 * @param pageSize
	 * @return
	 */
	List<Msg> recommendNews(String deviceid,List<String> msgTag,int pageSize);

	/**更新新闻状态
	 * @param paras
	 * @return
	 */
	boolean updateNews(UserMsg paras);

	/**
	 * @param paras
	 * @return
	 */
	List<MsgVo> queryMsgByparam(Map<String, Object> paras);

	/**根据条件查询用户消息
	 * @param um
	 * @param page
	 * @return
	 */
	List<UserMsg> queryUserMsg(UserMsg um,Page page);

	/**根据id查询明细
	 * @param id
	 * @return
	 */
	Msg queryById(Long id);

	/**查询用户待推送消息
	 * 同时将消息从缓存中删除
	 * @param partyId
	 * @return
	 */
	List<Msg> queryPushMsg(Long partyId);
}
