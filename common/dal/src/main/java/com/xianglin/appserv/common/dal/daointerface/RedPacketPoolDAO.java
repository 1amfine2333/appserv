package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xianglin.appserv.common.dal.dataobject.RedPacketPool;
@Repository
public interface RedPacketPoolDAO extends BaseDAO<RedPacketPool> {
	
	List<RedPacketPool> getRedPacketPool(Map<String,Object> params);
	
	int expiredPacketPool(Map<String,Object> params);
	
	int batchInsert(List<RedPacketPool> list);
	
	int selectCount(Map<String,Object> params);
	
	int updateRedPacketPool(Map<String,Object> params);
	
	Long generateRedPacketSeq();
	
	List<RedPacketPool> getParticipateUser(Map<String,Object> params);
}