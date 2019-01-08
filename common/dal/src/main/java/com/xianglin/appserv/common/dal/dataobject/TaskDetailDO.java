package com.xianglin.appserv.common.dal.dataobject;

public class TaskDetailDO extends BaseDO{
	
	/**自增ID*/
    private Long id;

    /**主机名称*/
    private String hostName;

    /**任务状态*/
    private String status;

    /**开始行数*/
    private Long startNum;

    /**截止行数*/
    private Long endNum;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the startNum
	 */
	public Long getStartNum() {
		return startNum;
	}

	/**
	 * @param startNum the startNum to set
	 */
	public void setStartNum(Long startNum) {
		this.startNum = startNum;
	}

	/**
	 * @return the endNum
	 */
	public Long getEndNum() {
		return endNum;
	}

	/**
	 * @param endNum the endNum to set
	 */
	public void setEndNum(Long endNum) {
		this.endNum = endNum;
	}

}