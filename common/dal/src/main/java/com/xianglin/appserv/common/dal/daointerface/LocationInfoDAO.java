/**
 * 
 */
package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xianglin.appserv.common.dal.dataobject.LocationInfoDO;

/**
 * 地理位置DAO
 * 
 * @author pengpeng 2016年2月24日下午7:03:46
 */
public interface LocationInfoDAO {

	/**
	 * 根据userId查询地理位置信息
	 * 
	 * @param userId
	 * @return
	 */
	List<LocationInfoDO> getByUser(String userId);

	/**
	 * 插入新的地理位置信息
	 * 
	 * @param locationInfoDO
	 */
	void insert(LocationInfoDO locationInfoDO);

	/**
	 * 将地理位置信息放入历史表
	 * 
	 * @param locationInfoDO
	 */
	void insertToHis(LocationInfoDO locationInfoDO);

	/**
	 * 将地理位置信息放入历史表后，删除地理位置信息表中的记录
	 * 
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 根据geohash和时间查询
	 * 
	 * @param geohash
	 * @param time
	 * @return
	 */
	List<String> getUserIdsByGeohashAndTime(@Param("userId") String userId, @Param("geohash") String geohash,
			@Param("time") String time);

}
