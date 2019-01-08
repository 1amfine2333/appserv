package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityVo;
import com.xianglin.appserv.common.service.facade.model.vo.MapVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.UserCreaditApplyVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;

import java.util.List;
import java.util.Map;

/**发现主页及详细入口
 * Created by wanglei on 2017/5/2.
 */
public interface DiscoryService {

    /**发现页面优惠专享
     * @return 一期返回固定页面
     */
    Response<String> discoryDiscount();

    /**发现页面活动分页查询
     * @param req
     * @return
     */
    Response<List<ActivityVo>> discoryActivity(PageReq req);

    /**查询活动地址
     * @return
     */
    Response<String> discoryActivityUrl();

    /**发现页面新闻查询
     * @param req
     * @return
     */
    Response<List<MsgVo>> discoryMsg(MsgQuery req);

    /**提交办理信用卡申请
     * @param req
     * @return
     */
    Response<Boolean> submitCreditCardApply(UserCreaditApplyVo req);

    /**查询用户新闻
     * @param req
     * @return
     */
    Response<List<MsgVo>> listUserNews(MsgQuery req);

    /**新闻推荐
     * @param req 设备号
     * @return
     */
    Response<List<MsgVo>> recommendNews(MsgQuery req);

    /**新闻状态更新
     * 包含阅读，分享，点赞等
     * @param req
     * @return
     */
    Response<Boolean> operateNews(MsgQuery req);

    /**
     *查询频道列表 
     */
    Response<List<MapVo>> queryChannel();

    /**
     * 修改频道列表
     * @param list
     * @return
     */
    Response<Boolean> updateChannel(List<MapVo> list);

    /**新闻明显查询
     * @param id
     * @return
     */
    Response<MsgVo> newsDetail(Long id);
}
