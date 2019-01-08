package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.AppUserGenealogy;
import com.xianglin.appserv.common.dal.dataobject.AppUserGenerlogyLinkDO;

import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2017/11/9 11:16.
 * Update reason :
 */
public interface UserGenealogyManager {

    List<AppUserGenealogy> queryParas(Map<String, Object> paras);

    AppUserGenealogy query(Long id);

    Boolean update(AppUserGenealogy appUserGenealogy);

    Boolean insert(AppUserGenealogy appUserGenealogy);

    List<AppUserGenealogy> queryUserGenealogyParas(Map<String, Object> map);

    Boolean delete(Long id);

    Boolean insertLink(AppUserGenerlogyLinkDO userGenerlogyLinkDO);

    AppUserGenerlogyLinkDO queryLink(Long linkId);

    Boolean truncateGenealogysByPartyId(Long toPartyId);

    Boolean finishLink(Long linkId, String comments);

    List<AppUserGenerlogyLinkDO> queryLinkByPhone(String mobilePhone);

    List<AppUserGenerlogyLinkDO> queryLinkByParams(AppUserGenerlogyLinkDO linkDO);
}
