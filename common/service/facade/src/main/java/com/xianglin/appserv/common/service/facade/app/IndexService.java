/**
 * 
 */
package com.xianglin.appserv.common.service.facade.app;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.service.facade.req.ArticleReq;

/**
 * 
 * 
 * @author wanglei 2016年11月25日下午1:49:12
 */
public interface IndexService {

	/**
	 * 查询首页banners列表针对1。3版本
	 * 
	 * @return
	 */
	Response<List<BannerVo>> indexBanners();

	/**
	 * 查询首页banners列表，针对1.4以后版本
	 * 
	 * @param clientVersion
	 * @return
	 */
	Response<List<BannerVo>> indexBannersV2(String clientVersion,String type);

	/**
	 * 查询首页开通业务列表，针对1.3版本
	 * 
	 * @return
	 */
	Response<List<BusinessVo>> indexBusiness();

	/**
	 * 查询首页开通业务列表，真滴1.4及以后版本
	 * 
	 * @param clientVersion
	 * @return
	 */
	Response<List<BusinessVo>> indexBusinessV2(String clientVersion);

	/**
	 * 首页消息查询
	 * 
	 * @param req
	 * @return
	 */
	Response<List<MsgVo>> indexNewsMsg(Request<MsgQuery> req);

	/**
	 * 主题控制
	 * 
	 * @return
	 */
	Response<String> themeSwitch();

	/**
	 * 便民业务全部入口， 针对app3.0以上
	 * 
	 * @param clientVersion
	 *            默认3.0.0
	 * @return
	 */
	Response<BusinessAllVo> indexBusinessAll(String clientVersion);

	/**
	 * 首页弹出窗口提示
	 * 
	 * @param clientVersion
	 *            默认3.0.0
	 * @return
	 */
	Response<IndexAlertVo> indexAlert(String clientVersion);

	/**
	 * 首页弹出窗口以后都不再弹了
	 * 
	 * @param activityCode
	 *            默认3.0.0
	 * @return
	 */
	Response<Boolean> indexAlertConfirm(String activityCode);

	/**
	 * 首页活动入口
	 * 
	 * @param clientVersion
	 *            默认3.0.0
	 * @return
	 */
	Response<List<ActivityVo>> indexActivity(String clientVersion);

	/**
	 * 首页中部滚动栏消息
	 * 
	 * @param clientVersion
	 *            默认3.0.0
	 * @return
	 */
	Response<List<PointRushVo>> indexPointRush(String clientVersion);

	/**
	 * 首页动态推荐
	 * @param clientVersion
	 *            默认3.0.0
	 * @return
	 */
	Response<List<ArticleVo>> indexArticle(String clientVersion);

    /**
     * 查询默认的村
     * @param
     * @return
     */
    Response<VillageVo> queryDefaultVillage();

	/** 首页业务入口查询，
	 * 返回5个入口
	 * @since V3.4.0
	 * @param clientVersion
	 * @return
	 */
	Response<List<BusinessVo>> indexBusinessV3(String clientVersion);

	/**点击业务
	 * @param businessId 业务id
	 * @return
	 */
	Response<Boolean> clickBusiness(Long businessId);

	/**
     * 查询游戏
     * @return
     */
	Response<List<ActivityVo>>  queryGame();

    /**
     *修改游戏点击量 
     * @param id
     * @return
     */
	Response<Boolean> updateClickGame(Long id);


    /**
     * 查询用户的年报账单是否弹窗
     * @return
     */
	Response<Boolean> queryUserAnnualReportPopup();
    
	/**查询距离大转盘活动天数
	 * @return
	 */
	Response<Integer> queryCycleDays();

    /**
     * 根据类型查Banner top
     * @return
     */
	Response<List<BannerVo>> queryTopBanner(List<String> types);

    /**
     * 修改Banner top
     * @param bannerVos
     * @return
     */
	Response<Boolean> updateBannerVo(List<BannerVo> bannerVos);

    /**
     * 新增Banner top
     * @param bannerVo
     * @return
     */
	Response<Boolean> insertBannerVo(BannerVo bannerVo);

    /**
     * 删除Banner top
     * @param id
     * @return
     */
	Response<Boolean> deleteBannerVo(Long id);


    /**
     * 业务全部入口 
     * @param clientVersion
     * @return
     */
    Response<List<BusinessAllV2>> indexBusinessAllV2(String clientVersion);

    /**
     * 根据id查询banner
     * @param id
     * @return
     */
    Response<BannerVo>  queryBannerById(Long id);

    /**
     * 根据code查询business
     * @param code
     * @return
     */
    Response<BusinessVo> queryBusinessByCode(String code);

    /**
     * 查询启动页
     * @return
     */
    Response<BannerVo> queryStartPage();

    /**
     * 查运营位 4.0
     * @return
     */
    Response<List<BannerVo>> queryOpeartePosition();
    

    /**
     * 修改单个banner 4.0
     * @param bannerVo
     * @return
     */
    Response<Boolean> updateBanner(BannerVo bannerVo);

    /**
     * 查首页5条新闻 4.0
     * @return
     */
    Response<List<MsgVo>> queryIndexMsg();

    /**
     * 搜索应用名或微博
     * @param articleReq
     * @return
     */
    Response<BusinessArticleVo> queryBusinessArticleByqueryKey(ArticleReq articleReq);

    /** 首页业务入口查询，
     * 返回4个加10个
     * @since V3.4.0
     * @param clientVersion
     * @return
     */
    Response<BusinessV2> indexBusinessV4(String clientVersion);

    /**
     * 查运营位top后台 4.0
     * @return
     */
    Response<List<BannerVo>> queryOpeartePositionTop();

    /**
     * 查询乡邻广场
     *
     * @param 
     * @return
     */
    Response<List<BannerVo>> queryXlQuare(String types);

	/** 根据语音内容搜索业务
	 * @param voiceMsg
	 * @return
	 */
	Response<List<BusinessVo>> queryVoiceBusiness(String voiceMsg);
}
