/**
 * 
 */
package com.xianglin.appserv.common.service.facade.app;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityInviteDetailVo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityInviteVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.ActivityInviteDetailReq;

/**
 * 用户邀请活动相关接口
 * 
 * @author wanglei 2016年12月13日下午2:45:50
 */
public interface ActivityInviteService {

	/**
	 * 查询个人推荐基本信息，
	 * 
	 * @return
	 */
	Response<ActivityInviteVo> inviteInfo();
	
	/**
	 * 查询当前用户排名
	 * 
	 * @return
	 */
	Response<ActivityInviteVo> inviteUserRanking();
	
	/**
	 * 分页查询邀请排行榜
	 * 
	 * @param req
	 * @return
	 */
	Response<List<ActivityInviteVo>> inviteRanking(PageReq req);
	
	/**
	 * 用户登陆后查询该段时间推荐成功的用户
	 * 
	 * @return
	 */
	Response<String> inviteAlert();
	
	/**
	 * 分页查询个人推荐列表(包括资金)
	 * 
	 * @param req
	 * @return
	 */
	Response<List<ActivityInviteDetailVo>> inviteDetail(Request<PageReq> req);

    /**
     * 查询当前用户邀请用户成功数
     * @param id
     * @return
     */
    Response<Integer> inviteDetailCount(Long id);
	
	/**
	 * 分页查询活动奖励
	 * 
	 * @param req
	 * @return
	 */
	
	/**
	 * 提交绑定关系
	 * 
	 * @param vo
	 * @return
	 */
	Response<Boolean> invite(ActivityInviteDetailVo vo);

    /**
     * 根据条件分页查询邀请列表
     * @param activityInviteDetailReq
     * @return
     */
    Response<List<ActivityInviteDetailVo>> inviteDetailByParas(ActivityInviteDetailReq activityInviteDetailReq);

    /**
     * 查询当前用户邀请用户成功数
     * @param activityInviteDetailVo
     * @return
     */
    Response<Integer> inviteDetailCountByParas(ActivityInviteDetailVo activityInviteDetailVo);
}
