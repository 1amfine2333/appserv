/**
 * 
 */
package com.xianglin.appserv.core.model;

import org.springframework.context.annotation.DependsOn;

import com.xianglin.appserv.common.util.SysConfigUtil;

/**
 * 
 * 
 * @author zhangyong 2016年8月25日下午3:20:24
 */
@DependsOn("sysConfigService")
public class BaseUrl {

	public final static String H5WECHAT_URL_KEY ="H5WECHAT_URL";
	public final static String H5CAU_URL_KEY ="H5CAU_URL";
	public final static String H5WECHAT_URL = SysConfigUtil.getStr(H5WECHAT_URL_KEY);
	public final static String H5CAU_URL = SysConfigUtil.getStr(H5CAU_URL_KEY);
	public final static String BUSINESS_CLOSE_STUFFIX="_CLOSE_H5_URL";
	public final static String BUSINESS_STUFFIX="_H5_URL";

}
