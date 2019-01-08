package com.xianglin.appserv.common.service.facade.app;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityShareAuthVo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityShareDailyVo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityShareRewardVo;
import com.xianglin.appserv.common.service.facade.model.vo.WechatShareInfo;

/**
 * Created by wanglei on 2017/3/13. 红包雨活动相关接口
 */
public interface ActivityShareService {

	/**
	 * 显示是否弹出红包雨提示 for app
	 * 
	 * @return
	 */
	Response<String> queryShareAlert();

	/**
	 * 客户端确认收到并关闭提示框
	 * 
	 * @return
	 */
	Response<Boolean> confirmShareAlert();

	/**
	 * 查询个人红包雨活动进度 for app
	 * 
	 * @return
	 */
	Response<WechatShareInfo> queryShareProgessAlert();

	/**
	 * 客户端收到并确认提示框
	 * 
	 * @return
	 */
	Response<Boolean> confirmShareProgessAlert();

	/**
	 * 查询用户当日红包雨活动进度 for h5
	 * 
	 * @param partyId
	 * @return
	 */
	Response<ActivityShareDailyVo> queryShareDaily(Long partyId);

	/**
	 * 更新用户当日分享进度
	 * 
	 * @param vo
	 * @return
	 */
	Response<ActivityShareDailyVo> updateShareDaily(ActivityShareDailyVo vo);

	/**
	 * 验证手机验证码并绑定微信号
	 * 
	 * @param vo
	 * @return
	 */
	Response<ActivityShareAuthVo> bindShareAuth(ActivityShareAuthVo vo);

	/**
	 * 微信绑定关系查询
	 * 
	 * @param openid
	 * @return
	 */
	Response<ActivityShareAuthVo> queryShareAuth(String openid);

	/**
	 * 获奖列表查询
	 * 
	 * @param dailyId
	 * @return
	 */
	Response<List<ActivityShareRewardVo>> queryShareReward(Long dailyId);

	/**
	 * 领取奖励
	 * 
	 * @param mobilePhone
	 * @param dailyId
	 * @return
	 */
	Response<ActivityShareRewardVo> receiveShareReward(Long mobilePhone, Long dailyId);
}
