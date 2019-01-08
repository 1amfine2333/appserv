package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xianglin.appserv.common.dal.dataobject.TaskDetailDO;

public interface TaskDetailDAO {

	/**
	 * 根据主机名称查询
	 * 
	 * 
	 * @param hostname
	 * @param status
	 * @return
	 */
	TaskDetailDO getByHostNameAndStatus(@Param("hostName") String hostName,@Param("status") String status);

    /**
     * 查询全部
     * 
     * 
     * @return
     */
	List<TaskDetailDO> getAllTaskDetailDO();
    
    /**
     * 更新状态
     * 
     * 
     * @param hostname
     * @param status
     * @return
     */
    int updateStatusByHostName(@Param("hostName")String hostName, @Param("status") String status);
    
    /**
     * 更新任务：任务拆分后初始化子任务调用
     * 
     * 
     * @param taskDetailDO
     * @return
     */
    int updateTaskByHostName(TaskDetailDO taskDetailDO);
}