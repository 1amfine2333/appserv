package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.service.facade.req.UserFeedbackReq;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**用户个人页面接口
 * Created by wanglei on 2017/5/2.
 */
public interface PersonalService {

    /**查询个人在我的页面显示信息
     * @return
     */
    Response<PersonalVo> personInfo();

    /**未读消息数
     * @return
     */
    Response<Integer> unReadMsgCount();

    /**用户提交反馈
     * @param content
     * @return
     */
    Response<Boolean> submitFeedback(String content);

    /**查询个人设置信息
     * @return
     */
    Response<PersonalSetVo> personSetInfo();

    /**删除个人消息
     * @param msgIds 删除的消息数组
     * @return 删除的消息条数
     */
    Response<Integer> deleteUserMsg(String[] msgIds);

    /**级联区域查询
     * @param paraCode 父级区域编号 查询省级时传空
     * @return
     */
    Response<List<DistrictVo>> queryDistrictList(String paraCode);

    /**提交实名认证信息
     * @param vo
     * @return
     */
    Response<Boolean> addRealName(RealNameVo vo);

    /**查询实名认证信息
     * @return
     */
    Response<RealNameVo> queryRealName();
    
    /**查询用户信息
     * @return
     */
    Response<UserVo> queryUser(Long partyId);

    /**查询当前登陆用户信息
     * @return
     */
    Response<UserVo> queryPersonal();

    /**更新当前用户信息
     * @param vo
     * @return
     */
    Response<UserVo> updateUser(UserVo vo);

    /**查询站长站点信息
     * @return
     */
    Response<NodeVo> queryNodeInfo();

    /**
     * 根据条件分页查询所有的反馈 
     */
    Response<List<UserFeedbackVo>> queryUserFeedBackByParas(UserFeedbackReq req);

    /**
     * 修改用户反馈
     * @param userFeedbackVo
     * @return
     */
    Response<Boolean> updateUserFeedback(UserFeedbackVo userFeedbackVo);

    /***
     * 根据条件查询反馈数
     * @param req
     * @return
     */
    Response<Integer> queryUserFeedBackCountByParas(UserFeedbackReq req);

    /**查询分享二维码
     * @return
     */
    Response<String> queryQrCode();


    /**
     * 金币系统生成二维码地址
     * @return
     */
    Response<String> queryGoldQRCode();

    /**根据类型查询未读消息数V2
     * @return
     */
    Response<Integer> unReadMsgCountV2(String msgType);

    /**根据类型查询消息
     * @param req
     * @return
     */
    Response<List<MsgVo>> listUserMsg(MsgQuery req);

    /**查询最新一套消息
     * @param req
     * @return
     */
    Response<MsgVo> queryFirstMsg(MsgQuery req);

    /**用户年报数据查询
     * @return
     */
    Response<Map> queryAnnualReport();

    /**
     * 根据身份证号查询3级地址
     * @param idNumber
     * @return
     */
    Response<AreaVo> queryDistrictByIdNumber(String idNumber);

    /**
     * 根据图片，跳转地址，key创建或获取用户的二维码
     * @param img
     * @param url
     * @param key
     * @return
     */
    Response<String> createUserQRCode(String url,String key,String img);

    /**查询用户信息
     * @return
     */
    Response<UserVo> queryUserByPhone(String phone);

    /**
     * 查用户的关注数是否超过10人、是否发过微博、是否签到、是否晒收入
     * @return
     */
    Response<Map<String,Object>> queryUserSignAndSubjectAndFollow(Long partyId);

    /**
     *查询最新发布动态的三个用户 
     * @return
     */
    Response<List<UserVo>> queryNewArticleUser();

    /**
     * 查询当前用户的二维码
     * @return
     */
    Response<String> queryUserQRCode();

    /**查询个人配置
     * @param config
     * @return
     */
    Response<String> queryPersonalConfig(String config);

    /**更新个人配置
     * @param config
     * @param value
     * @return
     */
    Response<String> updatePersonalConfig(String config,String value);
}
