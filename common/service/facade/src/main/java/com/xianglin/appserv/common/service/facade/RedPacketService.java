/**
 *
 */
package com.xianglin.appserv.common.service.facade;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.service.facade.model.PointRushDTO;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.PointRushVo;
import com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo;

/**
 * @author zhangyong 2016年10月11日下午5:46:11
 */
public interface RedPacketService {

    public static String RED_CASH_ID_KEY = "red.packet.id.key";//红包id
    public static String RED_COUPON_ID_KEY = "red.coupon.packet.id.key";//红包id

    public static String CASH_RED_PACKET_INFO_KEY = "cash.red.packet.info.key";//保存现金红包信息

    public static String RED_PACKET_INFO_KEY = "red.packet.info.key";//保存优惠券红包信息

    public static String RED_ACTIVITY_FLAG = "red.packet.activity.flag";//活动是否开始标志


    public static String RED_PACKET_NUM = "red.packet.num";//红包已抢购数量

    public static String CLICK_NUM = "red.activity.click.num";//红包活动点击数

    public static String RED_ACTIVITY_MEMBER = "red.activity.member";//已抢购人信息，保存id，判断是否抢购过

    public static String RED_ACTIVITY_MEMBER_INFO = "red.activity.member.info";//已抢购人信息列表

    public static String RED_PACKET_PARTY_RELATION = "red.packet.party.relation";//抢到的人和红包的关系，缓存到当日截至

    public static String RED_PACKET_TOTAL_NUM = "red.packet.total.num";//红包总数量

    public static String CASH_RED_PACKET_SCALE = "cash.red.packet.scale";//现金券比例

    public static String COUPON_RED_PACKET_SCALE = "coupon.red.packet.scale";//优惠券券比例

    public static String RED_ACTIVITY_DESC = "red.packet.activity.desc";//今日|明日 9：00开抢 |正在开抢中


    public static String CASH_DECREASE_STEP = "cash.decrease.step";//现金券每次减少量
    public static String COUPON_DECREASE_STEP = "coupon.decrease.step";//优惠券每次减少量


    /**
     * 是否还有库存
     *
     * @param date
     * @return
     */
    Response<Boolean> checkRemains(String date);

    /**
     * 是否参与过
     *
     * @param date
     * @param partyId
     * @return
     */
    Response<Map<String, Object>> checkHasParticipate(String date, Long partyId);

    /**
     * 消费一个红包
     *
     * @param req
     * @return
     */
    Response<RedPacketVo> killRedPacket(Request<RedPacketVo> req);

    /**
     * 活动是否开始
     *
     * @return
     */
    Response<Map<String, Object>> isActivityStatus(String date);

    /**
     * 剩余数量
     *
     * @return
     */
    Response<Integer> remainsNum(String date);

    /**
     * 查询已经领到的用户列表
     * 有缓存数据
     *
     * @param req
     * @return
     */
    Response<List<RedPacketVo>> getParticipateUser(Request<RedPacketVo> req);

    /**
     * 取下一个可用的现金红包
     *
     * @return
     */
    Response<RedPacketVo> nextAvailable(String date);

    /**
     * 插入红包
     *
     * @param req
     * @return
     */
    Response<RedPacketVo> addRedPacket(Request<RedPacketVo> req);

    /**
     * 获取比例
     *
     * @param key
     * @return
     */
    Response<Map<String, Double>> getRate(String key);

    /**
     * 实时查询数据库
     *
     * @param req
     * @return
     */
    Response<List<RedPacketVo>> getRedPacketList(Request<RedPacketVo> req);

    /**
     * 现金红包入账
     *
     * @param req
     * @return
     */
    Response<Boolean> cashRedPacketInAccount(Request<RedPacketVo> req);

    /**
     * 红包领取
     */
    Response<RedPacketVo> cashRedPacket(Long partyId);

    /**
     * 查询首页滚动栏消息
     *
     * @param req
     * @return
     */
    Response<PointRushDTO> getPointRushList(Request<RedPacketVo> req);


    /**
     \
     * @param dateStr
     * @param step
     * @param assertNum
     * @return
     */
    Response<Boolean> decreaseTotal(String dateStr, Integer step, Integer assertNum);

    /**
     * 查询是否开放当前用户领取新手红包
     */
    Response<Boolean> isCashRedPacket();

    /**
     * 查询资金账户余额
     *
     * @return
     */
    Response<String> queryAcctBalance();

    /**
     * 查询红包明细
     *
     * @param packetId
     * @return
     */
    Response<RedPacketVo> queryRedPacketDetail(Long packetId);


    /**
     * 发送红包,用于聊天发送红包（个人对个人红包）
     * @param redPacket
     * @param password 密码
     * @return
     */
    Response<Long> sendRedPacket(RedPacketVo redPacket,String password);
}
