/**
 * 
 */
package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.Page;
import org.apache.ibatis.annotations.Param;

import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.dal.dataobject.UserMsg;

/**
 * 
 * @author wanglei 2016年8月12日上午10:07:13
 */
public interface UserMsgDAO extends BaseDAO<UserMsg> {

	/**
	 * 查询消息阅读情况
	 * 
	 * @param paras
	 * @return
	 */
	List<UserMsg> select(Map<String, Object> paras);
	
	/**
	 * 将手机上收到的未关联消息绑定到第一个登陆用户
	 * @param deviceId
	 * @param partyId
	 * @return
	 */
	Integer updateUnBindMsg(@Param("deviceId") String deviceId,@Param("partyId")Long partyId);
	
	/**
	 * 批量插入
	 * @param umList
	 */
	void batchInsert(List<UserMsg> umList);
	
	
	/**
	 * 根据partyID查询未读消息数
	 * @param paras
	 * @return
	 */
	Integer queryCount(Map<String,Object> paras);

	/**
	 * 批量删除消息
	 * @param paras
	 */
	Integer batchDelUserMsg(Map<String,Object> paras);

	/**更新用户消息操作数
	 * @param paras
	 * @return
	 */
	Integer updateMsgOperNum(UserMsg paras);

	/**根据条件查询
	 * @param paras
	 * @param page
	 * @return
	 */
	List<UserMsg> selectList(@Param("paras") UserMsg paras, @Param("page") Page page);

	/**标记类型为已读
	 * @param partyId
	 * @param msgType
	 * @return
	 */
	int readMsg(@Param("partyId") Long partyId, @Param("msgType")String msgType);
}
