package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;

import com.xianglin.appserv.common.dal.dataobject.AppCommuseMenuModel;

public interface AppCommuseMenuDAO {
    
	public List<AppCommuseMenuModel> queryTopUserCommMenu(AppCommuseMenuModel appCommuseMenuModel);
	
	public List<AppCommuseMenuModel> queryTopCommMenu(AppCommuseMenuModel appCommuseMenuModel);
}