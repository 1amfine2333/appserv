package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.*;

import java.util.Date;
import java.util.List;

/**
 * Describe :
 * Created by Lister<18627416004@163.com/> on 2017/10/31 10:09.
 * Update reason :
 */
public interface LogManager {

    /**保存请求明细
     * @param build
     * @return
     */
    boolean insertRequestLogDetail(AppRequestLogDetail build);

    /**保存请求
     * @param appRequestLog
     * @return
     */
    boolean insertRequestLog(AppRequestLog appRequestLog);

    /**更新请求信息
     * @param build
     * @return
     */
    boolean updateRequestLog(AppRequestLog build);

    /**保存客户端错误日志
     * @param log
     * @return
     */
    boolean saveClientLog(AppClientLog log);

    /**保存搜索日誌
     * @param build
     * @return
     */
    Boolean saveSearchRecord(AppClientLogSearch build);

    /**保存登陆日志
     * @param log
     * @return
     */
    Boolean saveLoginRecord(AppClientLogLogin log);

    /**查询用户登陆日志
     * @param partyId
     * @param fromDate
     * @param toDate
     * @return
     */
    List<AppClientLogLogin> queryUserLoginList(Long partyId, Date fromDate, Date toDate);

}
