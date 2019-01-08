/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo.req;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author wanglei 2016年8月12日上午11:40:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgQuery extends BaseVo {

	/**  */
	private static final long serialVersionUID = 6192868115336253452L;

	/** 消息类型 */
	private String msgType;

	private Long msgId;
	
	private String status;

	private String operateType;

	@Builder.Default
	private Integer startPage = 1;

	@Builder.Default
	private Integer pageSize = 10;

}
