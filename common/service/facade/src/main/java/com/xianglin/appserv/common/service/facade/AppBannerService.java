/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.BannerVo;

/**
 * 
 * 
 * @author zhangyong 2016年9月26日下午3:41:00
 */
public interface AppBannerService {

	/**
	 * 查询banner
	 * 
	 * 
	 * @param param
	 * @return
	 */
	Response<List<BannerVo>> queryBannerList(Map<String,Object> param);
	/**
	 * 增加banner
	 * 
	 * 
	 * @param req
	 * @return
	 */
	Response<Boolean> addBanner(Request<BannerVo> req);
	/**
	 * 更新banner
	 * 
	 * 
	 * @param req
	 * @return
	 */
	Response<Boolean> updateBanner(Request<BannerVo> req);
	
}
