/**
 *
 */
package com.xianglin.appserv.biz.shared.util;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


/**
 * 返回接口，带缓存
 *
 * @author wanglei
 * @date
 */
@Service("responseCacheUtils")
public class ResponseCacheUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResponseCacheUtils.class);

    @Autowired
    private RedissonClient redissonClient;

    private final String DELIMITER = "-";

    private final String SYSTER_NAME = "APP";

    private final String SYSTEM_LOCK_FRIFIX = "APP:SYSTEM:LOCK_";


    /**
     * 业务执行返回
     *
     * @param cacheKey 缓存key
     * @param subKey   调整key值，拼接在cacheKey之后
     * @param exec
     * @param <T>
     * @return
     */
    public <T> Response<T> executeCacheResponse(ResponseCacheKey cacheKey, String subKey, Supplier<T> exec) {
        Response<T> resp = new Response<>(null);
        try {
            String key = SYSTER_NAME + DELIMITER + cacheKey.name() + DELIMITER + subKey;
            logger.info("get cache data key:{}", key);
            RBucket<String> bucket = redissonClient.getBucket(key);
            String value = bucket.get();
            if (StringUtils.isNotEmpty(value)) {
                resp = JSON.parseObject(value, Response.class);
                long remainTime = bucket.remainTimeToLive();
                if(remainTime < cacheKey.remainTtime * 1000){
                    bucket.set(JSON.toJSONString(this.execute(exec)), cacheKey.timeout, TimeUnit.SECONDS);
                }
            } else {
                resp = this.execute(exec);
                bucket.set(JSON.toJSONString(resp), cacheKey.timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.warn("executeCacheResponse", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 清理缓存
     *
     * @param cacheKey
     */
    public void refreshCache(ResponseCacheKey cacheKey) {
        String key = SYSTER_NAME + DELIMITER + cacheKey.name() + DELIMITER;
        logger.info("begin refreshCache {}*", key);
        redissonClient.getKeys().getKeysByPattern(key + "*").forEach(v -> {
            logger.info(" refreshCache key {} ", v);
            redissonClient.getBucket(v).expireAt(10L);
        });
    }

    /**
     * 带锁方式执行方法
     *
     * @param lockKey
     * @param func
     * @param <T>
     * @return
     */
    public <T> Response<T> executeCacheLock(String lockKey, Supplier<T> func) {
        Response<T> resp = new Response<>(null);
        if (StringUtils.isEmpty(lockKey)) {
            return Response.ofFail("请求参数错误");
        }
        RLock lock = redissonClient.getFairLock(SYSTEM_LOCK_FRIFIX + lockKey);
        try {
            if (!lock.tryLock()) {
                throw new BusiException(FacadeEnums.RETURN_EMPTY);
            }
            resp.setResult(func.get());
        } catch (BusiException e) {
            logger.warn("BusiException", e);
            resp.setFacade(e.getFacadeEnums());
        } catch (Exception e) {
            logger.warn("", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return resp;
    }

    /**
     * 带锁方式执行方法
     *
     * @param func
     * @param <T>
     * @return
     */
    public <T> Response<T> execute(Supplier<T> func) {
        Response<T> resp = new Response<>(null);
        try {
            resp.setResult(func.get());
        } catch (BusiException e) {
            logger.warn("BusiException", e);
            resp.setFacade(e.getFacadeEnums());
        } catch (Exception e) {
            logger.warn("", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 缓存key
     */
    public enum ResponseCacheKey {

        INDEX_NEWS("首页新闻消息", 3600L,10L),

        ARTICLE_POPULAR("热门微博缓存", 60L,10L),

        RECRUIT_LIST("招工列表缓存", 3600L,10);

        private String desc;

        /**
         * 缓存时间
         */
        private long timeout;

        /**
         * 缓存剩余最小时间，
         * 当缓存数据不足此时间即触发更新缓存
         */
        private long remainTtime;

        ResponseCacheKey(String desc, long timeout,long remainTtime) {
            this.desc = desc;
            this.timeout = timeout;
            this.remainTtime = remainTtime;
        }
    }

}
