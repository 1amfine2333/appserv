/**
 * 
 */
package com.xianglin.appserv.common.service.facade.app;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.GoodWordsVo;

/**
 * 
 * 
 * @author zhangyong 2016年9月29日下午2:44:19
 */
public interface GoodwordService {

	/**
	 * 查询每日一言
	 * @param req
	 * @return
	 */
	Response<List<GoodWordsVo>> getGoodwords(Request<GoodWordsVo> req);
	/**
	 * 返回一条未发送过的每日一言
	 * 
	 * @param req
	 * @return
	 */
	Response<GoodWordsVo> getOneUnSend();
	
	/**
	 *根据id查询每日一言
	 * @param id
	 * @return
	 */
	Response<GoodWordsVo> getGoodWordsById(Request<Long> req);
}
