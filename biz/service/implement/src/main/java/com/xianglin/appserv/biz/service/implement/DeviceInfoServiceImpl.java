/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppPushDAO;
import com.xianglin.appserv.common.dal.dataobject.AppPush;
import com.xianglin.appserv.common.service.facade.DeviceInfoService;
import com.xianglin.appserv.common.service.facade.model.DeviceInfo;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgPushType;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.constant.ResponseConstants.FacadeEnums;
import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;

/**
 * 设备信息服务实现类
 *
 * @author cf 2016年8月15日上午10:59:57
 */
@ServiceInterface
public class DeviceInfoServiceImpl implements DeviceInfoService {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(DeviceInfoServiceImpl.class);

    /**
     * sessionHelper
     */
    private SessionHelper sessionHelper;

    @Autowired
    private AppPushDAO appPushDAO;

    /**
     * @see com.xianglin.cif.common.service.facade.DeviceInfoService#activateDevice(com.xianglin.cif.common.service.facade.model.DeviceInfo)
     */
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.DeviceInfoService.activateDevice", description = "移动app设备激活")
    @Override
    public Response<String> activateDevice(DeviceInfo deviceInfo) {
        Session session = sessionHelper.getSession();
        session.removeAttribute(SessionConstants.DEVICE_ID);
        /** 创建返回对象 */
        Response<String> resp = ResponseUtils.successResponse();
        try {
            String deviceId = StringUtils.replace(UUID.randomUUID().toString(), "-", "").substring(0, 32);
            AppPush push = AppPush.builder().deviceId(deviceId).systemType(deviceInfo.getSystemType())
                    .deviceInfo(deviceInfo.toString()).pushType(MsgPushType.HWPUSH.name()).pushToken(deviceId).build();
            appPushDAO.insert(push);
            /** 获取session相关 */
            session.setAttribute(SessionConstants.DEVICE_ID, deviceId);
            sessionHelper.saveSession(session);
            resp.setResult(deviceId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * @see com.xianglin.cif.common.service.facade.DeviceInfoService#updateIOSToken(java.lang.String)
     */
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.DeviceInfoService.updateIOSToken", description = "移动app更新iOSToken")
    @Override
    public Response<Boolean> updateIOSToken(String iOSToken) {
        /** 创建返回对象 */
        Response<Boolean> resp = ResponseUtils.successResponse();
        com.xianglin.cif.common.service.facade.model.Response<Boolean> response = new com.xianglin.cif.common.service.facade.model.Response<Boolean>();
        try {

            /** 获取session */
            Session session = sessionHelper.getSession();
            if (session == null) {
                logger.info("更新iOSToken,获取session为空。sessionId:{}", session.getId());
                resp.setCode(Integer.valueOf(FacadeEnums.SessionStatus.getCode()));
                resp.setMemo(FacadeEnums.SessionStatus.msg);
                resp.setTips(FacadeEnums.SessionStatus.tip);
                return resp;
            }
            /** 设备Id */
            String deviceId = session.getAttribute(SessionConstants.DEVICE_ID);
            /** 用户Partyid */
            String partyId = session.getAttribute(SessionConstants.PARTY_ID);
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public SessionHelper getSessionHelper() {
        return sessionHelper;
    }

    public void setSessionHelper(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.replace(UUID.randomUUID().toString(), "-", "").substring(0, 32));
    }
}
