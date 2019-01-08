package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xianglin.appserv.common.dal.dataobject.PermanetCalendar;


@Repository
public interface PermanetCalendarDAO  extends BaseDAO<PermanetCalendar>{
	
	List<PermanetCalendar> getPermanetCalendarList(Map<String,Object> param);
	
	
}