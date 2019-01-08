/**
 * 
 */
package com.xianglin.appserv.core.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.core.service.QRCodeTokenService;
import com.xianglin.fala.session.RedisClient;
import com.xianglin.fala.session.RedisClient.RedisOperation;

import redis.clients.jedis.ShardedJedis;

/**
 * 二维码token相关服务实现类
 * 
 * @author pengpeng 2016年3月23日上午11:33:34
 */
public class QRCodeTokenServiceImpl implements QRCodeTokenService {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(QRCodeTokenServiceImpl.class);

	/** redisClient */
	private RedisClient redisClient;

	/** tokenTimeout */
	private int tokenTimeoutInSeconds;

	/** keyPrefix */
	private String keyPrefix;

	/**
	 * @see com.xianglin.appserv.core.service.QRCodeTokenService#getUserToken(java.lang.String)
	 */
	@Override
	public String getUserToken(final String figureId) {
		String token = UUID.randomUUID().toString();
		final String key = keyPrefix + ":" + token;
		try {
			redisClient.execute(new RedisOperation<String>() {
				@Override
				public String execute(ShardedJedis shardedJedis) {
					shardedJedis.set(key, figureId);
					shardedJedis.expire(key, tokenTimeoutInSeconds);
					return null;
				}
			});
		} catch (Throwable e) {
			logger.error("getUserToken error!", e);
			token = null;
		}

		return token;
	}

	/**
	 * @see com.xianglin.appserv.core.service.QRCodeTokenService#getGroupToken(java.lang.String)
	 */
	@Override
	public String getGroupToken(final String groupId) {
		String token = UUID.randomUUID().toString();
		final String key = keyPrefix + ":" + token;
		try {
			redisClient.execute(new RedisOperation<String>() {
				@Override
				public String execute(ShardedJedis shardedJedis) {
					shardedJedis.set(key, groupId);
					shardedJedis.expire(key, tokenTimeoutInSeconds);
					return null;
				}
			});
		} catch (Throwable e) {
			logger.error("getGroupToken error!", e);
			token = null;
		}

		return token;
	}

	/**
	 * @see com.xianglin.appserv.core.service.QRCodeTokenService#getTokenInfo(java.lang.String)
	 */
	@Override
	public String getTokenInfo(String token) {
		String result = null;
		final String key = keyPrefix + ":" + token;
		try {
			result = redisClient.execute(new RedisOperation<String>() {
				@Override
				public String execute(ShardedJedis shardedJedis) {
					return shardedJedis.get(key);
				}
			});
		} catch (Throwable e) {
			logger.error("getTokenInfo error!", e);
		}
		return result;
	}

	/**
	 * @return the redisClient
	 */
	public RedisClient getRedisClient() {
		return redisClient;
	}

	/**
	 * @param redisClient
	 *            the redisClient to set
	 */
	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

	/**
	 * @return the tokenTimeoutInSeconds
	 */
	public int getTokenTimeoutInSeconds() {
		return tokenTimeoutInSeconds;
	}

	/**
	 * @param tokenTimeoutInSeconds
	 *            the tokenTimeoutInSeconds to set
	 */
	public void setTokenTimeoutInSeconds(int tokenTimeoutInSeconds) {
		this.tokenTimeoutInSeconds = tokenTimeoutInSeconds;
	}

	/**
	 * @return the keyPrefix
	 */
	public String getKeyPrefix() {
		return keyPrefix;
	}

	/**
	 * @param keyPrefix
	 *            the keyPrefix to set
	 */
	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

}
