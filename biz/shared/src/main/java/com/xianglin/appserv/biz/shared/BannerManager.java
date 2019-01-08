/**
 *
 */
package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.AppBanner;
import com.xianglin.appserv.common.dal.dataobject.AppBusiness;
import com.xianglin.appserv.common.service.facade.model.BanerDTO;
import com.xianglin.appserv.common.service.facade.model.vo.BannerVo;
import com.xianglin.appserv.common.service.facade.model.vo.BusinessVo;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyong 2016年8月31日下午2:27:02
 */
public interface BannerManager {

    /**
     * 查询banner
     *
     * @return
     */
    @Deprecated
    List<BanerDTO> getBannerList();

    /**
     * 差异化查询对应的banner
     *
     * @param param
     * @return
     */
    List<BanerDTO> getBannerList(Map<String, Object> param);

    /**
     * 根据条件查询首页展示banner列表
     *
     * @param param
     * @return
     * @throws Exception
     */
    List<BannerVo> indexBannerList(Map<String, Object> param) throws Exception;

    /**
     * 根据条件展示客户端开通业务列表
     *
     * @param param
     * @return
     * @throws Exception
     */
    List<BusinessVo> indexBusiList(Map<String, Object> param);

    /**
     * 查询业务列表
     *
     * @param param
     * @return
     * @throws Exception
     */
    List<AppBusiness> queryBusiness(AppBusiness param);

    /**
     * 查询用户关联的业务
     *
     * @param param
     * @return
     * @throws Exception
     */
    List<AppBusiness> queryUserBusiness(AppBusiness param);

    /**
     * 更新（新增）用户操作业务记录
     *
     * @param partyId
     * @param business
     * @return
     */
    boolean updateUserBusiness(Long partyId, AppBusiness business);

    /**
     * 根据id查询业务明细
     *
     * @param businessId
     * @return
     */
    AppBusiness queryBusiness(Long businessId);

    /**
     * 修改Banner
     *
     * @param appBanner
     * @return
     */
    Boolean updateBanner(AppBanner appBanner);

    /**
     * 删除Banner
     *
     * @param id
     * @return
     */
    Boolean deleteBanner(Long id);

    /**
     * 新增Banner
     *
     * @param appBanner
     * @return
     */
    Boolean insertBannerVo(AppBanner appBanner);

    AppBanner selectByPrimaryKey(Long id);

    /**
     * 根据code查询business
     *
     * @param code
     * @return
     */
    AppBusiness selectBusinessByCode(String code);

    List<AppBanner> queryBanner(AppBanner appBanner);

    /**
     * 根据IDS查询应用，并按修改时间排序
     *
     * @param build
     * @return
     */
    List<AppBusiness> selectBusiListByIds(AppBusiness build);
}
