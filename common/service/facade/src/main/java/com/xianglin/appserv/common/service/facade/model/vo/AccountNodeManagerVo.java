package com.xianglin.appserv.common.service.facade.model.vo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.xianglin.appserv.common.service.facade.model.UserInfoDTO;

public class AccountNodeManagerVo extends BaseVo {

	private static final long serialVersionUID = 8661169782845412265L;

	private Long id;

	private String accountName;

	private Long partyId;

	private String xid;

	private String password;

	private String wechatId;

	private String mobilePhone;

	private String emailAddress;

	private String trueName;

	private String usedName;

	private String gender;

	private String nation;

	private Date birthday;

	private String title;

	private String position;

	private String imageUrl;

	private Long nodePartyId;

	private String telephone;

	private String districtCode;

	private String createInQw; // 在企微中创建联系人结果（描述信息）

	private String followQw; // 是否关注了企微

	private String followQwInPast; // 是否曾经关注了企微

	private String mobileCertificated; // 站长手机号是否经过验证，是/否

	private String existsInQw; // 在企微通讯录中存在状态，可能会多选

	private String isCommit;// 是否确认提交

	private String status;// 微信通讯录中的关注状态,返回给前端做判断

	/**
	 * 日志描述
	 */
	private List<String> logComments;

	/**
	 * 用户角色
	 */
	private String userRole;

	private UserInfoDTO userInfoDTO;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the isCommit
	 */
	public String getIsCommit() {
		return isCommit;
	}

	/**
	 * @param isCommit
	 *            the isCommit to set
	 */
	public void setIsCommit(String isCommit) {
		this.isCommit = isCommit;
	}

	public void setExistsInQw(String existsInQw) {
		this.existsInQw = existsInQw;
	}

	public void setCreateInQw(String createInQw) {
		this.createInQw = createInQw;
	}

	public String getFollowQw() {
		return followQw;
	}

	public void setFollowQw(String followQw) {
		this.followQw = followQw;
	}

	public String getFollowQwInPast() {
		return followQwInPast;
	}

	public void setFollowQwInPast(String followQwInPast) {
		this.followQwInPast = followQwInPast;
	}

	public String getMobileCertificated() {
		return mobileCertificated;
	}

	public void setMobileCertificated(String mobileCertificated) {
		this.mobileCertificated = mobileCertificated;
	}

	public Long getNodePartyId() {

		return nodePartyId;
	}

	public void setNodePartyId(Long nodePartyId) {

		this.nodePartyId = nodePartyId;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {

		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {

		this.imageUrl = imageUrl;
	}

	public String getTitle() {

		return title;
	}

	public void setTitle(String title) {

		this.title = title;
	}

	public String getPosition() {

		return position;
	}

	public void setPosition(String position) {

		this.position = position;
	}

	/**
	 * 前台传过来的出身年月字符串
	 */
	private String birthdayString;

	public String getBirthdayString() {

		return birthdayString;
	}

	public void setBirthdayString(String birthdayString) {
		this.birthdayString = birthdayString;
		try {
			if (null != birthdayString && birthdayString.length() > 0 && !"undefined".equals(birthdayString))
				setBirthday(DateUtils.parseDate(birthdayString, "yyyy-MM-dd"));
		} catch (ParseException e) {
			setBirthday(null);
		}
	}

	private String credentialsType;

	private String credentialsNumber;

	private String educationDegree;

	private String isLocalResidence;

	private String residentialAddress;

	private String livingAddress;

	private String fixedPhone;

	private String jobs;

	private String emergencyContact;

	private String emergencyContactRelation;

	private String emergencyContactPhone;

	private String isPartyMember;

	private String isVillageCadres;

	private String marriedSituation;

	private String isOwnerOccupiedHouse;

	private String landCertificateNumber;

	private String realEstateNumber;

	private String isLoan;

	private String businessPlaceType;

	private BigDecimal leasePeriod;

	private String selectionResult;

	private String selectionRemark;

	private String bankCardOwnerName;

	private String bankCardNumber;

	private String isDeleted;

	private String creator;

	private String updater;

	private Date createDate;

	private Date updateDate;

	private String comments;

	private String operationStatus;// 添加网点状态

	/**
	 * @return the operationStatus
	 */
	public String getOperationStatus() {

		return operationStatus;
	}

	/**
	 * @param operationStatus
	 *            the operationStatus to set
	 */
	public void setOperationStatus(String operationStatus) {

		this.operationStatus = operationStatus;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getAccountName() {

		return accountName;
	}

	public void setAccountName(String accountName) {

		this.accountName = accountName == null ? null : accountName.trim();
	}

	public Long getPartyId() {

		return partyId;
	}

	public void setPartyId(Long partyId) {

		this.partyId = partyId;
	}

	public String getXid() {

		return xid;
	}

	public void setXid(String xid) {

		this.xid = xid == null ? null : xid.trim();
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password == null ? null : password.trim();
	}

	public String getWechatId() {

		return wechatId;
	}

	public void setWechatId(String wechatId) {

		this.wechatId = wechatId == null ? null : wechatId.trim();
	}

	public String getMobilePhone() {

		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {

		this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
	}

	public String getEmailAddress() {

		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {

		this.emailAddress = emailAddress == null ? null : emailAddress.trim();
	}

	public String getTrueName() {

		return trueName;
	}

	public void setTrueName(String trueName) {

		this.trueName = trueName == null ? null : trueName.trim();
	}

	public String getUsedName() {

		return usedName;
	}

	public void setUsedName(String usedName) {

		this.usedName = usedName == null ? null : usedName.trim();
	}

	public String getGender() {

		return gender;
	}

	public void setGender(String gender) {

		this.gender = gender == null ? null : gender.trim();
	}

	public String getNation() {

		return nation;
	}

	public void setNation(String nation) {

		this.nation = nation == null ? null : nation.trim();
	}

	public Date getBirthday() {

		return birthday;
	}

	public void setBirthday(Date birthday) {

		this.birthday = birthday;
	}

	public String getCredentialsType() {

		return credentialsType;
	}

	public void setCredentialsType(String credentialsType) {

		this.credentialsType = credentialsType == null ? null : credentialsType.trim();
	}

	public String getCredentialsNumber() {

		return credentialsNumber;
	}

	public void setCredentialsNumber(String credentialsNumber) {

		this.credentialsNumber = credentialsNumber == null ? null : credentialsNumber.trim();
	}

	public String getEducationDegree() {

		return educationDegree;
	}

	public void setEducationDegree(String educationDegree) {

		this.educationDegree = educationDegree == null ? null : educationDegree.trim();
	}

	public String getIsLocalResidence() {

		return isLocalResidence;
	}

	public void setIsLocalResidence(String isLocalResidence) {

		this.isLocalResidence = isLocalResidence == null ? null : isLocalResidence.trim();
	}

	public String getResidentialAddress() {

		return residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {

		this.residentialAddress = residentialAddress == null ? null : residentialAddress.trim();
	}

	public String getLivingAddress() {

		return livingAddress;
	}

	public void setLivingAddress(String livingAddress) {

		this.livingAddress = livingAddress == null ? null : livingAddress.trim();
	}

	public String getFixedPhone() {

		return fixedPhone;
	}

	public void setFixedPhone(String fixedPhone) {

		this.fixedPhone = fixedPhone == null ? null : fixedPhone.trim();
	}

	public String getJobs() {

		return jobs;
	}

	public void setJobs(String jobs) {

		this.jobs = jobs == null ? null : jobs.trim();
	}

	public String getEmergencyContact() {

		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {

		this.emergencyContact = emergencyContact == null ? null : emergencyContact.trim();
	}

	public String getEmergencyContactRelation() {

		return emergencyContactRelation;
	}

	public void setEmergencyContactRelation(String emergencyContactRelation) {

		this.emergencyContactRelation = emergencyContactRelation == null ? null : emergencyContactRelation.trim();
		if ("undefined".equalsIgnoreCase(this.emergencyContactRelation)) {
			this.emergencyContactRelation = "";
		}

	}

	public String getEmergencyContactPhone() {

		return emergencyContactPhone;
	}

	public void setEmergencyContactPhone(String emergencyContactPhone) {

		this.emergencyContactPhone = emergencyContactPhone == null ? null : emergencyContactPhone.trim();
	}

	public String getIsPartyMember() {

		return isPartyMember;
	}

	public void setIsPartyMember(String isPartyMember) {

		this.isPartyMember = isPartyMember == null ? null : isPartyMember.trim();
	}

	public String getIsVillageCadres() {

		return isVillageCadres;
	}

	public void setIsVillageCadres(String isVillageCadres) {

		this.isVillageCadres = isVillageCadres == null ? null : isVillageCadres.trim();
	}

	public String getMarriedSituation() {

		return marriedSituation;
	}

	public void setMarriedSituation(String marriedSituation) {

		this.marriedSituation = marriedSituation == null ? null : marriedSituation.trim();
	}

	public String getIsOwnerOccupiedHouse() {

		return isOwnerOccupiedHouse;
	}

	public void setIsOwnerOccupiedHouse(String isOwnerOccupiedHouse) {

		this.isOwnerOccupiedHouse = isOwnerOccupiedHouse == null ? null : isOwnerOccupiedHouse.trim();
	}

	public String getLandCertificateNumber() {

		return landCertificateNumber;
	}

	public void setLandCertificateNumber(String landCertificateNumber) {

		this.landCertificateNumber = landCertificateNumber == null ? null : landCertificateNumber.trim();
	}

	public String getRealEstateNumber() {

		return realEstateNumber;
	}

	public void setRealEstateNumber(String realEstateNumber) {

		this.realEstateNumber = realEstateNumber == null ? null : realEstateNumber.trim();
	}

	public String getIsLoan() {

		return isLoan;
	}

	public void setIsLoan(String isLoan) {

		this.isLoan = isLoan == null ? null : isLoan.trim();
	}

	public String getBusinessPlaceType() {

		return businessPlaceType;
	}

	public void setBusinessPlaceType(String businessPlaceType) {

		this.businessPlaceType = businessPlaceType == null ? null : businessPlaceType.trim();
	}

	public BigDecimal getLeasePeriod() {

		return leasePeriod;
	}

	public void setLeasePeriod(BigDecimal leasePeriod) {

		this.leasePeriod = leasePeriod;
	}

	public String getSelectionResult() {

		return selectionResult;
	}

	public void setSelectionResult(String selectionResult) {

		this.selectionResult = selectionResult == null ? null : selectionResult.trim();
	}

	public String getSelectionRemark() {

		return selectionRemark;
	}

	public void setSelectionRemark(String selectionRemark) {

		this.selectionRemark = selectionRemark == null ? null : selectionRemark.trim();
	}

	public String getBankCardOwnerName() {

		return bankCardOwnerName;
	}

	public void setBankCardOwnerName(String bankCardOwnerName) {

		this.bankCardOwnerName = bankCardOwnerName == null ? null : bankCardOwnerName.trim();
	}

	public String getBankCardNumber() {

		return bankCardNumber;
	}

	public void setBankCardNumber(String bankCardNumber) {

		this.bankCardNumber = bankCardNumber == null ? null : bankCardNumber.trim();
	}

	public String getIsDeleted() {

		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {

		this.isDeleted = isDeleted == null ? null : isDeleted.trim();
	}

	public String getCreator() {

		return creator;
	}

	public void setCreator(String creator) {

		this.creator = creator == null ? null : creator.trim();
	}

	public String getUpdater() {

		return updater;
	}

	public void setUpdater(String updater) {

		this.updater = updater == null ? null : updater.trim();
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

		this.comments = comments == null ? null : comments.trim();
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getCreateInQw() {
		return createInQw;
	}

	public String getExistsInQw() {
		return existsInQw;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public List<String> getLogComments() {
		return logComments;
	}

	public void setLogComments(List<String> logComments) {
		this.logComments = logComments;
	}

	public UserInfoDTO getUserInfoDTO() {
		return userInfoDTO;
	}

	public void setUserInfoDTO(UserInfoDTO userInfoDTO) {
		this.userInfoDTO = userInfoDTO;
	}

}
