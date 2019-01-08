/**
 * 
 */
package com.xianglin.appserv.biz.service.implement;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.AppInstall;
import com.xianglin.appserv.common.service.facade.AppInstallUploadService;
import com.xianglin.appserv.common.service.facade.model.InstallAppDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.core.service.CoreAppInstallUploadService;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;

/**
 * 
 * 
 * @author zhangyong 2016年9月2日下午12:17:53
 */
@Service
@ServiceInterface
public class AppInstallUploadServiceImpl implements AppInstallUploadService {
	private static final Logger logger = LoggerFactory.getLogger(AppInstallUploadServiceImpl.class);
	@Autowired
	private CoreAppInstallUploadService coreUploadService;
	
	/**
	 * @see com.xianglin.appserv.common.service.facade.AppInstallUploadService#uploadAppInstallInfo(java.util.List)
	 */
	@Override
	@ServiceMethod(alias="com.xianglin.appserv.core.service.AppInstallUploadService.uploadAppInstallInfo" ,description="上传安装软件信息")
	public Response<Boolean> uploadAppInstallInfo(List<InstallAppDTO> list) {
		
		Response<Boolean> response = ResponseUtils.successResponse();
		List<AppInstall> applist;
		
		try {
			applist = DTOUtils.map(list, AppInstall.class);
			Map<String,Object> map = coreUploadService.uploadAppInstallInfo(applist);
			response.setResult(true);
			logger.info("上传数据信息:{}",map);
		} catch (Exception e) {
			response.setResult(false);
			logger.error("上传数据异常：",e);
			response.setTips("上传数据失败");
		}
		return response;
	}
	
}
