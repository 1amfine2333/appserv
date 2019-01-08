package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by wanglei on 2017/5/2.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityVo extends BaseVo {

	private Long id;

    private String category;

	private String activeCode;

	private String activityName;

	private String activityDesc;

	private String activityUrl;

	private String activityImgUrl;

	private String activityDetailImgUrl;

	private String isPraise;

	private String dataStatus;

    private int clickNum;

    private String supportVersion;

	private String isDeleted;

	private Date createTime;

	private Date updateTime;

	private String comments;

	
}
