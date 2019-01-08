/**
 * 
 */
package com.xianglin.appserv.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xianglin.appserv.common.dal.daointerface.AppInstallDAO;
import com.xianglin.appserv.common.dal.dataobject.AppInstall;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.core.service.CoreAppInstallUploadService;

/**
 * 
 * 
 * @author zhangyong 2016年9月1日下午5:50:06
 */
@Service
public class CoreAppInstallUploadServiceImpl implements CoreAppInstallUploadService {

	private Logger logger = LoggerFactory.getLogger(CoreAppInstallUploadServiceImpl.class);
	@Autowired
	private AppInstallDAO appInstallDao;
	/**
	 * @see com.xianglin.appserv.core.service.AppInstallUploadService#uploadAppInstallInfo(java.util.List)
	 */
	@Override
	//该操作不需要事务
	//@Transactional
	public Map<String,Object> uploadAppInstallInfo(List<AppInstall> list) {
		Map<String,Object> map =new HashMap<>();
		if(CollectionUtils.isEmpty(list)){
			logger.warn("未上传任何设备安装应用信息");
			return map;
		}
		
		AppInstall first  = list.get(0);
		
		String deviceId = first.getDeviceId();
		
		List<AppInstall> hashInstall = appInstallDao.queryAppInstallByDeviceId(deviceId);
		if(CollectionUtils.isEmpty(hashInstall)){
			//直接插入
			int batchInsert = appInstallDao.batchInsertAppInstall(list);
			logger.info("批量插入设备应用数量：{}",batchInsert);
			map.put("insertNum", batchInsert);
			return map;
		}
		
		Map<String,AppInstall> newMap = convertToMap(list);
		Map<String,AppInstall> hashInstallMap = convertToMap(hashInstall);
		String key ="";
		int insertNum =0;
		int updateNum =0;
		int deleteNum =0;
		Date now = DateUtils.getNow();
		for (AppInstall appInstall : list) {
			key = appInstall.getAppName().concat(appInstall.getAppPackage());
			if(newMap.containsKey(key)
					&& !hashInstallMap.containsKey(key)){//新的有，老的没有
				//插入
				appInstall.setIsDeleted("0");
				appInstall.setCreateTime(now);
				appInstall.setUpdateTime(now);
				appInstallDao.insert(appInstall);
				insertNum ++;
			}else if(newMap.containsKey(key) && hashInstallMap.containsKey(key)){
				//更新
				AppInstall updateObj = newMap.get(key);
				updateObj.setUpdateTime(now);
				appInstallDao.updateAppInstall(updateObj);
				updateNum++;
				hashInstallMap.remove(key);
			}else if(!newMap.containsKey(key) && hashInstallMap.containsKey(key)){
				//删除
				appInstallDao.deleteAppInstall(appInstall.getDeviceId(), appInstall.getAppName());
				deleteNum++;
				hashInstallMap.remove(key);
			}
		}
		Set<String> keyset = hashInstallMap.keySet();
		for (String remainkey : keyset) {//将剩余的存量数据置为删除
			appInstallDao.deleteAppInstall(hashInstallMap.get(remainkey).getDeviceId(), hashInstallMap.get(remainkey).getAppName());
			deleteNum ++;
		}
		map.put("insertNum", insertNum);
		map.put("updateNum", updateNum);
		map.put("deleteNum", deleteNum);
		map.put("deviceId", deviceId);
		
		return map;
	}
	
	private Map<String,AppInstall> convertToMap(List<AppInstall> list){
		Map<String,AppInstall> map = new HashMap<>(list.size());
		
		for (AppInstall appInstall : list) {
			map.put(appInstall.getAppName().concat(appInstall.getAppPackage()), appInstall);
		}
		return map;
	}

}
