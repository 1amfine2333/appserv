package com.xianglin.appserv.common.dal.dataobject;

import java.util.Date;

public class GoodWords {
    private Long id;

    private String words;

    private String imgUrl;

    private String wordsState;

    private String useDate;
    private String updator;

    private Date createDate;

    private Date updateDate;

    private String isDeleted;

    private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getWordsState() {
		return wordsState;
	}

	public void setWordsState(String wordsState) {
		this.wordsState = wordsState;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUseDate() {
		return useDate;
	}

	public void setUseDate(String useDate) {
		this.useDate = useDate;
	}

	@Override
	public String toString() {
		return "GoodWords [id=" + id + ", words=" + words + ", imgUrl=" + imgUrl + ", wordsState=" + wordsState
				+ ", useDate=" + useDate + ", updator=" + updator + ", createDate=" + createDate + ", updateDate="
				+ updateDate + ", isDeleted=" + isDeleted + ", comment=" + comment + "]";
	}

}