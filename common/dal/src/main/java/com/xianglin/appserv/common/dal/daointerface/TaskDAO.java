package com.xianglin.appserv.common.dal.daointerface;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xianglin.appserv.common.dal.dataobject.Task;


public interface TaskDAO {

	/**
	 * 更新任务状态为执行中
	 * 
	 * 
	 * @param status 任务状态
	 * @param taskId 任务Id
	 * @param excuteDate 任务执行时间
	 * @return
	 */
    int updateExcutedByTaskId(@Param("status") String status,@Param("taskId") String taskId,@Param("executeDate") String executeDate);

    /**
	 * 任务执行完毕，更新任务状态下一个工作日待执行
	 * 
	 * 
	 * @param status 任务状态
	 * @param taskId 任务Id
	 * @param excuteDate 任务执行时间
	 * @param nextExcuteDate 下次任务执行时间
	 * @return
	 */
    int updateEndByTaskId(@Param("status") String status,@Param("taskId") String taskId,@Param("executeDate") String executeDate,@Param("nextExecuteDate") String nextExecuteDate);

    /**
     * 
     * 
     * 
     * @param map
     * @return
     * @throws Exception
     */
    Task getTaskByTaskId(Map map) throws Exception;
    /**
     * 
     * 
     * 
     * @param task
     * @return
     * @throws Exception
     */
    int updateEntity(Task task);
}