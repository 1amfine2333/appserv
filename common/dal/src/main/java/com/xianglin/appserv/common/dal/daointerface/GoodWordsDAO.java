package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xianglin.appserv.common.dal.dataobject.GoodWords;

@Repository
public interface GoodWordsDAO  extends BaseDAO<GoodWords>{
	
	List<GoodWords> getGoodWords(Map<String,Object> param);
	
	int updateGoodStates(GoodWords good);
	
}