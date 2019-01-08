/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.MonthAchieveVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;

/**
 * 月度业绩排行榜服务
 * 
 * @author wanglei 2016年8月12日下午2:49:58
 */
public interface MonthAchieveService {

	public Response<MonthAchieveVo> query(Request<MonthAchieveQuery> req);
}
