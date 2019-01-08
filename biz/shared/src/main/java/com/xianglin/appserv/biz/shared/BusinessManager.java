/**
 * 
 */
package com.xianglin.appserv.biz.shared;

import java.util.List;
import java.util.Set;

import com.xianglin.appserv.common.service.facade.model.BusinessDTO;

/**
 * 
 * 
 * @author zhangyong 2016年8月30日下午2:14:45
 */
public interface BusinessManager {


	/**开通业务查询
	 * @param nodePartyId
	 * @return
	 */
	List<BusinessDTO> getBusinessList(Long nodePartyId);

	/**开通业务查询
	 * @param userType
	 * @return
	 */
	List<BusinessDTO> getBusiList(String userType);

	/**是否开通缴费业务
	 * @return
	 */
	boolean isOpenjiaofei();

	/**查询用户开通的业务
	 * @return
	 */
	Set<String> queryUserBusiness();
}
