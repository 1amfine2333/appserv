package com.xianglin.appserv.common.dal.dataobject;

import java.util.Date;


/**
 * 任务执行
 * @author zy
 *
 */
public class Task {
	
	/**
	 *  id
	 */
	private Integer id;
	/**
	 * 任务id
	 */
	private String taskId;
	 
	
	/**
	 *  任务 名称
	 */
	private Integer version;
	
	
	private String taskName;
	/**
	 * 任务状态
	 */
	private String status; 
	
	
	
    private Date createTime;
	
	private Date updateTime;
	
	private Long timeStamp;
	
	private String executeDate;
	
	public String getExecuteDate() {
		return executeDate;
	}
	public void setExecuteDate(String executeDate) {
		this.executeDate = executeDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
 
	
	
}
