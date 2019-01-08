/**
 *
 */
package com.xianglin.appserv.biz.shared.impl;

import com.xianglin.appserv.biz.shared.BannerManager;
import com.xianglin.appserv.common.dal.daointerface.AppBannerDAO;
import com.xianglin.appserv.common.dal.daointerface.AppBusinessDAO;
import com.xianglin.appserv.common.dal.dataobject.AppBanner;
import com.xianglin.appserv.common.dal.dataobject.AppBusiness;
import com.xianglin.appserv.common.service.facade.model.BanerDTO;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.BannerVo;
import com.xianglin.appserv.common.service.facade.model.vo.BusinessVo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SysConfigUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyong 2016年8月31日下午2:29:08
 */
@Service
public class BannerManagerImpl implements BannerManager {

    private static final Logger logger = LoggerFactory.getLogger(BannerManagerImpl.class);

    @Autowired
    private AppBannerDAO bannerDao;

    @Autowired
    private AppBusinessDAO businessDAO;

    /**
     * @see com.xianglin.appserv.biz.shared.BannerManager#getBannerList()
     */
    @Override
    public List<BanerDTO> getBannerList(Map<String, Object> param) {
        logger.info("查询banner参数：{}", param);
        List<Map<String, Object>> list = bannerDao.queryAppBanerList(param);
        List<BanerDTO> listDto = null;
        if (CollectionUtils.isNotEmpty(list)) {
            listDto = new ArrayList<>(list.size());
            for (Map<String, Object> appbaner : list) {
                try {
                    String supportVersion = appbaner.get("supportVersion") == null ? null
                            : appbaner.get("supportVersion").toString();

                    String paramSupportVersion = param.get("supportVersion") == null ? ""
                            : param.get("supportVersion").toString();
                    if (StringUtils.isNotEmpty(supportVersion)) {
                        String[] versions = supportVersion.split(",");
                        for (String version : versions) {
                            if (version.equals(paramSupportVersion) || "ALL".equalsIgnoreCase(supportVersion)) {
                                wrapBanner(listDto, appbaner);
                            }
                        }
                    } else {
                        wrapBanner(listDto, appbaner);
                    }
                } catch (Exception e) {
                    logger.error("查询banner出现问题", e);
                }
            }

        }
        return listDto;
    }

    private void wrapBanner(List<BanerDTO> listDto, Map<String, Object> appbaner) {
        BanerDTO dto = new BanerDTO();
        if (appbaner.get("bannerImageCode") != null) {
            dto.setImageUrl(SysConfigUtil.getStr(Constant.BusiVisitKey.APP_FILESERVER_URL.code, "")
                    + appbaner.get("bannerImageCode"));
        }
        if (appbaner.get("hrefUrl") != null) {
            dto.setUrl(appbaner.get("hrefUrl").toString());
        }
        listDto.add(dto);
    }

    /**
     * @see com.xianglin.appserv.biz.shared.BannerManager#getBannerList()
     */
    @Override
    public List<BanerDTO> getBannerList() {
        return null;
    }

    /**
     * @throws Exception
     * @see com.xianglin.appserv.biz.shared.BannerManager#indexBannerList(java.util.Map)
     */
    @Override
    public List<BannerVo> indexBannerList(Map<String, Object> param) throws Exception {
        List<AppBanner> list = bannerDao.queryBanerList(param);
        return DTOUtils.map(list, BannerVo.class);
    }

    /**
     * @throws Exception
     * @see com.xianglin.appserv.biz.shared.BannerManager#indexBusiList(java.util.Map)
     */
    @Override
    public List<BusinessVo> indexBusiList(Map<String, Object> param) {
        List<AppBusiness> list = businessDAO.query(param);
        if (CollectionUtils.isNotEmpty(list)) {
            return DTOUtils.map(list, BusinessVo.class);
        }
        return null;
    }

    @Override
    public List<AppBusiness> queryBusiness(AppBusiness param)  {
        return businessDAO.queryList(param);
    }

    @Override
    public List<AppBusiness> queryUserBusiness(AppBusiness param)  {
        return businessDAO.selectUserList(param);
    }

    @Override
    public boolean updateUserBusiness(Long partyId, AppBusiness business) {
        if(business != null){
            Integer result = businessDAO.selectBusinessUserCount(partyId, business.getBusiCode());
            if (result != null && result != 0) {
                businessDAO.updateBusinessUser(partyId, business.getBusiCode());
            } else {
                businessDAO.insertBusinessUser(partyId, business.getBusiCode());
            }
        }
        return true;
    }

    @Override
    public AppBusiness queryBusiness(Long businessId) {
        return businessDAO.selectByPrimaryKey(businessId);
    }

    @Override
    public Boolean updateBanner(AppBanner appBanner) {
        return bannerDao.updateByPrimaryKeySelective(appBanner)==1;
    }

    @Override
    public Boolean deleteBanner(Long id) {
        return bannerDao.deleteByPrimaryKey(id)==1;
    }

    @Override
    public Boolean insertBannerVo(AppBanner appBanner) {
        return bannerDao.insert(appBanner)==1;
    }

    @Override
    public AppBanner selectByPrimaryKey(Long id) {
        return bannerDao.selectByPrimaryKey(id);
    }

    @Override
    public AppBusiness selectBusinessByCode(String code) {
        return businessDAO.selectBusinessByCode(code);
    }

    @Override
    public List<AppBanner> queryBanner(AppBanner appBanner) {
        return bannerDao.queryBanner(appBanner);
    }

    @Override
    public List<AppBusiness> selectBusiListByIds(AppBusiness build) {
        return businessDAO.selectBusiListByIds(build);
    }

}
