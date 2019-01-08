/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 
 * 
 * @author wanglei 2016年11月25日下午2:48:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessVo extends BaseVo {

	/**  */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String districtFullName;

    private String busiCode;

    private String busiName;

    private String introduction;

    private String busiImage;
    
    private String busiIcon;
    
    private String htmlTitle;

    private String hrefUrl;
    
    private String busiActive;

    private String supportVersion;

    private String supportUserType;

    private Integer priorityLevel;

    private String busiCategory;

    private String authSign;

    private Date createDate;

    private Date updateDate;

    private String creator;

    private String updater;

    private String isDeleted;

    private String comments;

    private String keyWords;

	private String indexStatus;
}
