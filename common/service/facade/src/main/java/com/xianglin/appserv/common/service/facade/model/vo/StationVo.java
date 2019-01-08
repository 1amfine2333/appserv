package com.xianglin.appserv.common.service.facade.model.vo;

import java.math.BigDecimal;
import java.util.Date;


public class StationVo extends BaseVo{
	private static final long serialVersionUID = -1625208023909170524L;
	/**
     * 发电站的站点id
     */
	private String stationId;
	/**
     * 设备id
     */
    private String inverterId;

    /**
     * 状态
     */
    private String status;

    /**
     * 即时功率
     */
    private BigDecimal power;

    /**
     * 当天发电
     */
    private BigDecimal eday;

    /**
     * 累计发电
     */
    private BigDecimal etotal;

    /**
     * 累计工时
     */
    private BigDecimal htotal;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 站点名称
     */
    private String name;

    /**
     * 装机容量
     */
    private BigDecimal capacity;

    /**
     * 建造日期
     */
    private Date creationDate;

    /**
     * 地址
     */
    private String address;

    /**
     * 上次数据时点
     */
    private Date lastDataTime;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 备注信息
     */
    private String comments;
    
    /**
     * 总用电量
     */
    private String etotalAmount;

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getInverterId() {
		return inverterId;
	}

	public void setInverterId(String inverterId) {
		this.inverterId = inverterId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getPower() {
		return power;
	}

	public void setPower(BigDecimal power) {
		this.power = power;
	}

	public BigDecimal getEday() {
		return eday;
	}

	public void setEday(BigDecimal eday) {
		this.eday = eday;
	}

	public BigDecimal getEtotal() {
		return etotal;
	}

	public void setEtotal(BigDecimal etotal) {
		this.etotal = etotal;
	}

	public BigDecimal getHtotal() {
		return htotal;
	}

	public void setHtotal(BigDecimal htotal) {
		this.htotal = htotal;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getLastDataTime() {
		return lastDataTime;
	}

	public void setLastDataTime(Date lastDataTime) {
		this.lastDataTime = lastDataTime;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEtotalAmount() {
		return etotalAmount;
	}

	public void setEtotalAmount(String etotalAmount) {
		this.etotalAmount = etotalAmount;
	}

}
