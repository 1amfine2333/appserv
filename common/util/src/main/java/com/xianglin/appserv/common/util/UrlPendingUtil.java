/**
 * 
 */
package com.xianglin.appserv.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 
 * @author zhangyong 2016年8月26日下午4:49:35
 */
public class UrlPendingUtil {

	/**
	 * 拼接url，如果子域名为空，则返回空
	 * 
	 * 
	 * @param baseKey
	 * @param childKey
	 * @return
	 */
	public static String concatUrl(String baseKey,String childKey){
		
		String baseUrl = PropertiesUtil.getProperty(baseKey);
		String childUrl = PropertiesUtil.getProperty(childKey);
		
		if(StringUtils.isEmpty(childUrl)){
			return "";
		}else{
			return baseUrl+childUrl;
		}
	}
}
