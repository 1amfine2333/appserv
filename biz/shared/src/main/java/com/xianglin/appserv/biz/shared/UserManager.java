/**
 *
 */
package com.xianglin.appserv.biz.shared;

import java.util.List;
import java.util.Map;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xianglin.appserv.common.dal.dataobject.AppUserCreditApply;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.dal.dataobject.UserFeedback;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.AppPushVo;
import com.xianglin.appserv.common.service.facade.model.vo.LoginVo;
import com.xianglin.appserv.common.service.facade.model.vo.UserCreaditApplyVo;
import com.xianglin.merchant.common.service.facade.dto.MerchantOrderParamDTO;
import com.xianglin.merchant.common.service.facade.dto.MerchantRqpayOrderInfoDTO;
import com.xianglin.merchant.common.service.facade.dto.ResponseDTO;

/**
 * app登陆用户管理
 *
 * @author wanglei 2016年8月19日下午3:12:24
 */
public interface UserManager {

	/**
	 * 用户增加或关系更新
	 *
	 * @param user
	 */
	User addUpdateUser(User user);

	/**
	 * 用户主动退出，解除用户和手机的绑定关系
	 *
	 * @param user
	 */
	void logout(User user);

	/** 删除用户
	 * @param id
	 * @return
	 */
	int deleteUser(Long id);

	/**
	 * 新增/更新push信息 根据deviceId是否存在判断
	 *
	 * @param push
	 */
	void saveUpdatePush(AppPushVo push) throws Exception;

	/**
	 * @param loginAccount
	 * @return
	 */
	User getUserByLoginAccount(String loginAccount);

	/**
	 *根据条件查询用户
	 * @param map
	 * @return
	 */
	List<User> getUsersByParam(Map<String, Object> map);

	/**用户信息更新
	 * 更加id进行更新
	 * @param user
	 * @return
	 */
	int updateUser(User user);

	/**根据partyId查询用户
	 * @param partyId
	 * @return
	 */
	User queryUser(Long partyId);

	/**提交用户反馈
	 * @param feedback
	 * @return
	 */
	boolean addUserFeedback(UserFeedback feedback);

	/**查询反馈数
	 * @param paras
	 * @return
	 */
	int queryFeddbackCount(Map<String,Object> paras);

	/**
	 * 查询用户的未读消息个数
	 * @param paras
	 * @return
	 */
	Integer queryMsgCount(Map<String, Object> paras);

	/**
	 * 提交办理信用卡申请
	 * @param appUserCreaditApply
	 * @return
	 */
	Boolean addUserCreaditApply(AppUserCreditApply appUserCreaditApply);

	/**
	 * 删除消息
	 * @param paras
	 * @return
	 */
	Integer updateUserMsg(Map<String, Object> paras);

	/**反馈查询
	 * @param paras
	 * @return
	 */
	List<UserFeedback> selectList(Map<String, Object> paras);

	/**更新反馈信息
	 * @param map
	 * @return
	 */
	Boolean updateUserFeedback(UserFeedback map);

	/**查询反馈明细
	 * @param id
	 * @return
	 */
	UserFeedback queryFeedback(Long id);

	/**2017年报数据查询
	 * @param partyId
	 * @return
	 */
	Map queryAnnualReport(Long partyId);

	/**查询并同步实名认证状态
	 * @param partyId
	 * @return
	 */
	boolean queryAndSynRealName(Long partyId);

	/**渠道用户注册
	 * @param userInfo
	 * @return
	 */
	User channelRegister(User userInfo) throws BusiException;

    /**
     *查询最新发布动态的三个用户 
     * @param param
     * @return
     */
    List<User> selectNewArticleUser(Map<String,Object> param);

    /**
     * 根据partyids查询用户
     * @param partyIds
     * @return
     */
    List<User> selectUser(List<Long> partyIds);
}
