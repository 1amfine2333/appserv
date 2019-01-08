package com.xianglin.appserv.biz.shared.impl;

import com.xianglin.appserv.biz.shared.PropExtendManager;
import com.xianglin.appserv.common.dal.daointerface.PropExtendDAO;
import com.xianglin.appserv.common.dal.dataobject.AppPropExtend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2017/12/8 17:23.
 * Update reason :
 */
@Service
public class PropExtendManagerImpl implements PropExtendManager {

    @Autowired
    private PropExtendDAO propExtendDao;

    @Override
    public List<AppPropExtend> queryChannel(Map<String, Object> map) {

        return propExtendDao.query(map);
    }

    @Override
    public Boolean update(AppPropExtend apppropextend) {

        return propExtendDao.updateById(apppropextend) == 1;
    }

    @Override
    public Boolean insert(AppPropExtend appPropExtend) {

        return propExtendDao.insert(appPropExtend) == 1;
    }

    @Override
    public AppPropExtend selectAppPropExtendById(Long id) {

        return propExtendDao.selectById(id);
    }

    @Override
    public AppPropExtend queryAndInit(AppPropExtend paras) {

        AppPropExtend prop = propExtendDao.selectProp(paras.getRelationId(), paras.getType(), paras.getEkey());
        if (prop == null) {
            propExtendDao.insertExceptPropExtend(paras);
            prop = paras;
        }
        return prop;
    }

    @Override
    public Boolean insertExceptPropExtend(AppPropExtend build) {

        return propExtendDao.insertExceptPropExtend(build) == 1;
    }

    @Override
    public Boolean updateByParam(Map<String, String> param, AppPropExtend build) {

        return propExtendDao.updateByparamMap(param, build) > 0;
    }

    @Override
    public AppPropExtend selectProp(Long partyId, String type, String ekey) {
        return propExtendDao.selectProp(partyId, type, ekey);
    }
}
