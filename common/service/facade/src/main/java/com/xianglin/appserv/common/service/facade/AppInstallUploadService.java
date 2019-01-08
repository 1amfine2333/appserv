/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.InstallAppDTO;
import com.xianglin.appserv.common.service.facade.model.Response;

/**
 * 
 * 
 * @author zhangyong 2016年9月1日下午5:42:50
 */
public interface AppInstallUploadService {

	/**
	 * 上传设备已经安装的应用信息
	 */
	Response<Boolean> uploadAppInstallInfo(List<InstallAppDTO> list);
}
