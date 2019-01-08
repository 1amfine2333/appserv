package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.ClientLogVo;
import com.xianglin.appserv.common.service.facade.model.vo.ClientLoginLogVo;

import java.util.Date;
import java.util.List;

/**
 * 日志服务
 */
public interface LogService {

    /** 提交客户端日志
     * @param req
     * @return
     */
    Response<Boolean> putClientLog(ClientLogVo req);

    /**
     * 提交用户的搜索记录
     * @param content
     * @return
     */
    Response<Boolean> putSearchRecord(String content);

    /**查询用户登陆记录
     * @param partyId
     * @param fromDate
     * @param toDate
     * @return
     */
    Response<List<ClientLoginLogVo>> queryUserLoginList(Long partyId, Date fromDate, Date toDate);
}
