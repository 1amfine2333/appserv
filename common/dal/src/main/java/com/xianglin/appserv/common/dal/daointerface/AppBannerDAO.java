package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.AppBanner;
import com.xianglin.appserv.common.dal.dataobject.AppBusiness;
import org.apache.ibatis.annotations.Param;


public interface AppBannerDAO extends BaseDAO<AppBanner>{
	/**
	 * 查询banner,
	 * 符合distict_Code is NULl OR districtCode ='ALL'
	 * support_Version is NULL OR supportVersion='ALL'
	 * @param params
	 * @return
     */
	List<Map<String,Object>> queryAppBanerList(Map<String,Object> params);
	
	/**
	 * 根据条件查询bannner列表
	 * 
	 * @param params
	 * @return
	 */
	List<AppBanner> queryBanerList(Map<String,Object> params);

    /**
     * 根据条件查询bannner列表
     * @param appBanner
     * @return
     */
    List<AppBanner> queryBanner(@Param("param")AppBanner appBanner);

}
