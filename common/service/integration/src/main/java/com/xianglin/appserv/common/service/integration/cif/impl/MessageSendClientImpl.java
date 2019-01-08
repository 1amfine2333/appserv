package com.xianglin.appserv.common.service.integration.cif.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.common.service.integration.cif.MessageSendClient;
import com.xianglin.xlappserver.common.service.facade.MessageFacade;
import com.xianglin.xlappserver.common.service.facade.base.Base;
import com.xianglin.xlappserver.common.service.facade.base.CommonReq;
import com.xianglin.xlappserver.common.service.facade.base.CommonResp;
import com.xianglin.xlappserver.common.service.facade.base.Header;
import com.xianglin.xlappserver.common.service.facade.base.Md;
import com.xianglin.xlappserver.common.service.facade.enums.BizTypeEnum;
import com.xianglin.xlappserver.common.service.facade.enums.SOPCodeEnum;
import com.xianglin.xlappserver.common.service.facade.enums.SendTypeEnum;
import com.xianglin.xlappserver.common.service.facade.vo.JsonBody;
import com.xianglin.xlappserver.common.service.facade.vo.SData;

public class MessageSendClientImpl implements MessageSendClient {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageSendClientImpl.class);

	private MessageFacade messageFacade;

	/**
	 * @see com.xianglin.appserv.common.service.integration.cif.MessageSendService#sendNotice(java.util.List)
	 */
	@Override
	public void sendNotice(List<Md> mds) {
		logger.info("MessageSendServiceImpl send message {}",mds);
		try {
			if(CollectionUtils.isNotEmpty(mds)){
				CommonReq<JsonBody> req = new CommonReq<>();
				Header header = new Header();
				header.setDirectType((byte) 8);
				header.setVersion((byte) 1);
				header.setCompressType((byte) 0);
				req.setHeader(header);
				
				JsonBody body = new JsonBody();
				SData[] datas = new SData[mds.size()];
				int index = 0;
				for(Md md:mds){
					datas[index] = new SData();
					datas[index].setBiz(BizTypeEnum.NOTICE.code());
					datas[index++].setMd(md);
				}
				body.setSData(datas);
				body.setSKey(String.valueOf(System.currentTimeMillis()));
				body.setSOpCode(SOPCodeEnum.MESSAGE.getCode());
				req.setBody(body);
				CommonResp<Base> resp = messageFacade.sendMessage(req);
				logger.info("MessageSendServiceImpl send resp {}",resp);
			}
		} catch (Exception e) {
			logger.error("MessageSendServiceImpl error",e);
		}
	}

	/**
	 * @see com.xianglin.appserv.common.service.integration.cif.MessageSendService#sendNotice(com.xianglin.xlappserver.common.service.facade.base.Md)
	 */
	@Override
	public void sendNotice(Md md) {
		logger.info("MessageSendServiceImpl send message {}",md);
		try {
			if(md != null){
				CommonReq<JsonBody> req = new CommonReq<>();
				Header header = new Header();
				header.setDirectType((byte) 8);
				header.setVersion((byte) 1);
				header.setCompressType((byte) 0);
				req.setHeader(header);
				
				JsonBody body = new JsonBody();
				SData data = new SData();
				data.setBiz(BizTypeEnum.NOTICE.code());
				data.setMd(md);
				body.setSData(ArrayUtils.toArray(data));
				body.setSKey(String.valueOf(System.currentTimeMillis()));
				body.setSOpCode(SOPCodeEnum.MESSAGE.getCode());
				req.setBody(body);
				CommonResp<Base> resp = messageFacade.sendMessage(req);
				logger.info("MessageSendServiceImpl send resp {}",resp);
			}
		} catch (Exception e) {
			logger.error("MessageSendServiceImpl error",e);
		}		
	}

	/**
	 * @see com.xianglin.appserv.common.service.integration.cif.MessageSendClient#sendGroupNotice(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendGroupNotice(String groupId, Integer noticeType, String updateId, String updateFigure,
			String operFigrue) {
		Md md = new Md();
		md.setGroupId(groupId);
		md.setFromId(updateId);
		md.setFromFigure(updateFigure);
		md.setNoticeType(noticeType);
		md.setSendType(SendTypeEnum.TEAM_MSG.code());
		md.setOperid(operFigrue);
		sendNotice(md);
	}

	public void setMessageFacade(MessageFacade messageFacade) {
		this.messageFacade = messageFacade;
	}
}
