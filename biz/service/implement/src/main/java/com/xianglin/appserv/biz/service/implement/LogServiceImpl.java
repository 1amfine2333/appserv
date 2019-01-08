package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.LogManager;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.AppClientLog;
import com.xianglin.appserv.common.dal.dataobject.AppClientLogSearch;
import com.xianglin.appserv.common.service.facade.app.LogService;
import com.xianglin.appserv.common.service.facade.model.AppSessionConstants;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.ClientLogVo;
import com.xianglin.appserv.common.service.facade.model.vo.ClientLoginLogVo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 日志服务
 */
@Service("logService")
@ServiceInterface
public class LogServiceImpl implements LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private LogManager logManager;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.LogService.putClientLog", description = "提交用户的搜索记录")
    public Response<Boolean> putClientLog(ClientLogVo req) {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String clentVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            String systemType = sessionHelper.getSessionProp(AppSessionConstants.SYSTEM_TYPE,String.class);
            String remoteIp = sessionHelper.getGatewayProp("remoteAddr");
            return logManager.saveClientLog(AppClientLog.builder().partyId(partyId).deviceId(deviceId).version(clentVersion).systemType(systemType).ip(remoteIp).message(req.getMessage()).build());
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.LogService.putSearchRecord", description = "日志服务")
    public Response<Boolean> putSearchRecord(String content) {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String clentVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            String systemType = sessionHelper.getSessionProp(AppSessionConstants.SYSTEM_TYPE,String.class);
            String remoteIp = sessionHelper.getGatewayProp("remoteAddr");
            return logManager.saveSearchRecord(AppClientLogSearch.builder().partyId(partyId).deviceId(deviceId).systemType(systemType).version(clentVersion).type(Constant.SearchType.SEARCH.name()).content(content).build());
        });
    }

    @Override
    public Response<List<ClientLoginLogVo>> queryUserLoginList(Long partyId, Date fromDate, Date toDate) {
        return responseCacheUtils.execute(() -> {
            return logManager.queryUserLoginList(partyId,fromDate,toDate).stream().map(v -> {
                return DTOUtils.map(v,ClientLoginLogVo.class);
            }).collect(Collectors.toList());
        });
    }
}
