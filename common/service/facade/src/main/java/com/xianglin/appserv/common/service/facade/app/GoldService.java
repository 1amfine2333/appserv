package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Describe :
 * Created by xingyali on 2017/12/11 15:41.
 * Update reason :
 */
public interface GoldService {

    /**
     *签到
     */
    String ACTIVITY_SIGN = "105";

    /**
     *奖励
     */
    String ACTIVITY_REWARD = "106";

    /**
     * 惩罚
     */
    String ACTIVITY_PUBNISH = "107";

    /**
     * 年报弹窗
     */
    String ANNUALREPORT_POPUP = "205";

    /**
     * 大转盘倒计时弹窗
     */
    String COUNTDOWN_POPUP = "109";

    /**
     * 兑换执行状态
     */
    String DO_EXCHANGE_TAG ="do_exchange_tag";


    /**查询签到金币奖励
     * @return
     */
    Response<Integer> querySignGold();

    /**查询注册金币奖励
     * @return
     */
    Response<Integer> queryRegisterGold();

    /**
     * 金币交易记录查询
     * @return
     */
    Response<List<GoldcoinRecordV1>> queryRecord(int startPage,int pageSize);


    /**
     * 查询上一次的金币兑换记录 和晒收入金额
     * @return
     */
    Response<GoldcoinRecordV1> queryRecordByLastTime();


    /**
     * 查询签到天数
     */
    Response<List<GoldSignDateVo>> querySignDay();

    /**
     * 点击签到
     */
    Response<Boolean> sign();

    /**
     * 连续签到
     */
    Response<GoldSignDayVo>  queryContinuitySign();


    /***
     * 领取金币
     */
    Response<Boolean> getGoldcoin(String date);

    /**
     * 查询日常奖励与惩罚
     * @return
     */
    Response<List<ActivityRewardVo>>  querySignAndPunish();

    /**
     * 发送奖励
     * @return
     */
    Response<Integer> award(String type);

    /**
     * 给指定的用户发送奖励
     * @return
     */
    Response<Integer> awardByPartyId(String type,Long partyId);
    
    Response<Integer> queryRewardAmountByCategary(String categary);

    /**执行兑换批次
     * @param batchId
     * @return
     */
    Response<Boolean> doExchange(String batchId);

}
