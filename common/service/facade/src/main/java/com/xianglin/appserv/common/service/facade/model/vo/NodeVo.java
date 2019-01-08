package com.xianglin.appserv.common.service.facade.model.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;

public class NodeVo extends BaseVo {

	private static final long serialVersionUID = -2543668644677401072L;

	private Long id;

	private Long nodePartyId;

	private String nodeCode;

	private String nodeCodeOld;

	private String nodeName;

	private String nodeAddress;

	private String companyTcode;

	private BigDecimal gpsLat;

	private BigDecimal gpsLong;

	private String geoHash;

	private Long nodeVillageId;

	private String districtCode;

	private Long nodeManagerPartyId;

	private String nodeManagerName;

	private Long countyManagerId;

	private String countyManagerName;

	private Long marketingManagerId;

	private String marketingManagerName;

	private Long generalManagerId;

	private String generalManagerName;

	private String referenceSource;

	private String referenceName;

	private String referencePhone;

	private Date referenceDatetime;

	private String countyManagerOpinion;

	private String marketingManagerOpinion;

	private String generalManagerOpinion;

	private String selectionStatus;

	private String isSigningContract;

	private String depositorInfoGatherStatus;

	private String decorationStatus;

	private String advertisementStatus;

	private String furnitureStatus;

	private String facilityStatus;

	private String checkAcceptStatus;

	private String bankBusiness;

	private String loanBusiness;

	private String eBusiness;

	private String trueName;

	private String mobilePhone;

	private String credentialsNumber;

	private Date lastUpdate;

	private Integer precision;// GEOHASH 精度

	private Double distance;// 网点离当前点的距离

	private Integer followQwNumber;

	/** 监控开始时间，时分各2位，0830表示8点30 */
	private String monitorStartTime;

	/** 监控开始时间，时分各2位，2130表示21点30 */
	private String monitorEndTime;

	private Integer standardNumber;

	private String ryToken;
	
	private String userType = UserType.visitor.name();

	public String getTrueName() {

		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCredentialsNumber() {
		return credentialsNumber;
	}

	public void setCredentialsNumber(String credentialsNumber) {
		this.credentialsNumber = credentialsNumber;
	}

	/** 加盟协议编号 */
	private String joinContractNumber;

	private Date signDate;

	private String signDateToString;

	private Date openDate;

	private Date inspectDate;

	private Date inspectFailDate;

	private String openDateToString;

	private Date revokeDate;

	private String operationStatus;

	private String nodeRank;

	private BigDecimal currentBalance;

	private Integer currentCardCount;

	private Date currentImportDate;

	private BigDecimal maxBalance;

	private Date maxBalanceDate;

	private BigDecimal facilityDeposit;

	private Integer cardCount;

	private Date cardCountDate;

	private Date lastWorkDate;

	private String lastWorkType;

	/** 系统添加的巡查类任务数量 */
	private Integer patrolNumber;

	/** 员工添加的巡查类任务数量 */
	private Integer patrolManualNumber;

	private Integer inspectNumber;

	private Integer eqpworkNumber;

	private Integer buildingNumber;

	private Integer openNumber;

	private Integer revokeNumber;

	private Integer visitNumber;

	private Integer callbackNumber;

	private Integer callback2Number;

	private Integer signNumber;

	private Integer otherworkNumber;

	private String isDeleted;

	private String creator;

	private String updater;

	private Date createDate;

	private Date updateDate;

	private String comments;

	private String districtFullName;

	private String openedBusiness;

	private String balanceRange;

	private String cardCountRange;

	private String fullTreePath;

	/** 网点导出数据CSV */
	private String createDateStr; // 网点创建时间

	private String signDateStr;

	private String revokeDateStr;

	private String lastWorkDateStr;

	private String provinceName; // 省

	private String cityName; // 市

	private String countyName; // 区

	private String townName; // 乡镇

	private String villageName; // 村

	private String bankBusinessStatus; // 银行业务状态

	private Date bankSignufDate; // 银行业务开通时间

	private String bankSignufDateStr;

	private Date bankOpenDate; // 银行业务开业时间

	private String bankOpenDateStr;

	private String BusinessStatus; // 电商业务状态

	private Date BusinessSignufDate; // 电商业务开通时间

	private String BusinessSignufDateStr;

	private Date BusinessOpenDate; // 电商业务开业时间

	private String BusinessOpenDateStr;

	private Date completeDate; // 最近一次巡查任务完成时间

	private String completeDateStr;

	private Date importDate; // 业绩统计时间

	private String importDateStr;

	private BigDecimal balance; // 存款业绩

	private BigDecimal monthAverage; // 月日均

	private BigDecimal yearAverage; // 年日均

	private String companyName; // 所属部门

	// 站点关联字段
	private Long nodeMapId;
	private String nodeMapBankTypeCode;
	private String nodeMapProvinceCode;
	private String nodeMapBankNo;
	private String nodeMapBranchNo;
	private String nodeMapSubbranchNo;
	private String nodeMapHelpNodeCode;
	private String nodeMapHelpNodeName;
	private Long nodeMapNodePartyId;
	private String nodeMapNodeName;
	private String nodeMapIsDeleted;
	private String nodeMapCreator;
	private String nodeMapUpdater;
	private Date nodeMapCreateDate;
	private Date nodeMapUpdateDate;
	private String nodeMapComments;
	private String nodeBusinessBankTypeCode;
	private Long nodeBusinessId;
	
	private String avatar;//站长头像

//	private List<NodeBusinessExtVo> nodeBusinessVoList; // 站点业务数据集
//
//	private List<BankImportNodeMapVo> bankImportNodeMapVoList; // 站点关联数据集
//
//	private List<NodeTagsVo> nodeTagVoList;// 业务标签数据集

	private String tag;// 数据库抽出来的标签

	// 未完成的提醒类任务级别
	private Integer remindNumber;

	/**
	 * @return the createDateStr
	 */
	public String getCreateDateStr() {
		return createDateStr;
	}

	/**
	 * @param createDateStr
	 *            the createDateStr to set
	 */
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	/**
	 * @return the lastWorkDateStr
	 */
	public String getLastWorkDateStr() {
		return lastWorkDateStr;
	}

	/**
	 * @param lastWorkDateStr
	 *            the lastWorkDateStr to set
	 */
	public void setLastWorkDateStr(String lastWorkDateStr) {
		this.lastWorkDateStr = lastWorkDateStr;
	}

	/**
	 * @return the signDateStr
	 */
	public String getSignDateStr() {
		return signDateStr;
	}

	/**
	 * @param signDateStr
	 *            the signDateStr to set
	 */
	public void setSignDateStr(String signDateStr) {
		this.signDateStr = signDateStr;
	}

	/**
	 * @return the revokeDateStr
	 */
	public String getRevokeDateStr() {
		return revokeDateStr;
	}

	/**
	 * @param revokeDateStr
	 *            the revokeDateStr to set
	 */
	public void setRevokeDateStr(String revokeDateStr) {
		this.revokeDateStr = revokeDateStr;
	}

	/**
	 * @return the provinceName
	 */
	public String getProvinceName() {
		return provinceName;
	}

	/**
	 * @param provinceName
	 *            the provinceName to set
	 */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName
	 *            the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the countyName
	 */
	public String getCountyName() {
		return countyName;
	}

	/**
	 * @param countyName
	 *            the countyName to set
	 */
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	/**
	 * @return the townName
	 */
	public String getTownName() {
		return townName;
	}

	/**
	 * @param townName
	 *            the townName to set
	 */
	public void setTownName(String townName) {
		this.townName = townName;
	}

	/**
	 * @return the villageName
	 */
	public String getVillageName() {
		return villageName;
	}

	/**
	 * @param villageName
	 *            the villageName to set
	 */
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	/**
	 * @return the bankBusinessStatus
	 */
	public String getBankBusinessStatus() {
		return bankBusinessStatus;
	}

	/**
	 * @param bankBusinessStatus
	 *            the bankBusinessStatus to set
	 */
	public void setBankBusinessStatus(String bankBusinessStatus) {
		this.bankBusinessStatus = bankBusinessStatus;
	}

	/**
	 * @return the bankSignufDate
	 */
	public Date getBankSignufDate() {
		return bankSignufDate;
	}

	/**
	 * @param bankSignufDate
	 *            the bankSignufDate to set
	 */
	public void setBankSignufDate(Date bankSignufDate) {
		this.bankSignufDate = bankSignufDate;
	}

	/**
	 * @return the bankSignufDateStr
	 */
	public String getBankSignufDateStr() {
		return bankSignufDateStr;
	}

	/**
	 * @param bankSignufDateStr
	 *            the bankSignufDateStr to set
	 */
	public void setBankSignufDateStr(String bankSignufDateStr) {
		this.bankSignufDateStr = bankSignufDateStr;
	}

	/**
	 * @return the bankOpenDate
	 */
	public Date getBankOpenDate() {
		return bankOpenDate;
	}

	/**
	 * @param bankOpenDate
	 *            the bankOpenDate to set
	 */
	public void setBankOpenDate(Date bankOpenDate) {
		this.bankOpenDate = bankOpenDate;
	}

	/**
	 * @return the bankOpenDateStr
	 */
	public String getBankOpenDateStr() {
		return bankOpenDateStr;
	}

	/**
	 * @param bankOpenDateStr
	 *            the bankOpenDateStr to set
	 */
	public void setBankOpenDateStr(String bankOpenDateStr) {
		this.bankOpenDateStr = bankOpenDateStr;
	}

	/**
	 * @return the businessStatus
	 */
	public String getBusinessStatus() {
		return BusinessStatus;
	}

	/**
	 * @param businessStatus
	 *            the businessStatus to set
	 */
	public void setBusinessStatus(String businessStatus) {
		BusinessStatus = businessStatus;
	}

	/**
	 * @return the businessSignufDate
	 */
	public Date getBusinessSignufDate() {
		return BusinessSignufDate;
	}

	/**
	 * @param businessSignufDate
	 *            the businessSignufDate to set
	 */
	public void setBusinessSignufDate(Date businessSignufDate) {
		BusinessSignufDate = businessSignufDate;
	}

	/**
	 * @return the businessSignufDateStr
	 */
	public String getBusinessSignufDateStr() {
		return BusinessSignufDateStr;
	}

	/**
	 * @param businessSignufDateStr
	 *            the businessSignufDateStr to set
	 */
	public void setBusinessSignufDateStr(String businessSignufDateStr) {
		BusinessSignufDateStr = businessSignufDateStr;
	}

	/**
	 * @return the businessOpenDate
	 */
	public Date getBusinessOpenDate() {
		return BusinessOpenDate;
	}

	/**
	 * @param businessOpenDate
	 *            the businessOpenDate to set
	 */
	public void setBusinessOpenDate(Date businessOpenDate) {
		BusinessOpenDate = businessOpenDate;
	}

	/**
	 * @return the businessOpenDateStr
	 */
	public String getBusinessOpenDateStr() {
		return BusinessOpenDateStr;
	}

	/**
	 * @param businessOpenDateStr
	 *            the businessOpenDateStr to set
	 */
	public void setBusinessOpenDateStr(String businessOpenDateStr) {
		BusinessOpenDateStr = businessOpenDateStr;
	}

	/**
	 * @return the completeDate
	 */
	public Date getCompleteDate() {
		return completeDate;
	}

	/**
	 * @param completeDate
	 *            the completeDate to set
	 */
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	/**
	 * @return the completeDateStr
	 */
	public String getCompleteDateStr() {
		return completeDateStr;
	}

	/**
	 * @param completeDateStr
	 *            the completeDateStr to set
	 */
	public void setCompleteDateStr(String completeDateStr) {
		this.completeDateStr = completeDateStr;
	}

	/**
	 * @return the importDate
	 */
	public Date getImportDate() {
		return importDate;
	}

	/**
	 * @param importDate
	 *            the importDate to set
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	/**
	 * @return the importDateStr
	 */
	public String getImportDateStr() {
		return importDateStr;
	}

	/**
	 * @param importDateStr
	 *            the importDateStr to set
	 */
	public void setImportDateStr(String importDateStr) {
		this.importDateStr = importDateStr;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the monthAverage
	 */
	public BigDecimal getMonthAverage() {
		return monthAverage;
	}

	/**
	 * @param monthAverage
	 *            the monthAverage to set
	 */
	public void setMonthAverage(BigDecimal monthAverage) {
		this.monthAverage = monthAverage;
	}

	/**
	 * @return the yearAverage
	 */
	public BigDecimal getYearAverage() {
		return yearAverage;
	}

	/**
	 * @param yearAverage
	 *            the yearAverage to set
	 */
	public void setYearAverage(BigDecimal yearAverage) {
		this.yearAverage = yearAverage;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFullTreePath() {
		return fullTreePath;
	}

	public void setFullTreePath(String fullTreePath) {
		this.fullTreePath = fullTreePath;
	}

	public String getCardCountRange() {
		return cardCountRange;
	}

	public void setCardCountRange(String cardCountRange) {
		this.cardCountRange = cardCountRange;
	}

	private String uncompletedWork;

	private String bankStatus;

	public String getBankStatus() {
		return bankStatus;
	}

	public void setBankStatus(String bankStatus) {
		this.bankStatus = bankStatus;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public String geteStatus() {
		return eStatus;
	}

	public void seteStatus(String eStatus) {
		this.eStatus = eStatus;
	}

	private String loanStatus;

	private String eStatus;

	public String getJoinContractNumber() {
		return joinContractNumber;
	}

	public void setJoinContractNumber(String joinContractNumber) {
		this.joinContractNumber = joinContractNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNodePartyId() {
		return nodePartyId;
	}

	public void setNodePartyId(Long nodePartyId) {
		this.nodePartyId = nodePartyId;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getNodeCodeOld() {
		return nodeCodeOld;
	}

	public void setNodeCodeOld(String nodeCodeOld) {
		this.nodeCodeOld = nodeCodeOld;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeAddress() {
		return nodeAddress;
	}

	public void setNodeAddress(String nodeAddress) {
		this.nodeAddress = nodeAddress;
	}

	public String getCompanyTcode() {
		return companyTcode;
	}

	public void setCompanyTcode(String companyTcode) {
		this.companyTcode = companyTcode;
	}

	public BigDecimal getGpsLat() {
		return gpsLat;
	}

	public void setGpsLat(BigDecimal gpsLat) {
		this.gpsLat = gpsLat;
	}

	public BigDecimal getGpsLong() {
		return gpsLong;
	}

	public void setGpsLong(BigDecimal gpsLong) {
		this.gpsLong = gpsLong;
	}

	public String getGeoHash() {
		return geoHash;
	}

	public void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}

	public Long getNodeVillageId() {
		return nodeVillageId;
	}

	public void setNodeVillageId(Long nodeVillageId) {
		this.nodeVillageId = nodeVillageId;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public Long getNodeManagerPartyId() {
		return nodeManagerPartyId;
	}

	public void setNodeManagerPartyId(Long nodeManagerPartyId) {
		this.nodeManagerPartyId = nodeManagerPartyId;
	}

	public String getNodeManagerName() {
		return nodeManagerName;
	}

	public void setNodeManagerName(String nodeManagerName) {
		this.nodeManagerName = nodeManagerName;
	}

	public Long getCountyManagerId() {
		return countyManagerId;
	}

	public void setCountyManagerId(Long countyManagerId) {
		this.countyManagerId = countyManagerId;
	}

	public String getCountyManagerName() {
		return countyManagerName;
	}

	public void setCountyManagerName(String countyManagerName) {
		this.countyManagerName = countyManagerName;
	}

	public Long getMarketingManagerId() {
		return marketingManagerId;
	}

	public void setMarketingManagerId(Long marketingManagerId) {
		this.marketingManagerId = marketingManagerId;
	}

	public String getMarketingManagerName() {
		return marketingManagerName;
	}

	public void setMarketingManagerName(String marketingManagerName) {
		this.marketingManagerName = marketingManagerName;
	}

	public Long getGeneralManagerId() {
		return generalManagerId;
	}

	public void setGeneralManagerId(Long generalManagerId) {
		this.generalManagerId = generalManagerId;
	}

	public String getGeneralManagerName() {
		return generalManagerName;
	}

	public void setGeneralManagerName(String generalManagerName) {
		this.generalManagerName = generalManagerName;
	}

	public String getReferenceSource() {
		return referenceSource;
	}

	public void setReferenceSource(String referenceSource) {
		this.referenceSource = referenceSource;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getReferencePhone() {
		return referencePhone;
	}

	public void setReferencePhone(String referencePhone) {
		this.referencePhone = referencePhone;
	}

	public Date getReferenceDatetime() {
		return referenceDatetime;
	}

	public void setReferenceDatetime(Date referenceDatetime) {
		this.referenceDatetime = referenceDatetime;
	}

	public String getCountyManagerOpinion() {
		return countyManagerOpinion;
	}

	public void setCountyManagerOpinion(String countyManagerOpinion) {
		this.countyManagerOpinion = countyManagerOpinion;
	}

	public String getMarketingManagerOpinion() {
		return marketingManagerOpinion;
	}

	public void setMarketingManagerOpinion(String marketingManagerOpinion) {
		this.marketingManagerOpinion = marketingManagerOpinion;
	}

	public String getGeneralManagerOpinion() {
		return generalManagerOpinion;
	}

	public void setGeneralManagerOpinion(String generalManagerOpinion) {
		this.generalManagerOpinion = generalManagerOpinion;
	}

	public String getSelectionStatus() {
		return selectionStatus;
	}

	public void setSelectionStatus(String selectionStatus) {
		this.selectionStatus = selectionStatus;
	}

	public String getIsSigningContract() {
		return isSigningContract;
	}

	public void setIsSigningContract(String isSigningContract) {
		this.isSigningContract = isSigningContract;
	}

	public String getDepositorInfoGatherStatus() {
		return depositorInfoGatherStatus;
	}

	public void setDepositorInfoGatherStatus(String depositorInfoGatherStatus) {
		this.depositorInfoGatherStatus = depositorInfoGatherStatus;
	}

	public String getDecorationStatus() {
		return decorationStatus;
	}

	public void setDecorationStatus(String decorationStatus) {
		this.decorationStatus = decorationStatus;
	}

	public String getAdvertisementStatus() {
		return advertisementStatus;
	}

	public void setAdvertisementStatus(String advertisementStatus) {
		this.advertisementStatus = advertisementStatus;
	}

	public String getFurnitureStatus() {
		return furnitureStatus;
	}

	public void setFurnitureStatus(String furnitureStatus) {
		this.furnitureStatus = furnitureStatus;
	}

	public String getFacilityStatus() {
		return facilityStatus;
	}

	public void setFacilityStatus(String facilityStatus) {
		this.facilityStatus = facilityStatus;
	}

	public String getCheckAcceptStatus() {
		return checkAcceptStatus;
	}

	public void setCheckAcceptStatus(String checkAcceptStatus) {
		this.checkAcceptStatus = checkAcceptStatus;
	}

	public String getBankBusiness() {
		return bankBusiness;
	}

	public void setBankBusiness(String bankBusiness) {
		this.bankBusiness = bankBusiness;
	}

	public String getLoanBusiness() {
		return loanBusiness;
	}

	public void setLoanBusiness(String loanBusiness) {
		this.loanBusiness = loanBusiness;
	}

	public String geteBusiness() {
		return eBusiness;
	}

	public Integer getFollowQwNumber() {
		return followQwNumber;
	}

	public void setFollowQwNumber(Integer followQwNumber) {
		this.followQwNumber = followQwNumber;
	}

	/**
	 * 前端传undefined， 导致入库字段超长， 要求前端也做修改，现在VO层面过滤一下。
	 * 
	 * @param eBusiness
	 */
	public void seteBusiness(String eBusiness) {
		this.eBusiness = eBusiness;
		if ("undefined".equalsIgnoreCase(this.eBusiness))
			this.eBusiness = "";

	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	/**
	 * @return the inspectDate
	 */
	public Date getInspectDate() {
		return inspectDate;
	}

	/**
	 * @param inspectDate
	 *            the inspectDate to set
	 */
	public void setInspectDate(Date inspectDate) {
		this.inspectDate = inspectDate;
	}

	/**
	 * @return the inspectFailDate
	 */
	public Date getInspectFailDate() {
		return inspectFailDate;
	}

	/**
	 * @param inspectFailDate
	 *            the inspectFailDate to set
	 */
	public void setInspectFailDate(Date inspectFailDate) {
		this.inspectFailDate = inspectFailDate;
	}

	public Date getRevokeDate() {
		return revokeDate;
	}

	public void setRevokeDate(Date revokeDate) {
		this.revokeDate = revokeDate;
	}

	public String getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}

	public String getNodeRank() {
		return nodeRank;
	}

	public void setNodeRank(String nodeRank) {
		this.nodeRank = nodeRank;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Integer getCurrentCardCount() {
		return currentCardCount;
	}

	public void setCurrentCardCount(Integer currentCardCount) {
		this.currentCardCount = currentCardCount;
	}

	public Date getCurrentImportDate() {
		return currentImportDate;
	}

	public void setCurrentImportDate(Date currentImportDate) {
		this.currentImportDate = currentImportDate;
	}

	public BigDecimal getMaxBalance() {
		return maxBalance;
	}

	public void setMaxBalance(BigDecimal maxBalance) {
		this.maxBalance = maxBalance;
	}

	public Date getMaxBalanceDate() {
		return maxBalanceDate;
	}

	public void setMaxBalanceDate(Date maxBalanceDate) {
		this.maxBalanceDate = maxBalanceDate;
	}

	public BigDecimal getFacilityDeposit() {
		return facilityDeposit;
	}

	public void setFacilityDeposit(BigDecimal facilityDeposit) {
		this.facilityDeposit = facilityDeposit;
	}

	public Integer getCardCount() {
		return cardCount;
	}

	public void setCardCount(Integer cardCount) {
		this.cardCount = cardCount;
	}

	public Date getCardCountDate() {
		return cardCountDate;
	}

	public void setCardCountDate(Date cardCountDate) {
		this.cardCountDate = cardCountDate;
	}

	public Integer getPatrolNumber() {
		return patrolNumber;
	}

	public void setPatrolNumber(Integer patrolNumber) {
		this.patrolNumber = patrolNumber;
	}

	public Integer getPatrolManualNumber() {
		return patrolManualNumber;
	}

	public void setPatrolManualNumber(Integer patrolManualNumber) {
		this.patrolManualNumber = patrolManualNumber;
	}

	public Integer getInspectNumber() {
		return inspectNumber;
	}

	public void setInspectNumber(Integer inspectNumber) {
		this.inspectNumber = inspectNumber;
	}

	public Integer getEqpworkNumber() {
		return eqpworkNumber;
	}

	public void setEqpworkNumber(Integer eqpworkNumber) {
		this.eqpworkNumber = eqpworkNumber;
	}

	public Integer getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(Integer buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public Integer getOpenNumber() {
		return openNumber;
	}

	public void setOpenNumber(Integer openNumber) {
		this.openNumber = openNumber;
	}

	public Integer getRevokeNumber() {
		return revokeNumber;
	}

	public void setRevokeNumber(Integer revokeNumber) {
		this.revokeNumber = revokeNumber;
	}

	public Integer getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(Integer visitNumber) {
		this.visitNumber = visitNumber;
	}

	public Integer getCallbackNumber() {
		return callbackNumber;
	}

	public void setCallbackNumber(Integer callbackNumber) {
		this.callbackNumber = callbackNumber;
	}

	public Integer getCallback2Number() {
		return callback2Number;
	}

	public void setCallback2Number(Integer callback2Number) {
		this.callback2Number = callback2Number;
	}

	public Integer getSignNumber() {
		return signNumber;
	}

	public void setSignNumber(Integer signNumber) {
		this.signNumber = signNumber;
	}

	public Integer getOtherworkNumber() {
		return otherworkNumber;
	}

	public void setOtherworkNumber(Integer otherworkNumber) {
		this.otherworkNumber = otherworkNumber;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
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

	public String getSignDateToString() {
		return signDateToString;
	}

	public void setSignDateToString(String signDateToString) {
		this.signDateToString = signDateToString;
	}

	public String getOpenDateToString() {
		return openDateToString;
	}

	public void setOpenDateToString(String openDateToString) {
		this.openDateToString = openDateToString;
	}

	public String getDistrictFullName() {
		return districtFullName;
	}

	public void setDistrictFullName(String districtFullName) {
		this.districtFullName = districtFullName;
	}

	public String getOpenedBusiness() {
		return openedBusiness;
	}

	public void setOpenedBusiness(String openedBusiness) {
		this.openedBusiness = openedBusiness;
	}

	public String getBalanceRange() {
		return balanceRange;
	}

	public void setBalanceRange(String balanceRange) {
		this.balanceRange = balanceRange;
	}

	public String getUncompletedWork() {
		return uncompletedWork;
	}

	public void setUncompletedWork(String uncompletedWork) {
		this.uncompletedWork = uncompletedWork;
	}

	public Date getLastWorkDate() {
		return lastWorkDate;
	}

	public void setLastWorkDate(Date lastWorkDate) {
		this.lastWorkDate = lastWorkDate;
	}

	public String getLastWorkType() {
		return lastWorkType;
	}

	public void setLastWorkType(String lastWorkType) {
		this.lastWorkType = lastWorkType;
	}

	/**
	 * @return the precision
	 */
	public Integer getPrecision() {
		return precision;
	}

	/**
	 * @param precision
	 *            the precision to set
	 */
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	/**
	 * @return the distance
	 */
	public Double getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}

//	public List<NodeBusinessExtVo> getNodeBusinessVoList() {
//		return nodeBusinessVoList;
//	}
//
//	public void setNodeBusinessVoList(List<NodeBusinessExtVo> nodeBusinessVoList) {
//		this.nodeBusinessVoList = nodeBusinessVoList;
//	}
//
//	public List<BankImportNodeMapVo> getBankImportNodeMapVoList() {
//		return bankImportNodeMapVoList;
//	}
//
//	public void setBankImportNodeMapVoList(List<BankImportNodeMapVo> bankImportNodeMapVoList) {
//		this.bankImportNodeMapVoList = bankImportNodeMapVoList;
//	}

	public Long getNodeMapId() {
		return nodeMapId;
	}

	public void setNodeMapId(Long nodeMapId) {
		this.nodeMapId = nodeMapId;
	}

	public String getNodeMapBankTypeCode() {
		return nodeMapBankTypeCode;
	}

	public void setNodeMapBankTypeCode(String nodeMapBankTypeCode) {
		this.nodeMapBankTypeCode = nodeMapBankTypeCode;
	}

	public String getNodeMapProvinceCode() {
		return nodeMapProvinceCode;
	}

	public void setNodeMapProvinceCode(String nodeMapProvinceCode) {
		this.nodeMapProvinceCode = nodeMapProvinceCode;
	}

	public String getNodeMapBankNo() {
		return nodeMapBankNo;
	}

	public void setNodeMapBankNo(String nodeMapBankNo) {
		this.nodeMapBankNo = nodeMapBankNo;
	}

	public String getNodeMapBranchNo() {
		return nodeMapBranchNo;
	}

	public void setNodeMapBranchNo(String nodeMapBranchNo) {
		this.nodeMapBranchNo = nodeMapBranchNo;
	}

	public String getNodeMapSubbranchNo() {
		return nodeMapSubbranchNo;
	}

	public void setNodeMapSubbranchNo(String nodeMapSubbranchNo) {
		this.nodeMapSubbranchNo = nodeMapSubbranchNo;
	}

	public String getNodeMapHelpNodeCode() {
		return nodeMapHelpNodeCode;
	}

	public void setNodeMapHelpNodeCode(String nodeMapHelpNodeCode) {
		this.nodeMapHelpNodeCode = nodeMapHelpNodeCode;
	}

	public String getNodeMapHelpNodeName() {
		return nodeMapHelpNodeName;
	}

	public void setNodeMapHelpNodeName(String nodeMapHelpNodeName) {
		this.nodeMapHelpNodeName = nodeMapHelpNodeName;
	}

	public Long getNodeMapNodePartyId() {
		return nodeMapNodePartyId;
	}

	public void setNodeMapNodePartyId(Long nodeMapNodePartyId) {
		this.nodeMapNodePartyId = nodeMapNodePartyId;
	}

	public String getNodeMapNodeName() {
		return nodeMapNodeName;
	}

	public void setNodeMapNodeName(String nodeMapNodeName) {
		this.nodeMapNodeName = nodeMapNodeName;
	}

	public String getNodeMapIsDeleted() {
		return nodeMapIsDeleted;
	}

	public void setNodeMapIsDeleted(String nodeMapIsDeleted) {
		this.nodeMapIsDeleted = nodeMapIsDeleted;
	}

	public String getNodeMapCreator() {
		return nodeMapCreator;
	}

	public void setNodeMapCreator(String nodeMapCreator) {
		this.nodeMapCreator = nodeMapCreator;
	}

	public String getNodeMapUpdater() {
		return nodeMapUpdater;
	}

	public void setNodeMapUpdater(String nodeMapUpdater) {
		this.nodeMapUpdater = nodeMapUpdater;
	}

	public Date getNodeMapCreateDate() {
		return nodeMapCreateDate;
	}

	public void setNodeMapCreateDate(Date nodeMapCreateDate) {
		this.nodeMapCreateDate = nodeMapCreateDate;
	}

	public Date getNodeMapUpdateDate() {
		return nodeMapUpdateDate;
	}

	public void setNodeMapUpdateDate(Date nodeMapUpdateDate) {
		this.nodeMapUpdateDate = nodeMapUpdateDate;
	}

	public String getNodeMapComments() {
		return nodeMapComments;
	}

	public void setNodeMapComments(String nodeMapComments) {
		this.nodeMapComments = nodeMapComments;
	}

	public String getNodeBusinessBankTypeCode() {
		return nodeBusinessBankTypeCode;
	}

	public void setNodeBusinessBankTypeCode(String nodeBusinessBankTypeCode) {
		this.nodeBusinessBankTypeCode = nodeBusinessBankTypeCode;
	}

	public Long getNodeBusinessId() {
		return nodeBusinessId;
	}

	public void setNodeBusinessId(Long nodeBusinessId) {
		this.nodeBusinessId = nodeBusinessId;
	}

	public Integer getRemindNumber() {
		return remindNumber;
	}

	public void setRemindNumber(Integer remindNumber) {
		this.remindNumber = remindNumber;
	}

	public Integer getStandardNumber() {
		return standardNumber;
	}

	public void setStandardNumber(Integer standardNumber) {
		this.standardNumber = standardNumber;
	}

//	public List<NodeTagsVo> getNodeTagVoList() {
//		return nodeTagVoList;
//	}
//
//	public void setNodeTagVoList(List<NodeTagsVo> nodeTagVoList) {
//		this.nodeTagVoList = nodeTagVoList;
//	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate
	 *            the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return the monitorStartTime
	 */
	public String getMonitorStartTime() {
		return monitorStartTime;
	}

	/**
	 * @param monitorStartTime
	 *            the monitorStartTime to set
	 */
	public void setMonitorStartTime(String monitorStartTime) {
		this.monitorStartTime = monitorStartTime;
	}

	/**
	 * @return the monitorEndTime
	 */
	public String getMonitorEndTime() {
		return monitorEndTime;
	}

	/**
	 * @param monitorEndTime
	 *            the monitorEndTime to set
	 */
	public void setMonitorEndTime(String monitorEndTime) {
		this.monitorEndTime = monitorEndTime;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getRyToken() {
		return ryToken;
	}

	public void setRyToken(String ryToken) {
		this.ryToken = ryToken;
	}
}
