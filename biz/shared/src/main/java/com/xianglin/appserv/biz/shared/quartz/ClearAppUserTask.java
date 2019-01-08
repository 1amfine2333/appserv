package com.xianglin.appserv.biz.shared.quartz;

import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.common.dal.daointerface.TaskDAO;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.constant.AppservConstants;
import com.xianglin.xlnodecore.common.service.facade.AccountNodeManagerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 定时刷新用户表，查询根据手机号查询站长信息是否存在，不存在则更新该条记录
 * <p/>
 * <p/>
 * * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/13
 * Time: 9:32
 */
public class ClearAppUserTask {

    private static Logger logger = LoggerFactory.getLogger(ClearAppUserTask.class);

    private static String CLEAR_USER_TASK_ID = "40001";
    @Autowired
    private AccountNodeManagerService accountNodeManagerService;
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private UserManager userManager;

    public void clearUser() throws Exception {
        Calendar calendar = Calendar.getInstance();
        String executeDate = DateFormatUtils.format(calendar, "yyyyMMdd");
        logger.info("======开始执行用户清理操作======, excute date :{}", executeDate);
        TimeUnit.SECONDS.sleep(RandomUtils.nextInt(30));//任务执行前先暂停
        int result = taskDAO.updateExcutedByTaskId(AppservConstants.STATUS_EXECUTING, CLEAR_USER_TASK_ID, executeDate);
        if (result == 0) {
            logger.info("other server is excuting this task !");
            logger.info("======结束执行用户清理操作======, excute date :{}", executeDate);
            return;
        }

        int size = 100;
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userType", Constant.UserType.nodeManager.name());
        queryMap.put("status", Constant.UserStatus.NORMAL.name());

        queryMap.put("startPage", 1);

        queryMap.put("pageSize", size);

        boolean run = true;

        while (run) {
            List<User> list = userManager.getUsersByParam(queryMap);
            if (CollectionUtils.isEmpty(list) || list.size() != size) {
                run = false;
            } else {
                for (User u : list) {
                    String loginName = u.getLoginName();
                    //1.判断是否为站长
                    if (loginName != null && !checkIsManager(u.getLoginName())) {
                        u.setLoginName(null);
                        u.setUserType(Constant.UserType.user.name());
                        //2.不为站长，更新用户表中的手机号和用户类型及deviceId
                        int i = userManager.updateUser(u);
                        logger.debug("loginName:{} 不是站长了，更新为普通用户,更新条数：{}", loginName, i);
                    }

                }

            }


        }
        Date nextDay = DateUtils.addDate(calendar.getTime(), 7);
        calendar.setTime(nextDay);
        String nextExecuteDate = DateFormatUtils.format(calendar, "yyyyMMdd");
        taskDAO.updateEndByTaskId(AppservConstants.STATUS_UNEXECUTED, CLEAR_USER_TASK_ID, executeDate, nextExecuteDate);
        logger.info("======结束执行用户清理操作====== ,next excute date : {}", nextExecuteDate);
    }

    private boolean checkIsManager(String mobile) {
        try {
            List<Long> partyIds = accountNodeManagerService.getNodeManagerMoreInfoByMobilePhone(mobile);
            if (CollectionUtils.isNotEmpty(partyIds)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
