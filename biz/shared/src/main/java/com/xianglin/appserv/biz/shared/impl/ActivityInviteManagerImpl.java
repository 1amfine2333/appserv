/**
 * 
 */
package com.xianglin.appserv.biz.shared.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.ActivityInviteManager;
import com.xianglin.appserv.common.dal.daointerface.ActivityInviteDAO;
import com.xianglin.appserv.common.dal.daointerface.ActivityInviteDetailDAO;
import com.xianglin.appserv.common.dal.daointerface.UserDAO;
import com.xianglin.appserv.common.dal.dataobject.ActivityInvite;
import com.xianglin.appserv.common.dal.dataobject.ActivityInviteDetail;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.app.ActivityInviteService;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.ActivityInviteSource;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.ActivityInviteStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import com.xianglin.appserv.common.util.QRUtils;
import com.xianglin.appserv.common.util.SerialNumberUtil;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.xlappfile.common.service.facade.FileDealService;
import com.xianglin.xlappfile.common.service.facade.base.CommonReq;
import com.xianglin.xlappfile.common.service.facade.base.CommonResp;
import com.xianglin.xlappfile.common.service.facade.vo.FileReqVo;
import com.xianglin.xlappfile.common.service.facade.vo.FileRespVo;

/**
 * 
 * 
 * @author wanglei 2016年12月13日下午6:17:54
 */
@Service
public class ActivityInviteManagerImpl implements ActivityInviteManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityInviteManagerImpl.class);

	@Autowired
	private ActivityInviteDAO activityInviteDAO;

	@Autowired
	private ActivityInviteDetailDAO activityInviteDetailDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private FileDealService appFileService;
	
	@Value("#{config['APP_FILESERVER_URL']}")
	private String appFileURL;//
	
	/**
	 * @see com.xianglin.appserv.biz.shared.ActivityInviteManager#queryInit(java.lang.Long)
	 */
	@Override
	public ActivityInvite queryInit(Long partyId) {
		ActivityInvite ai = activityInviteDAO.selectByPartyId(partyId); 
		if(ai == null){
			ai = new ActivityInvite();
			ai.setPartyId(partyId);
			//初始化
			User user = userDAO.selectByPartyId(partyId);
			if(StringUtils.isNotEmpty(user.getNikerName())){
				ai.setCommentName(user.getNikerName());
			}else{
				ai.setCommentName(SerialNumberUtil.phoneNumberEncrypt(user.getLoginName()));
			}
			ai.setQrCode(createQrCodeLink(partyId));
			ai.setRecAmt(BigDecimal.ZERO);
			ai.setRecCount(0);
			activityInviteDAO.insert(ai);
		}
		return ai;
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.ActivityInviteManager#queryRanking(java.util.Map)
	 */
	@Override
	public List<ActivityInvite> queryRanking(Map<String, Object> req) {
		return activityInviteDAO.selectRanking(req);
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.ActivityInviteManager#queryUpdateRemind(java.lang.Long)
	 */
	@Override
	public List<ActivityInviteDetail> queryUpdateRemind(Long partyId) {
		ActivityInviteDetail detail = new ActivityInviteDetail();
		detail.setRecPartyId(partyId);
		detail.setMsgStatus(YESNO.NO.code);
		detail.setStatus(ActivityInviteStatus.S.name());
		List<ActivityInviteDetail> list = activityInviteDetailDAO.select(detail);
		for(ActivityInviteDetail d:list){
			d.setMsgStatus(YESNO.YES.code);
			activityInviteDetailDAO.updateByPrimaryKeySelective(d);
		}
		return list;
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.ActivityInviteManager#queryInvateDetail(java.util.Map)
	 */
	@Override
	public List<ActivityInviteDetail> queryInvateDetail(Map<String, Object> req) {
		return activityInviteDetailDAO.selectMap(req);
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.ActivityInviteManager#addInvateDetail(com.xianglin.appserv.common.dal.dataobject.ActivityInviteDetail)
	 */
	@Override
	public int addInvateDetail(ActivityInviteDetail req) {
		return activityInviteDetailDAO.insertWithSelect(req);
	}

    @Override
    public int queryDetailCount(ActivityInviteDetail detail) {
        return activityInviteDetailDAO.selectCount(detail);
    }

    @Override
    public int updateInviteDetail(ActivityInviteDetail activityInviteDetail) {
        return activityInviteDetailDAO.updateByPrimaryKeySelective(activityInviteDetail);
    }

    /**
	 * 创建二维码链接
	 * 
	 * @param partyId
	 * @return
	 */
	private String createQrCodeLink(Long partyId){
		String returnUrl = null;
		try {
			String content = SysConfigUtil.getStr("activity.invite_url")+"?source="+ActivityInviteSource.QR.name()+"&pid="+partyId;
			byte[] fileBytes = QRUtils.qrCreate(content);
	        //上传文件到File服务器
	        if(fileBytes != null && fileBytes.length > 0){
	        	CommonReq<FileReqVo> commonReq = new CommonReq<>();
	        	FileReqVo vo = new FileReqVo();
	        	vo.setData(fileBytes);
	        	vo.setFileSize(fileBytes.length);
	        	vo.setFileName(UUID.randomUUID().toString());
	        	vo.setFileType("1");
	        	commonReq.setBody(vo);
	        	CommonResp<FileRespVo> fileResp = appFileService.uploadImgFile(commonReq);
	        	if(fileResp.getBody() != null){
	        		returnUrl = appFileURL+fileResp.getBody().getId();
	        	}
	        }
		} catch (Exception e) {
			logger.error("Failed to createQrCodeLink", e);
		}
		return returnUrl;
	}
}
