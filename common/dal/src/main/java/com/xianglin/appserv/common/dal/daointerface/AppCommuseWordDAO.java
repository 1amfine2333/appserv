package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;

import com.xianglin.appserv.common.dal.dataobject.AppCommuseWord;

public interface AppCommuseWordDAO {
	
	public List<AppCommuseWord> queryTopUserCommWord(AppCommuseWord appCommuseWord);
	
	public List<AppCommuseWord> queryTopCommWord(AppCommuseWord appCommuseWord);
}