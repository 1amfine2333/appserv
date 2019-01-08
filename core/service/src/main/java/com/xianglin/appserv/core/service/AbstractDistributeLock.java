package com.xianglin.appserv.core.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.util.ApplicationContextUtil;
/**
 * redis 分布式锁基类
 * @author gengchaogang
 * @dateTime 2017年1月3日 上午10:56:58
 *
 * @param <P>	入参
 * @param <T>	结果
 */
public abstract class AbstractDistributeLock<P,T> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractDistributeLock.class);

	/**
	 * 同步锁
	 * @author gengchaogang
	 * @dateTime 2017年1月3日 上午11:15:14
	 * @param lockKey
	 * @param p
	 * @return
	 * @throws BusiException
	 */
	public final T synLock(String lockKey,P p) throws BusiException{
		RedissonFactory redissonFactory = ApplicationContextUtil.getBean("redissonFactory", RedissonFactory.class);
		RReadWriteLock rReadWriteLock = redissonFactory.getRedissonClient().getReadWriteLock(lockKey);
		T t = null;
		try {
			logger.debug("{} get lock start ..",lockKey);
			rReadWriteLock.writeLock().tryLock(30, TimeUnit.SECONDS);
			logger.debug("{} has lock",lockKey);
			t = process(p);
		} catch (InterruptedException e) {
			throw new BusiException(ResponseEnum.BUSI_INVALD, "获取redis锁超时");
		} finally {
			rReadWriteLock.writeLock().unlock();
			rReadWriteLock.expire(30, TimeUnit.SECONDS);
			logger.debug("{} release lock",lockKey);
		}
		return t;
	}
	/**
	 * 核心业务处理
	 * @author gengchaogang
	 * @dateTime 2017年1月3日 上午10:56:50
	 * @param p
	 * @return
	 * @throws BusiException
	 */
	abstract T process(P p) throws BusiException;
}
