/**
 *
 */
package com.xianglin.appserv.common.service.facade;

import java.util.Map;

import com.xianglin.appserv.common.service.facade.model.DeviceInfo;
import com.xianglin.appserv.common.service.facade.model.LoginDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.LoginVo;
import com.xianglin.appserv.common.service.facade.model.vo.NodeVo;
import com.xianglin.appserv.common.service.facade.model.vo.RealNameVo;
import com.xianglin.appserv.common.service.facade.model.vo.UserVo;


/**
 * APP专用登录服务
 *
 * @author hebbo 2016年8月15日上午9:25:02
 */
public interface AppLoginService {

    /**
     * 主动登录
     *
     * @param loginDTO   用户登录对象
     * @param deviceInfo 设备信息
     * @return 用户唯一标识
     * @author hebbo
     */
    Response<NodeVo> login(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 自动登录
     *
     * @param loginDTO   用户登录对象
     * @param deviceInfo 设备信息
     * @return 用户唯一标识
     * @author hebbo
     */
    Response<NodeVo> autoLogin(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 退出登录
     *
     * @return true：退出成功，false：退出失败
     * @author hebbo
     */
    Response<Boolean> logout(LoginDTO loginDTO);

    /**
     * 获取验证码
     * chengfanfan
     *
     * @param nodeVo
     * @return 2016年8月22日
     */
    Response<Boolean> sendSms(NodeVo nodeVo);

    /**
     * 获取验证码并校验号码是否为新注册号码
     *
     * @param nodeVo
     * @return
     * @dateTime 2016年11月24日 下午6:47:43
     */
    Response<Map<String, Boolean>> sendSmsAndValid(NodeVo nodeVo);

    /**
     * 首次登录
     * chengfanfan
     *
     * @param loginDTO
     * @return 2016年8月22日
     */
    Response<Long> userFirstLogin(LoginDTO loginDTO);

    /**
     * 忘记密码
     * chengfanfan
     *
     * @param loginDTO
     * @return 2016年8月22日
     */
    Response<Long> resetUserLogin(LoginDTO loginDTO);

    /**
     * 获取经理手机号
     * chengfanfan
     *
     * @param loginDTO
     * @return 2016年8月22日
     */
    public Response<String> getMobileByNodeCode(LoginDTO loginDTO);

    /**
     * 通过短信验证码登录
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     */
    Response<NodeVo> loginByMobile(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 通过短信验证码登录
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     */
    Response<NodeVo> loginByMobileV1_3(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 自动登录v1.3+版本
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @dateTime 2016年12月1日 上午10:39:33
     */
    Response<NodeVo> autoLoginVOPT(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 验证短信验证码
     *
     * @param mobilePhone
     * @param smsCode
     * @return
     */
    Response<Boolean> validateSms(String mobilePhone, String smsCode);

    /**
     * V1.3后使用该接口发送短信
     *
     * @param nodeVo
     * @return
     */
    Response<Boolean> smsCodeSend(NodeVo nodeVo);

    /**
     * V1.3后使用该接口发送短信
     *
     * @param phone
     * @return
     */
    Response<Boolean> smsCodeSendV2(String phone);

    /**
     * 充值密码
     *
     * @param password 新密码
     * @return
     */
    Response<Boolean> resetPassword(String password);

    /**
     * 密码登陆
     *
     * @param loginDTO
     * @return
     */
    Response<NodeVo> loginByPassword(LoginDTO loginDTO);

    /**
     * 判断账号是否设置密码
     * s试用于appser版本3.0.1以上
     *
     * @param mobilePhone
     * @return 是否设置过登陆密码
     */
    Response<Boolean> hasPassword(String mobilePhone);

    /**
     * 用户验证码登陆
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.2.1
     */
    Response<String> loginMobileV2(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 用户密码登陆
     *
     * @param loginDTO
     * @return
     * @since v3.2.1
     */
    Response<String> loginPasswordV2(LoginDTO loginDTO);

    /**
     * 用户密码登陆
     *
     * @param password
     * @return
     * @since v3.2.1
     */
    Response<String> setPassword(String password);

    /**
     * 用户实名认证
     *
     * @param vo
     * @return
     * @since v3.2.1
     */
    Response<String> realNameAuth(RealNameVo vo);

    /**
     * 设置用户信息，兵完成登陆
     *
     * @param user
     * @return
     * @since v3.2.1
     */
    Response<String> submitUserInfo(UserVo user);

    /**
     * 自动登陆，返回下一步操作
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.2.1
     */
    Response<String> autoLoginV2(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 用户验证码登陆V3
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.5
     */
    Response<String> loginMobileV3(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 用户密码登陆V3
     *
     * @param loginDTO
     * @return
     * @since v3.5
     */
    Response<String> loginPasswordV3(LoginDTO loginDTO);

    /**
     * 自动登陆V3
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.5
     */
    Response<String> autoLoginV3(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 用户实名认证V3
     *
     * @param vo
     * @return
     * @since v3.5
     */
    Response<String> realNameAuthV3(RealNameVo vo);

    /**未实名认证帐号注销
     * @param mobilePhone 需要注销的手机号
     * @return
     */
    Response<Boolean> cancellation(String mobilePhone);

    /**修改客户手机号
     * @param mobilePhone
     * @param newMobile
     * @return
     */
    Response<Boolean> resetMobile(String mobilePhone,String newMobile);

    /**
     * 用户注册（登陆）V4
     * 支持手机号和验证码，密码，图案密码
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.5.4
     */
    Response<LoginVo> loginV4(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 自动登陆V4
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.5.4
     */
    Response<LoginVo> autoLoginV4(LoginDTO loginDTO, DeviceInfo deviceInfo);

    /**
     * 图形密码
     * @param patternPassword
     * @return
     */
    Response<Boolean> setPatternPassword(String patternPasswordStatus,String patternPassword);

    /**判断用户是否设置密码
     * @param mobilePhone 手机号
     * @param passwordType 密码类型：PWD,PATT_PWD
     * @return
     */
    Response<Boolean> hasPasswordV2(String mobilePhone,String passwordType);

    /**渠道用户注册(同步)
     * 保证partyId唯一，如果已经存在，则做数据更新
     * @param userInfo
     * @return
     */
    Response<LoginVo> channelRegister(LoginVo userInfo);

    /** 强制用户下线
     * @param partyId
     * @param message
     * @return
     */
    Response<Boolean> forceOffline(Long partyId,String message);

}
