package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xianglin.appserv.common.dal.dataobject.SystemConfigModel;

@Repository
public interface SystemConfigMapper extends BaseDAO<SystemConfigModel>{
	/**
	 * 获取系统配置
	 * @author gengchaogang
	 * @dateTime 2015年12月3日 下午6:05:24
	 * @return
	 */
	List<SystemConfigModel> getSysConfig();
	
	/**
	 * 根据key获取配置
	 * 
	 * @param key
	 * @return
	 */
	SystemConfigModel getSysConfigByKey(@Param("matchingKey") String key);

	/**
	 *
	 * @param key
	 * @return
     */
	String getSysConfigValue(@Param("key") String key);

	/**
	 * 分页查询系统参数
	 * @param para
	 * @return
	 */
	List<SystemConfigModel> selectMap(Map<String,Object> para);

    /**
     * 根据参数名称更新参数
     * @param para
     * @return
     */
    int updateByName(SystemConfigModel para);

	/**同步更新策略
	 * @param paraName 数名
	 * @param curValue 当前值
	 * @param resultValue 修改后值
	 * @return
	 */
	int updateSyn(@Param("paraName") String paraName,@Param("curValue") String curValue,@Param("resultValue") String resultValue);
}
