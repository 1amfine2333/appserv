package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.xianglin.appserv.common.dal.dataobject.AppBusiness;
import com.xianglin.appserv.common.dal.dataobject.AppPush;

public interface AppBusinessDAO extends BaseDAO<AppBusiness>{

	/**
	 * 根据条件分页查询
	 * @param paras
	 * @return
	 */
	List<AppBusiness> query(Map<String, Object> paras);

	/**根据条件查询
	 * @param paras
	 * @return
	 */
	List<AppBusiness> queryList(AppBusiness paras);

	/**查询用户操作过的站点（按操作时间倒序排列）
	 * @param paras
	 * @return
	 */
	List<AppBusiness> selectUserList(AppBusiness paras);

	/**查询用户业务操作
	 * @param partyId
	 * @param businessId
	 * @return
	 */
	Integer selectBusinessUserCount(@Param("partyId") Long partyId, @Param("busiCode") String busiCode);

	/**加入新的业务关联
	 * @param partyId
	 * @param businessId
	 * @return
	 */
	Integer insertBusinessUser(@Param("partyId") Long partyId, @Param("busiCode") String busiCode);

	/**更新用户业务操作数量和时间
	 * @param partyId
	 * @param businessId
	 * @return
	 */
	Integer updateBusinessUser(@Param("partyId") Long partyId, @Param("busiCode") String busiCode);

    /**
     * 根据code查询business
     * @param code
     * @return
     */
    AppBusiness selectBusinessByCode(String code);

    /**
     * 根据IDS查询应用，并按修改时间排序
     * @param build
     * @return
     */
    List<AppBusiness> selectBusiListByIds(AppBusiness build);
}
