package com.xianglin.appserv.biz.shared.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.common.dal.daointerface.TaskDAO;
import com.xianglin.appserv.common.dal.dataobject.Task;
/**
 * 定时任务集群控制
 *
 *  
 */
public class TimeTaskClusterUtil {
	/** logger */
	private static final Logger logger = LoggerFactory
			.getLogger(TimeTaskClusterUtil.class);

	/**
	 * 
	 * 
	 * 
	 * @param taskDAO
	 * @param taskId
	 * @param intervalTime 秒
	 * @return
	 * @throws Exception
	 */
	public static boolean isHasClusterExceute(TaskDAO taskDAO,String taskId, int intervalTime)
			throws Exception {
		logger.info("---------------begin check cluster {} execute status--------------",taskId); 
		Map<String, String> param = new HashMap<String, String>();
		param.put("taskId", taskId);
		Task task = taskDAO.getTaskByTaskId(param);
		if(task == null){
			logger.info("------task has cancled because status------------");
			return true;
		}
		logger.info("------taskExecute-------getVersion------------=ThreadId:"
				+ Thread.currentThread().getId() + "  version="
				+ task.getVersion());
		Long nowTimeStamp = System.currentTimeMillis() / 1000;
		long lastLimeStamp = task.getTimeStamp();
		logger.info("------nowTimeStamp-lastLimeStamp ="
				+ (nowTimeStamp - lastLimeStamp));

		if (nowTimeStamp - lastLimeStamp < intervalTime) {// 间隔一分钟内执行，认为是集群下的重复执行，放弃该任务
			logger.info("-------taskExecute can not execute because the timestamp!!! ---------{}",taskId);
			return true;
		}
		task.setTimeStamp(nowTimeStamp);
		task.setUpdateTime(new Date());
		int result = taskDAO.updateEntity(task);
		if (result < 1) {// 基于乐观锁的机制，如果更新失败，认为该任务已经执行过。
			logger.info("-------taskExecute can not execute because version !!!--------------{}",taskId);
			return true;
		}
		logger.info("---------------cluster check pass!,taskId:{}-------------------" ,taskId);
		return false;

	}
	
}
