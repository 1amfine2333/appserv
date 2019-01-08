/**
 * 
 */
package com.xianglin.appserv.biz.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppBannerDAO;
import com.xianglin.appserv.common.dal.dataobject.AppBanner;
import com.xianglin.appserv.common.service.facade.AppBannerService;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.BannerVo;
import com.xianglin.appserv.common.util.DTOUtils;

/**
 * 
 * 
 * @author zhangyong 2016年9月26日下午4:34:11
 */
public class AppBannerServiceImpl implements AppBannerService {

	private static final Logger logger = LoggerFactory.getLogger(AppBannerServiceImpl.class);
	@Autowired
	private AppBannerDAO bannerDao;
	/**
	 * @see com.xianglin.appserv.common.service.facade.AppBannerService#queryBannerList(java.util.Map)
	 */
	@Override
	public Response<List<BannerVo>> queryBannerList(Map<String, Object> param) {
		Response<List<BannerVo>> resp = new Response<>(null);
		resp.setFacade(FacadeEnums.OK);
		List<AppBanner>  list = bannerDao.queryBanerList(param);

		List<BannerVo> voList;
		if(CollectionUtils.isNotEmpty(list)){
			try {
				voList = DTOUtils.map(list, BannerVo.class);
				resp.setResult(voList);
			} catch (Exception e) {
				logger.warn("查询banner列表错误，",e);
				resp.setFacade(FacadeEnums.RETURN_EMPTY);
			}
		}
		return resp;
	}

	/**
	 * @see com.xianglin.appserv.common.service.facade.AppBannerService#addBanner(com.xianglin.appserv.common.service.facade.model.Request)
	 */
	@Override
	public Response<Boolean> addBanner(Request<BannerVo> req) {
		Response<Boolean> resp = new  Response<>(false);
		if(req == null || req.getReq() == null){
			resp.setFacade(FacadeEnums.E_S_DATA_PARAM_ERROR);
		}else{
			AppBanner appBanner;
			try {
				appBanner = DTOUtils.map(req.getReq(), AppBanner.class);
				Boolean success = bannerDao.insert(appBanner) > 0;
				resp.setResult(success);
				resp.setFacade(FacadeEnums.OK);
			} catch (Exception e) {
				logger.warn("addBanner error,",e);
				resp.setFacade(FacadeEnums.E_S_PERSISTENT_ERROR);
			}
		
		}
		return resp;
	}

	/**
	 * @see com.xianglin.appserv.common.service.facade.AppBannerService#updateBanner(com.xianglin.appserv.common.service.facade.model.Request)
	 */
	@Override
	public Response<Boolean> updateBanner(Request<BannerVo> req) {
		Response<Boolean> resp = new  Response<>(false);
		if(req == null || req.getReq() == null){
			resp.setFacade(FacadeEnums.E_S_DATA_PARAM_ERROR);
		}else{
			AppBanner appBanner;
			try {
				appBanner = DTOUtils.map(req.getReq(), AppBanner.class);
				Boolean success = bannerDao.updateByPrimaryKeySelective(appBanner) > 0;
				resp.setResult(success);
				resp.setFacade(FacadeEnums.OK);
			} catch (Exception e) {
				logger.warn("updateBanner error,",e);
				resp.setFacade(FacadeEnums.E_S_PERSISTENT_ERROR);
			}
			
		}
		return resp;
	}

}
