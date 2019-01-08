/**
 * 
 */
package com.xianglin.appserv.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.RedisClient;
import com.xianglin.fala.session.RedisClient.RedisOperation;

import redis.clients.jedis.ShardedJedis;

/**
 * 幂等性校验器（可用于重复提交校验）
 * 
 * @author pengpeng 2016年3月24日下午2:20:52
 */
public class IdempotentChecker {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(IdempotentChecker.class);

	/** 默认超时时间 */
	private static final int DEFAULT_TIMEOUT = 10;

	/** redisClient */
	private RedisClient redisClient;

	/**
	 * 是否不重复
	 * 
	 * @param key
	 * @return
	 */
	public boolean isNotRepeat(String key) {
		return isNotRepeat(key, DEFAULT_TIMEOUT);
	}

	/**
	 * 是否不重复
	 * 
	 * @param key
	 * @param timeoutInSecond
	 * @return
	 */
	public boolean isNotRepeat(String key, int timeoutInSecond) {
		return !isRepeat(key, timeoutInSecond);
	}

	/**
	 * 是否重复
	 * 
	 * @param key
	 * @return
	 */
	public boolean isRepeat(String key) {
		return isRepeat(key, DEFAULT_TIMEOUT);
	}

	/**
	 * 是否重复
	 * 
	 * @param key
	 * @param timeoutInSecond
	 * @return
	 */
	public boolean isRepeat(final String key, final int timeoutInSecond) {
		boolean result = false;
		try {
			result = redisClient.execute(new RedisOperation<Boolean>() {
				@Override
				public Boolean execute(ShardedJedis shardedJedis) {
					long value = shardedJedis.incr(key);
					shardedJedis.expire(key, timeoutInSecond);
					return value > 1;
				}
			});
		} catch (Throwable e) {
			logger.error("isRepeat error!", e);
			result = false;// 发生异常时默认不重复，防止redis异常影响正常业务逻辑
		}
		return result;
	}

	/**
	 * @param redisClient
	 *            the redisClient to set
	 */
	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}
}
