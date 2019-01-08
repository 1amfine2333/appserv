/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @author lengqiuhao 2016年10月31日下午3:06:52
 */
public class TotalSolarDataVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 8750887818702740648L;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 站点id
	 */
	private Long nodePartyId;
	/**
	 * 网店区域
	 */
	private String nodeCode;
	/**
	 * 数据监测网站站点id
	 */
	private String stationId;
	/**
	 * 实时功率
	 */
	private BigDecimal power=new BigDecimal(0.00);
	/**
	 * 当日累计收益
	 */
	private BigDecimal tdIncome=new BigDecimal(0.00);
	/**
	 * 当日累计减排
	 */
	private BigDecimal tdCo2=new BigDecimal(0.00);
	/**
	 * 当日累计种植
	 */
	private BigDecimal tdTotalPlant=new BigDecimal(0.00);
	/**
	 * 当日累计累计发电
	 */
	private BigDecimal eday=new BigDecimal(0.00);
	/**
	 * 累计发电
	 */
	private BigDecimal etotal=new BigDecimal(0.00);
	/**
	 * 累计收益
	 */
	private BigDecimal income=new BigDecimal(0.00);;
	/**
	 * 累计减排
	 */
	private BigDecimal co2=new BigDecimal(0.00);
	/**
	 * 累计种植
	 */
	private BigDecimal totalPlant=new BigDecimal(0.00);
	 /**
     * 站点名称
     */
    private String name;
    /**
     * 设备状态
     */
    private String status;
    /**
     * 装机容量
     */
    private BigDecimal capacity=new BigDecimal(0.00);

    /**
     * 建造日期
     */
    private Date creationDate;

    /**
     * 地址
     */
    private String address;

    /**
     * 电价
     */
    private BigDecimal price;

    /**
     * 所属账户id
     */
    private Long accountId;

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
	 * @return the income
	 */
	public BigDecimal getIncome() {
		return income;
	}
	/**
	 * @param income the income to set
	 */
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	/**
	 * @return the co2
	 */
	public BigDecimal getCo2() {
		return co2;
	}
	/**
	 * @param co2 the co2 to set
	 */
	public void setCo2(BigDecimal co2) {
		this.co2 = co2;
	}
	
	
	
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the nodePartyId
	 */
	public Long getNodePartyId() {
		return nodePartyId;
	}
	/**
	 * @param nodePartyId the nodePartyId to set
	 */
	public void setNodePartyId(Long nodePartyId) {
		this.nodePartyId = nodePartyId;
	}
	/**
	 * @return the nodeCode
	 */
	public String getNodeCode() {
		return nodeCode;
	}
	/**
	 * @param nodeCode the nodeCode to set
	 */
	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}
	/**
	 * @return the tdIncome
	 */
	public BigDecimal getTdIncome() {
		return tdIncome;
	}
	/**
	 * @param tdIncome the tdIncome to set
	 */
	public void setTdIncome(BigDecimal tdIncome) {
		this.tdIncome = tdIncome;
	}
	/**
	 * @return the tdCo2
	 */
	public BigDecimal getTdCo2() {
		return tdCo2;
	}
	/**
	 * @param tdCo2 the tdCo2 to set
	 */
	public void setTdCo2(BigDecimal tdCo2) {
		this.tdCo2 = tdCo2;
	}
	
	
	/**
	 * @return the etotal
	 */
	public BigDecimal getEtotal() {
		return etotal;
	}
	/**
	 * @param etotal the etotal to set
	 */
	public void setEtotal(BigDecimal etotal) {
		this.etotal = etotal;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the capacity
	 */
	public BigDecimal getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return the lastDataTime
	 */
	public Date getLastDataTime() {
		return lastDataTime;
	}
	/**
	 * @param lastDataTime the lastDataTime to set
	 */
	public void setLastDataTime(Date lastDataTime) {
		this.lastDataTime = lastDataTime;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the eday
	 */
	public BigDecimal getEday() {
		return eday;
	}
	/**
	 * @param eday the eday to set
	 */
	public void setEday(BigDecimal eday) {
		this.eday = eday;
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
	 * @return the tdTotalPlant
	 */
	public BigDecimal getTdTotalPlant() {
		return tdTotalPlant;
	}
	/**
	 * @param tdTotalPlant the tdTotalPlant to set
	 */
	public void setTdTotalPlant(BigDecimal tdTotalPlant) {
		this.tdTotalPlant = tdTotalPlant;
	}
	/**
	 * @return the totalPlant
	 */
	public BigDecimal getTotalPlant() {
		return totalPlant;
	}
	/**
	 * @param totalPlant the totalPlant to set
	 */
	public void setTotalPlant(BigDecimal totalPlant) {
		this.totalPlant = totalPlant;
	}
	
	/**
	 * @return the stationId
	 */
	public String getStationId() {
		return stationId;
	}
	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	/**
	 * @return the power
	 */
	public BigDecimal getPower() {
		return power;
	}
	/**
	 * @param power the power to set
	 */
	public void setPower(BigDecimal power) {
		this.power = power;
	}
	
	
}
