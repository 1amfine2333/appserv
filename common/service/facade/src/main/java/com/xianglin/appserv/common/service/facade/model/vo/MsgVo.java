/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.Date;

import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import lombok.*;

/**
 * 
 * 
 * @author wanglei 2016年8月12日上午11:28:49
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgVo extends BaseVo{

	/**  */
	private static final long serialVersionUID = -6079155477609505061L;

	private Long id;
	
	/** 消息接收用户parityId */
	private Long partyId;

    private String msgTitle;

    private String msgType;

    private String msgTag;
    
    private String titleImg;

    @Builder.Default
    private String loginCheck = YESNO.NO.code;

    @Builder.Default
    private Integer expiryTime = 0;

    @Builder.Default
    private String passCheck = YESNO.NO.code;

    private String message;
    
    private Integer readNum;

    private Integer shareNum;//分享数

    private String remark;//备注 20171115

    private Integer praises;//点赞数

    private String status;

    private String isDeleted;

    private String creator;

    private String updater;

    private Date createTime;

    private Date updateTime;
    
    /** 消息是否已读 1：未读，9：已读 */
    private String msgStatus;
    
    /** 是否已点赞，N:否，Y:是 */
    private String praiseSign;

    /** 消息是否保存  默认保存*/
    @Builder.Default
    private YESNO isSave = YESNO.YES;
    
    private String url;
    
    private String msgSource;

    private String msgSourceUrl;

    private String titleImgList;//多图展示格式

    private Integer treadNum;//点踩数

    /**
     * Y;点赞
     * N：点踩
     * 为空，未操作
     */
    private String praiseTread;

    private String dateTime;

    private String comments;

}
