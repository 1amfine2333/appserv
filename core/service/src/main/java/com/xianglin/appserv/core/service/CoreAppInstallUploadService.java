/**
 * 
 */
package com.xianglin.appserv.core.service;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.AppInstall;

/**
 * 
 * 
 * @author zhangyong 2016年9月1日下午5:46:31
 */
public interface CoreAppInstallUploadService {

	/**
	 * 
	 * 
	 * 
	 * @param list
	 * @return
	 */
	Map<String,Object> uploadAppInstallInfo(List<AppInstall> list);
}
