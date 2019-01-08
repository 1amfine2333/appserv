package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.req.DateSectionReq;
import com.xianglin.appserv.common.service.facade.req.FilofaxDetailReq;

import java.util.List;

/** 记账本服务
 * Created by wanglei on 2017/7/4.
 */
public interface FilofaxService {

    /** 查询个人关联类别
     * @param mode 收入支出类型（IN,OUT）
     * @return
     */
    Response<List<FilofaxCategoryVo>> queryCategoryList(String mode);

    /**新增类别
     * @param vo
     * @return
     */
    Response<Boolean> addCategory(FilofaxCategoryVo vo);

    /**查询个人账号列表
     * @param acctId 账号id
     * @return
     */
    Response<List<FilofaxAccountVo>> queryAccountList(Long acctId);

    /**新增账户
     * @param vo
     * @return
     */
    Response<Boolean> addAccount(FilofaxAccountVo vo);

    /**更新账户信息
     * @param vo
     * @return
     */
    Response<Boolean> updateAccount(FilofaxAccountVo vo);

    /**删除帐户
     * @param id
     * @return
     */
    Response<Boolean> deleteAccount(Long id);

    /**记一笔，新增账本明细
     * @param vo
     * @return
     */
    Response<Boolean> addFilofaxDetail(FilofaxDetailVo vo);

    /**更新明细信息
     * @param vo
     * @return
     */
    Response<Boolean> updateFilofaxDetail(FilofaxDetailVo vo);

    /**删除明细
     * @param id
     * @return
     */
    Response<Boolean> deleteFilofaxDetail(Long id,Long partyId);

    /**账本明细查询请求参数
     * @param req
     * @return
     */
    Response<List<FilofaxDetailVo>> queryFilofaxDetailList(FilofaxDetailReq req);

    /**我的钱包->余额查询
     * @return
     */
    Response<FilofaxAccountTotalVo> queryAccountTotal();

    /**日统计查询
     * @return
     */
    Response<DailyDetailTotalVo> queryDailyDetailTotal();

    /**预算金额及对应支出，收入查询
     * 明细页面，按条件返回预算剩余，收入，支出三个金额信息，若对应的预算不存在，则初始一个金额为0的预算
     * @return
     */
    Response<BudgetTotalVo> queryBudgetTotal(BudgetTotalVo vo);

    /**更新预算金额，
     * 若预算不存在，则新增一个
     * @param vo
     * @return
     */
    Response<Boolean> updateBudget(BudgetVo vo);

    /**按条件查询预算信息，若对应的预算不存在，则初始化一个
     * @param vo
     * @return
     */
    Response<BudgetVo> queryBudget(BudgetVo vo);

    /**预算页面，对应每日明细查询
     * @param req
     * @return
     */
    Response<List<DailyDetailTotalVo>> queryBudgetDetail(ProportionTotalVo req);

    /**时间区域内每日占比查询
     * 日度明细页面每日占比
     * @param req
     * @return
     */
    Response<List<DailyProportionVo>> queryDailyProportion(ProportionTotalVo req);

    /**一定时间区域时间内按条件合计查询
     * h5页面统计使用，支持类别，标签的支出/收入统计
     * @param req
     * @return
     */
    Response<List<TotalProportionVo>> queryTotalProportion(ProportionTotalVo req);

    /**年度总乱查询
     * @return
     */
    Response<List<YearTotalProportionVo>> queryYearTotalProportion(int year,Long partyId);
    
    /**
     * h5图表详情页面
     * 一定时间区域的占比和每日明细，包含收入和支出
     * */
    Response<DailyProportionVo> queryProportionAndDetail(ProportionTotalVo req);
    
    /**
     * 现金页面根据账户ID查一定时间区域内的流入和流出
     * 
     * */
    Response<DailyDetailTotalVo> queryInSumAndOutSum(ProportionTotalVo req);
    
}
