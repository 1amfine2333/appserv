/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.PushMsgCheckVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;

/**
 * 
 * @author wanglei 2016年8月12日上午11:22:22
 */
public interface MessageService {

	/**
	 * 查询当前用户消息
	 * @param req
	 * @return
	 */
	public Response<List<MsgVo>> list(Request<MsgQuery> req);
	
	/**
	 * 查询消息详情
	 * @param req
	 * @return
	 */
	public Response<MsgVo> detail(Request<Long> req);
	
	/**
	 * 更新/新增
	 * @param req
	 * @return
	 */
	public Response<Boolean> saveUpdate(Request<MsgVo> req);
	
	/**
	 * 发送消息
	 * @param req
	 * @return
	 */
	public Response<Boolean> sendMsg(Request<MsgVo> req);

	/**
	 * 批量发送消息
	 * @param req
	 * @param partyIds
	 * @return
	 */
	public Response<Boolean> sendMsg(Request<MsgVo> req,List<Long> partyIds);
	
	/**
	 * 查询指定用户是否有未读消息
	 * @param partyId
	 * @return
	 */
	public Response<Boolean> hasUnReadMsg(Long partyId);
	
	/**
	 * 更新消息为已读
	 * @param req
	 * @return
	 */
	public Response<MsgVo> read(Request<Long> req);
	
	/**
	 * 消息点赞
	 * @param req
	 * @return
	 */
	public Response<Boolean> praise(Request<Long> req);
	
	/**
	 * 同步推送消息状态
	 * @param req
	 * @return
	 */
	public Response<Boolean> updatePushMsg(PushMsgCheckVo req);

    /***
     * 推荐6条3天内的播放量最高的视频
     * @return
     */
    public Response<List<MsgVo>> recommendVideo(String msgTag);

	/**查询待推送消息
	 * @return
	 */
	Response<List<MsgVo>> queryPushMsg();

	/**查询待推送轮询开关
	 * @return
	 */
	Response<Boolean> queryPushStatus();
}
