package com.xianglin.appserv.biz.shared.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xianglin.appserv.biz.shared.LogManager;
import com.xianglin.appserv.common.dal.daointerface.*;
import com.xianglin.appserv.common.dal.dataobject.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Describe :
 * Created by Lister<18627416004@163.com/> on 2017/10/31 10:11.
 * Update reason :
 */
@Service
public class LogManagerImpl implements LogManager {
    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    @Autowired
    private AppRequestLogDAO appRequestLogDAO;

    @Autowired
    private AppRequestLogDetailDAO appRequestLogDetailDAO;

    @Autowired
    private AppPushDAO appPushDAO;
    
    @Autowired
    private AppClientLogSearchDAO appClientLogSearchDAO;

    @Autowired
    private AppClientLogDAO appClientLogDAO;

    @Autowired
    private AppClientLogLoginDAO appClientLogLoginDAO;

    @Override
    public boolean insertRequestLogDetail(AppRequestLogDetail build) {
        return appRequestLogDetailDAO.insert(build) == 1;
    }

    @Override
    public boolean insertRequestLog(AppRequestLog appRequestLog) {
        return appRequestLogDAO.insert(appRequestLog) == 1;
    }

    @Override
    public boolean updateRequestLog(AppRequestLog build) {
        return appRequestLogDAO.updateByPrimaryKeySelective(build) == 1;
    }

    @Override
    public boolean saveClientLog(AppClientLog log) {
        if (StringUtils.isEmpty(log.getSystemType()) || StringUtils.isEmpty(log.getVersion())) {
            AppPush push = appPushDAO.selectByDeviceId(log.getDeviceId());
            if (push != null) {
                log.setVersion(push.getVersion());
                log.setSystemType(push.getSystemType());
                if (log.getPartyId() == null) {
                    log.setPartyId(push.getPartyId());
                }
            }
        }
        return appClientLogDAO.insert(log) == 1;
    }

    @Override
    public Boolean saveSearchRecord(AppClientLogSearch build) {
        return appClientLogSearchDAO.insert(build)==1;
    }

    @Override
    public Boolean saveLoginRecord(AppClientLogLogin log) {
        return appClientLogLoginDAO.insert(log) == 1;
    }

    @Override
    public List<AppClientLogLogin> queryUserLoginList(Long partyId, Date fromDate, Date toDate) {
        EntityWrapper<AppClientLogLogin> ew = new EntityWrapper();
        ew.eq("party_id",partyId)
                .ge(fromDate != null,"create_time",fromDate)
                .le(fromDate != null,"create_time",toDate).orderBy("id",false);
        return appClientLogLoginDAO.selectList(ew);
    }
}
