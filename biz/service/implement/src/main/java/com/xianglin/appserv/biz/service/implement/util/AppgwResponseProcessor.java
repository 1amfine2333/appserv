/**
 * 
 */
package com.xianglin.appserv.biz.service.implement.util;

import com.xianglin.appserv.biz.shared.SysParaManager;
import com.xianglin.appserv.common.util.EmojiEscapeUtil;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.gateway.common.service.spi.ResponseProcessor;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

/**
 * appgw网关响应转换器
 * 
 * @author pengpeng 2016年2月24日下午4:31:58
 */
public class AppgwResponseProcessor implements ResponseProcessor<String, Object> {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(AppgwResponseProcessor.class);

	@Autowired
	private SessionHelper sessionHelper;

	/**
	 * @see com.xianglin.gateway.common.service.spi.ResponseProcessor#process(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public ServiceResponse<Object> process(String serviceId, Object response) {
		ServiceResponse<Object> result = null;
		if (response instanceof Response<?>) {
			Response<?> resp = (Response<?>) response;
			result = new ServiceResponse<Object>(resp.getCode(), resp.getMemo(), resp.getTips());
			if (resp.getResult() instanceof String) {
				result.setResult(EmojiEscapeUtil.escapeBase642EmojiString(resp.getResult().toString()));
			} else {
				result.setResult(JSON.parse(EmojiEscapeUtil.escapeBase642EmojiString(JSON.toJSONString(resp.getResult()))));
			}
			String strResult = ToStringBuilder.reflectionToString(result,ToStringStyle.JSON_STYLE);
			Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
			String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID,String.class);
			logger.info("appserv {} resp deviceId:{} partyId:{},result:{}",serviceId,deviceId,partyId,StringUtils.substring(strResult,0,500));
			SessionHelper.clear();
		} else {
			logger.error("illegal response! response:" + response);
			result = new ServiceResponse<Object>(ResultEnum.BizException);
		}
		// 清理ThreadLocal
		SessionHelper.clear();
		return result;
	}


}

