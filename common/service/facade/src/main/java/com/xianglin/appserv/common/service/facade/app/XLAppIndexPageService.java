/**
 * 
 */
package com.xianglin.appserv.common.service.facade.app;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.BanerDTO;
import com.xianglin.appserv.common.service.facade.model.BusiVisitDTO;
import com.xianglin.appserv.common.service.facade.model.BusinessDTO;
import com.xianglin.appserv.common.service.facade.model.ProfitDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.XLAppHomeDataDTO;
import com.xianglin.appserv.common.service.facade.model.vo.AppPushVo;
import com.xianglin.appserv.common.service.facade.model.vo.AppVersionVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.TotalSolarDataVo;

/**
 * 
 * 
 * @author zhangyong 2016年8月12日上午11:17:18
 */
public interface XLAppIndexPageService {

	/**
	 * app首页数据
	 *  
	 * @param partyId
	 * @param nodePartyId
	 * @return
	 */
	Response<XLAppHomeDataDTO>  getXLAppHomeData(Long partyId,Long nodePartyId);
	
	/**
	 * 查询光伏总收益
	 * 
	 * @return
	 */
	Response<TotalSolarDataVo> getTotalSolarData();
	
	/**
	 * 网点收益业绩数据
	 * @param partyId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	Response<List<ProfitDTO>> getProfitList(Long partyId,Integer startPage,Integer pageSize);
	
	/**
	 * 
	 * app baner滚动
	 * 
	 * @return
	 */
	Response<List<BanerDTO>> getBanerList();
	
	/***
	 * 
	 * 查询开通的业务
	 * @param nodePartyId
	 * @return
	 */
	Response<List<BusinessDTO>> getBusinessList(Long nodePartyId);
	/**
	 * 
	 * 
	 * 查询消息列表
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	Response<List<MsgVo>> getMsgList(Integer startPage,Integer pageSize);
	
	/**
	 * 查询首页业绩与收益相关url
	 * 
	 * 
	 * @param busiType
	 * @return
	 */
	Response<BusiVisitDTO> getBusiVisitUrlInfo(String busiType);
	
	/**
	 * 
	 *	资金账户是否存在
	 * 
	 * @param partyId
	 * @return
	 */
	Response<Boolean> isExistPartyAttrAccount(Long partyId);
	
	/**
	 * 查询app最新版本和下载地址
	 * 
	 * @param deviceType ANDROID/IOS
	 * @return
	 */
	Response<AppVersionVo> version(String deviceType);
	
	/**
	 * 提交推送信息
	 * @param vo
	 * @return
	 */
	Response<Boolean> submitPushInfo(AppPushVo vo);
	/**
	 * 开关，ios是否支持网点编号登陆
	 * 
	 * 
	 * @return
	 */
	Response<Boolean> iosSupportNodeCode();
}
