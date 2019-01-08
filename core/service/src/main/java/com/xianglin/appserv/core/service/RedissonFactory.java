package com.xianglin.appserv.core.service;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class RedissonFactory {
	private boolean single;
	private String redisAddress;
	private RedissonClient redissonClient;
	
	public void setSingle(boolean single) {
		this.single = single;
	}

	public void setRedisAddress(String redisAddress) {
		this.redisAddress = redisAddress;
	}

	public RedissonClient getRedissonClient() {
		if(single){
			return redissonClient;
		}
		
		Config config = new Config();
        config.useSingleServer().setAddress(redisAddress);
		return Redisson.create(config);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(redisAddress, "redis连接信息不能为空");
		Config config = new Config();
        config.useSingleServer().setAddress(redisAddress);
        this.redissonClient = Redisson.create(config);
	}
	
	public void shutdown(){
		this.redissonClient.shutdown();
	}
	
}
