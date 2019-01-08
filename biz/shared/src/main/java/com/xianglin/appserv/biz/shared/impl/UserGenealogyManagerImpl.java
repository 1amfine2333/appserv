package com.xianglin.appserv.biz.shared.impl;

import com.xianglin.appserv.biz.shared.UserGenealogyManager;
import com.xianglin.appserv.common.dal.constant.GenealogyLinkStatus;
import com.xianglin.appserv.common.dal.constant.GenealogyLinkType;
import com.xianglin.appserv.common.dal.daointerface.AppUserGenealogyDAO;
import com.xianglin.appserv.common.dal.daointerface.AppUserGenerlogyLinkDOMapper;
import com.xianglin.appserv.common.dal.dataobject.AppUserGenealogy;
import com.xianglin.appserv.common.dal.dataobject.AppUserGenerlogyLinkDO;
import com.xianglin.appserv.common.dal.dataobject.AppUserGenerlogyLinkDOExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2017/11/9 14:00.
 * Update reason :
 */
@Service
public class UserGenealogyManagerImpl implements UserGenealogyManager {

    @Autowired
    private AppUserGenealogyDAO appUserGenealogyDAO;

    @Autowired
    private AppUserGenerlogyLinkDOMapper generlogyLinkMapper;

    @Override
    public List<AppUserGenealogy> queryParas(Map<String, Object> paras) {

        return appUserGenealogyDAO.queryUserGenealogy(paras);
    }

    @Override
    public AppUserGenealogy query(Long id) {

        return appUserGenealogyDAO.selectByPrimaryKey(id);
    }

    @Override
    public Boolean update(AppUserGenealogy appUserGenealogy) {

        return appUserGenealogyDAO.updateByPrimaryKeySelective(appUserGenealogy) == 1;
    }

    @Override
    public Boolean insert(AppUserGenealogy appUserGenealogy) {

        return appUserGenealogyDAO.insert(appUserGenealogy) == 1;
    }

    @Override
    public List<AppUserGenealogy> queryUserGenealogyParas(Map<String, Object> map) {

        return appUserGenealogyDAO.query(map);
    }

    @Override
    public Boolean delete(Long id) {

        return appUserGenealogyDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public Boolean insertLink(AppUserGenerlogyLinkDO userGenerlogyLinkDO) {

        generlogyLinkMapper.insertSelective(userGenerlogyLinkDO);
        return true;
    }

    @Override
    public AppUserGenerlogyLinkDO queryLink(Long linkId) {

        return generlogyLinkMapper.selectByPrimaryKey(linkId);
    }

    @Override
    public Boolean truncateGenealogysByPartyId(Long toPartyId) {

        appUserGenealogyDAO.deleteAllByCreator(toPartyId);
        return true;
    }

    /**
     * 结束分享链接状态
     *
     * @param linkId
     * @return
     */
    @Override
    public Boolean finishLink(Long linkId, String comments) {

        AppUserGenerlogyLinkDO linkDO = new AppUserGenerlogyLinkDO();
        linkDO.setId(linkId);
        linkDO.setStatus(GenealogyLinkStatus.FINISH.getValue());
        linkDO.setUpdateTime(new Date());
        linkDO.setComments(comments);
        generlogyLinkMapper.updateByPrimaryKeySelective(linkDO);
        return true;
    }

    @Override
    public List<AppUserGenerlogyLinkDO> queryLinkByPhone(String mobilePhone) {

        AppUserGenerlogyLinkDOExample example = new AppUserGenerlogyLinkDOExample();
        example.or().andMobileEqualTo(mobilePhone)
                .andStatusEqualTo(GenealogyLinkStatus.WAIT.getValue())
                .andTypeEqualTo(GenealogyLinkType.PUBLIC_ADD.getValue())
                .andIsDeletedEqualTo("N");
        return generlogyLinkMapper.selectByExample(example);
    }

    @Override
    public List<AppUserGenerlogyLinkDO> queryLinkByParams(AppUserGenerlogyLinkDO linkDO) {

        AppUserGenerlogyLinkDOExample example = new AppUserGenerlogyLinkDOExample();
        example.or().andMobileEqualTo(linkDO.getMobile())
                .andGenealogyUserEqualTo(linkDO.getGenealogyUser())
                .andParentIdEqualTo(linkDO.getParentId())
                .andTypeEqualTo(GenealogyLinkStatus.WAIT.getValue())
                .andTypeEqualTo(GenealogyLinkType.PUBLIC_ADD.getValue())
                .andIsDeletedEqualTo("N");
        return generlogyLinkMapper.selectByExample(example);
    }
}

