package com.xianglin.appserv.biz.shared.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.SysParaManager;
import com.xianglin.appserv.common.dal.daointerface.SystemConfigMapper;
import com.xianglin.appserv.common.dal.dataobject.SystemConfigModel;

/**
 * Created by wanglei on 2017/1/4.
 */
@Service
public class SysParaManagerImpl implements SysParaManager {

	private static final Logger logger = LoggerFactory.getLogger(SysParaManagerImpl.class);

	@Autowired
	private SystemConfigMapper systemConfigMapper;

	@Override
	public List<SystemConfigModel> queryPara(Map<String, Object> req) {
		return systemConfigMapper.selectMap(req);
	}

	@Override
	public Boolean updatePara(SystemConfigModel para) {
		Boolean b = systemConfigMapper.updateByPrimaryKeySelective(para) == 1;
		return b;
	}

	@Override
	public Boolean addPara(SystemConfigModel para) {
		return systemConfigMapper.insert(para) == 1;
	}

    @Override
    public Boolean updateByName(SystemConfigModel para) {
        Boolean b = systemConfigMapper.updateByName(para) == 1;
        return b;
    }

	@Override
	public String queryConfigValue(String paraName) {
		return systemConfigMapper.getSysConfigValue(paraName);
	}

	@Override
	public List<SystemConfigModel> queryPara() {
		return systemConfigMapper.getSysConfig();
	}
}
