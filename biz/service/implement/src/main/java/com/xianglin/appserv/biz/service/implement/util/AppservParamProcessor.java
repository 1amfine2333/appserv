package com.xianglin.appserv.biz.service.implement.util;/**
 * Created by wanglei on 2017/4/11.
 */

import com.xianglin.appserv.biz.shared.SysParaManager;
import com.xianglin.appserv.common.util.EmojiEscapeUtil;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.impl.JSONParamProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.UUID;

/**
 * app客户端输入参数日志打印
 *
 * @author
 * @create 2017-04-11 10:59
 **/
public class AppservParamProcessor extends JSONParamProcessor {

    @Autowired
    private SessionHelper sessionHelper;

    private static final Logger logger = LoggerFactory.getLogger(AppservParamProcessor.class);

    @Autowired
    private SysParaManager sysParaManager;

    private static LocalTime expireTime;

    @Override
    public Object[] processParam(String serviceId, String requestData, Type[] types) {
        try {
            requestData = EmojiEscapeUtil.escapeEmoji2Base64String(requestData);
            Thread.currentThread().setName(UUID.randomUUID().toString());
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            if (!StringUtils.contains(serviceId, "LogService")) {
                logger.info("appserv {} deviceId:{} req partyId:{},requestData:{}", serviceId, deviceId, partyId, StringUtils.substring(requestData, 0, 500));
            }
            synSystemConfig();
        } catch (Exception e) {
            logger.warn("para process", e);
        }
        return super.processParam(serviceId, requestData, types);
    }

    /**
     * 同步参数配置信息
     */
    @Async
    void synSystemConfig(){
        if(expireTime == null || (LocalTime.now().getMinute() != expireTime.getMinute())){
            logger.info("begin to synSystemConfig");
            sysParaManager.queryPara().forEach(config -> {
                SysConfigUtil.put(config.getCode(),config.getValue());
            });
            expireTime = LocalTime.now();
        }
    }
}
