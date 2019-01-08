/**
 * 
 */
package com.xianglin.appserv.common.service.integration.cif;

import com.xianglin.cif.common.service.facade.model.DeviceInfo;
import com.xianglin.cif.common.service.facade.model.LoginDTO;
import com.xianglin.cif.common.service.facade.model.Response;
import com.xianglin.cif.common.service.facade.vo.PersonVo;
import com.xianglin.xlnodecore.common.service.facade.base.CommonResp;
import com.xianglin.xlnodecore.common.service.facade.enums.OperateTypeEnum;
import com.xianglin.xlnodecore.common.service.facade.resp.AccountNodeManagerResp;
import com.xianglin.xlnodecore.common.service.facade.resp.PosLoginResp;
import com.xianglin.xlnodecore.common.service.facade.vo.NodeVo;

/**
 *  cif AppLoginService
 * 
 * 
 * @author cf 2016年8月15日上午10:36:02
 */
 
public interface AppLoginServiceClient {
	/**
	 * cif自动登录
	 * @param loginDTO
	 * @param deviceInfo
	 * @param deviceId
     * @return
     */
	@Deprecated
	Response<String> cifAutoLogin(LoginDTO loginDTO,DeviceInfo deviceInfo,String deviceId);
	/**
	 * cif 登录，验证设备信息
	 * @param loginDTO
	 * @param deviceInfo
	 * @param deviceId
     * @return
     */
	@Deprecated
	Response<String> cifLogin(LoginDTO loginDTO, DeviceInfo deviceInfo, String deviceId);
	/**
	 * 注册用户
	 * @param loginDTO
	 * @param deviceInfo
	 * @param deviceId
     * @return
     */
	@Deprecated
	Response<PersonVo> registerUser(LoginDTO loginDTO,DeviceInfo deviceInfo,String deviceId);
	/**
	 * 根据手机号查询站长partyId
	 * @param mobilePhone
	 * @return
     */
	@Deprecated
	Response<Long> getNodeManagerPartyId(String mobilePhone);

	/**
	 * 主动登录
	 * 
	 * @author hebbo
	 * 
	 * @param loginDTO
	 *            用户登录对象
	 * @param deviceInfo
	 *            设备信息
	 * @return 用户唯一标识
	 */
	@Deprecated
	Response<NodeVo> login(LoginDTO loginDTO, DeviceInfo deviceInfo,String deviceId);
	
	/**
	 * 登录(登陆注册合一)
	 * 
	 * @author hebbo
	 * 
	 * @param loginDTO
	 *            用户登录对象
	 * @param deviceInfo
	 *            设备信息
	 * @return 用户唯一标识
	 */
	@Deprecated
	Response<NodeVo> loginAndRegister(LoginDTO loginDTO, DeviceInfo deviceInfo,String deviceId);

	/**
	 * 自动登录
	 * 
	 * @author hebbo
	 * 
	 * @param loginDTO
	 *            用户登录对象
	 * @param deviceInfo
	 *            设备信息
	 * @return 用户唯一标识
	 */
	@Deprecated
	Response<NodeVo> autoLogin(LoginDTO loginDTO, DeviceInfo deviceInfo,String deviceId);

	/**
	 * 退出登录
	 * 
	 * @author hebbo
	 * 
	 * @return true：退出成功，false：退出失败
	 */
	@Deprecated
	Response<Boolean> logout(String deviceId,Long partyId);
	
	/**
	 * 获取验证码
	 * chengfanfan
	 * @param nodeCode
	 * @param mobilePhone
	 * @return
	 * 2016年8月22日
	 */
	@Deprecated
	Response<Boolean> sendSms(String nodeCode,String mobilePhone);
	
	/**
	 * 获取验证码
	 * @param mobilePhone
	 * @return
	 * 2016年8月22日
	 */
	Response<Boolean> sendSmsOnly(String mobilePhone);
	
	/**
	 * 验证短信验证码
	 * 
	 * 
	 * @param mobilePhone
	 * @param smsCode
	 * @return
	 */
	Response<Boolean> validateSms(String mobilePhone,String smsCode);
	/**
	 * 首次登录
	 * chengfanfan
	 * @param nodeCode
	 * @param smsCode
	 * @param password
	 * @return
	 * 2016年8月22日
	 */
	@Deprecated
	public PosLoginResp userFirstLogin(String nodeCode,String smsCode,String password,String mobilePhone);
	
	/**
	 * 忘记密码
	 * chengfanfan
	 * @param nodeCode
	 * @param smsCode
	 * @param password
	 * @param mobilePhone
	 * @return
	 * 2016年8月22日
	 */
	@Deprecated
	public PosLoginResp resetUserLogin(String nodeCode,String smsCode,String password,String mobilePhone);
	
	/**
	 * 通过partyId获取站点经理的信息
	 * chengfanfan
	 * @param partyId
	 * @return
	 * 2016年8月23日
	 */
	public AccountNodeManagerResp queryNodeManagerByPartyId(Long partyId);

	/**
	 * 根据网点经理partyId查询网点信息
	 * @param partyId
	 * @return
     */
	NodeVo queryNodeInfoByNodeManagerPartyId(Long partyId);
	/**
	 * 通过nodeCode获取站点经理的手机号码
	 * chengfanfan
	 * @param nodeCode
	 * @return
	 * 2016年8月23日
	 */
	@Deprecated
	public  CommonResp<String> queryMobileByNodeCode(String nodeCode);

	/**新增登陆记录
	 * @param objectId
	 * @param accountName
	 * @param creator
	 * @param operateTypeEnum
	 * @param comment
	 */
	@Deprecated
	void saveLog(Long objectId, String accountName, String creator, OperateTypeEnum operateTypeEnum, String comment);

	/**
	 * @param objectId
	 * @param accountName
	 * @param creator
	 * @param operateTypeEnum
	 * @param comment
	 * @param operateResult
	 */
	@Deprecated
	void saveLog(Long objectId, String accountName, String creator, OperateTypeEnum operateTypeEnum, String comment,String operateResult);

	/**查询用户登陆记录
	 * @param partyId
	 * @param type
	 * @return
	 */
	@Deprecated
	int queryLogCcount(Long partyId,String type);
}
