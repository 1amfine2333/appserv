/**
 * 
 */
package com.xianglin.appserv.common.service.integration.cif.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * cif FigureService服务本地客户端实现类
 * 
 * @author pengpeng 2016年2月25日上午11:58:22
 */
public class FigureServiceClientImpl {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(FigureServiceClientImpl.class);

//	/** figureService */
//	private FigureService figureService;
//
//	/**
//	 * @see com.xianglin.appserv.common.service.integration.cif.FigureServiceClient#getByFigureId(java.lang.String)
//	 */
//	@Override
//	public FigureDTO getByFigureId(String figureId) {
//		List<FigureDTO> list = listByFigureIds(Arrays.asList(figureId));
//		if (CollectionUtils.isEmpty(list)) {
//			return null;
//		}
//		return list.get(0);
//	}
//
//	/**
//	 * @see com.xianglin.appserv.common.service.integration.cif.FigureServiceClient#listByUserId(java.lang.String)
//	 */
//	@Override
//	public List<FigureDTO> listByUserId(String userId) {
//		return listByUserIds(Arrays.asList(userId));
//	}
//
//	/**
//	 * @see com.xianglin.appserv.common.service.integration.cif.FigureServiceClient#listByUserIds(java.util.List)
//	 */
//	@Override
//	public List<FigureDTO> listByUserIds(List<String> userIdList) {
//		List<FigureDTO> result = new ArrayList<FigureDTO>();
//		try {
//			Response<List<FigureDTO>> response = figureService.listByPartyIds(userIdList);
//			if (response.isSuccess()) {
//				result = response.getResult();
//			} else {
//				logger.warn("listByUserIds fail! userIdList:{},response:{}",
//						ArrayUtils.toString(userIdList.toArray(new String[userIdList.size()])), response);
//			}
//		} catch (Exception e) {
//			logger.error("listByUserIds error!", e);
//		}
//		return result;
//	}
//
//	/**
//	 * @see com.xianglin.appserv.common.service.integration.cif.FigureServiceClient#listByFigureIds(java.util.List)
//	 */
//	@Override
//	public List<FigureDTO> listByFigureIds(List<String> figureIdList) {
//		List<FigureDTO> result = new ArrayList<FigureDTO>();
//		try {
//			Response<List<FigureDTO>> response = figureService.listByFigureIds(figureIdList);
//			if (response.isSuccess()) {
//				result = response.getResult();
//			} else {
//				logger.warn("listByFigureIds fail! figureIdList:{},response:{}",
//						ArrayUtils.toString(figureIdList.toArray(new String[figureIdList.size()])), response);
//			}
//		} catch (Exception e) {
//			logger.error("listByFigureIds error!", e);
//		}
//		return result;
//	}
//
//	/**
//	 * @param figureService
//	 *            the figureService to set
//	 */
//	public void setFigureService(FigureService figureService) {
//		this.figureService = figureService;
//	}

}
