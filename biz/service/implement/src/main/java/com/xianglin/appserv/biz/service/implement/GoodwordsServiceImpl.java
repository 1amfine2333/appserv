/**
 * 
 */
package com.xianglin.appserv.biz.service.implement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xianglin.appserv.common.dal.daointerface.GoodWordsDAO;
import com.xianglin.appserv.common.dal.dataobject.GoodWords;
import com.xianglin.appserv.common.service.facade.app.GoodwordService;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.GoodWordsVo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.DateUtils;

/**
 * 
 * 
 * @author zhangyong 2016年9月29日下午2:47:27
 */

public class GoodwordsServiceImpl implements GoodwordService {

	private static final Logger logger = LoggerFactory.getLogger(GoodwordsServiceImpl.class);

	@Autowired
	private GoodWordsDAO goodWordsDao;
	/**
	 * @see com.xianglin.appserv.common.service.facade.app.GoodwordService#getGoodwords(com.xianglin.appserv.common.service.facade.model.Request)
	 */
	@Override
	public Response<List<GoodWordsVo>> getGoodwords(Request<GoodWordsVo> req) {
		Response<List<GoodWordsVo>> resp = new Response<>(null);
		if(req == null || req.getReq() == null){
			resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
		}else{
			try {
				Map<String,Object> param = DTOUtils.beanToMap(req.getReq());
				param.put("isDeleted", "0");
				List<GoodWords> dbList= goodWordsDao.getGoodWords(param);
				if(CollectionUtils.isNotEmpty(dbList)){
					resp.setResult(DTOUtils.map(dbList, GoodWordsVo.class));
				}else{
					resp.setFacade(FacadeEnums.RETURN_EMPTY);
				}
			} catch (Exception e) {
				logger.error("数据转换错误",e);
			}
		}
		return resp;
	}

	/**
	 * .Request)
	 */
	@Override
	public Response<GoodWordsVo> getOneUnSend() {
		Response<GoodWordsVo> resp = new Response<>(null);
			try {
				Map<String,Object> param = new HashMap<>();
				param.put("isDeleted", "0");
				param.put("wordsState", "N");
				param.put("startPage", 0);
				param.put("pageSize", 1);
				List<GoodWords> dbList= goodWordsDao.getGoodWords(param);
				if(CollectionUtils.isNotEmpty(dbList)){
					GoodWords good = dbList.get(0);
					resp.setResult(DTOUtils.map(good, GoodWordsVo.class));
					//更新为已读：
					good.setWordsState("Y");
					good.setUseDate(DateUtils.formatDate(DateUtils.getNow(),DateUtils.DATE_TPT_TWO));
					goodWordsDao.updateByPrimaryKeySelective(good);
				}else{
					resp.setFacade(FacadeEnums.RETURN_EMPTY);
				}
			} catch (Exception e) {
				logger.error("数据转换错误",e);
				resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
			}
		return resp;
	}

	/**
	 * @see 
	 */
	@Override
	public Response<GoodWordsVo> getGoodWordsById(Request<Long> req) {
		Response<GoodWordsVo> resp = new Response<>(null);
		try {
			GoodWords dbgood= goodWordsDao.selectByPrimaryKey(req.getReq());
			if(null!=dbgood){
				resp.setResult(DTOUtils.map(dbgood, GoodWordsVo.class));
			}else{
				resp.setFacade(FacadeEnums.RETURN_EMPTY);
			}
		} catch (Exception e) {
			logger.error("数据转换错误",e);
		}
	return resp;
	}

}
