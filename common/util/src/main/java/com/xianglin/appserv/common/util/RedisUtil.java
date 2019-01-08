/**
 *
 */
package com.xianglin.appserv.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.xianglin.fala.session.RedisClient;
import com.xianglin.fala.session.RedisClient.RedisOperation;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

/**
 * @author zhangyong 2016年10月11日下午1:41:13
 */
public class RedisUtil {

    /**
     * 红包推荐成功发送奖励提醒
     */
    public final static String ACTIVITY_INVITE_POINT_ALERT = "activity_invite_point_alert";

    @Resource
    private RedisClient redisClient;

    /**
     * 存放数据
     *
     * @param key
     * @param timeout
     * @return
     */
    public Boolean hmset(final String key, final Map<String, Object> map, final int timeout) {
        Boolean result = redisClient.execute(new RedisOperation<Boolean>() {

            @Override
            public Boolean execute(ShardedJedis sharededJedis) {
                Map<String, String> hmap = new HashMap<>(map.size());
                List<String> deleteAttributeList = new ArrayList<String>();
                for (String key : map.keySet()) {
                    Object value = map.get(key);
                    if (value == null) {
                        deleteAttributeList.add(key);
                    }
                    if (value instanceof String) {
                        hmap.put(key, (String) value);
                    } else {
                        hmap.put(key, JSON.toJSONString(value));
                    }
                }
                ShardedJedisPipeline pipeline = sharededJedis.pipelined();
                if (deleteAttributeList.size() > 0) {
                    pipeline.hdel(key,
                            deleteAttributeList.toArray(new String[deleteAttributeList.size()]));
                }
                pipeline.hmset(key, hmap);
                pipeline.sync();
                if (timeout > 0) {
                    sharededJedis.expire(key, timeout);
                }

                return true;
            }

        });
        return result;
    }

    public String get(final String key) {
        return redisClient.execute(new RedisOperation<String>() {

            @Override
            public String execute(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }
        });
    }

    public String hget(final String key, final String field) {
        return redisClient.execute(new RedisOperation<String>() {

            @Override
            public String execute(ShardedJedis shardedJedis) {
                return shardedJedis.hget(key, field);
            }
        });
    }


    public Map<String, String> hmgetAll(final String key) {

        Map<String, String> result = redisClient.execute(new RedisOperation<Map<String, String>>() {

            @Override
            public Map<String, String> execute(ShardedJedis sharededJedis) {
                return sharededJedis.hgetAll(key);
            }
        });
        return result;
    }

    public String pop(final String key) {
        return redisClient.execute(new RedisOperation<String>() {

            @Override
            public String execute(ShardedJedis shardedJedis) {
                return shardedJedis.lpop(key);
            }
        });
    }

    public void push(final String key, final int timeout, final String... values) {
        redisClient.execute(new RedisOperation<Map<String, String>>() {

            @Override
            public Map<String, String> execute(ShardedJedis shardedJedis) {
                ShardedJedisPipeline pipeline = shardedJedis.pipelined();
                int i = 0;
                for (String value : values) {
                    if (!StringUtils.isEmpty(value)) {
                        pipeline.rpush(key, value);
                        i++;
                    }
                }
                shardedJedis.expire(key, timeout);
                if (i > 0) {
                    pipeline.syncAndReturnAll();
                }
                return null;
            }
        });
    }


    public Boolean hdelete(final String key, final String... fields) {

        return redisClient.execute(new RedisOperation<Boolean>() {

            @Override
            public Boolean execute(ShardedJedis shardedJedis) {
                Long num = shardedJedis.hdel(key, fields);
                return num == fields.length;
            }
        });
    }

    /**
     * 删除和key相关的数据
     *
     * @param key
     * @return
     */
    public Boolean delete(final String key) {
        return redisClient.execute(new RedisOperation<Boolean>() {

            @Override
            public Boolean execute(ShardedJedis shardedJedis) {
                Long num = shardedJedis.del(key);
                return 1 == num;
            }
        });
    }

    public Boolean add(final String key, final String value, final int timeout) {
        return redisClient.execute(new RedisOperation<Boolean>() {

            @Override
            public Boolean execute(ShardedJedis shardedJedis) {
                String result = shardedJedis.set(key, value);
                if (0 < timeout) {
                    shardedJedis.expire(key, timeout);
                }
                return "OK".equals(result);
            }
        });
    }

    /**
     * 是否已经准备好继续操作
     * 当增加时，step为正，count为正
     * 当减少时，step为负，count此时根据业务场景设定具体的值
     * <br>eg.判断集合中是否还有数量时，此时，redis的值必须大于0，每步减少1，则count为0就可以
     *
     * @param key
     * @param step    步长
     * @param count   比较值
     * @param timeout 小于零时，没有超时时间
     * @return
     */
    public Boolean isReady(final String key, final Integer step, final Integer count, final int timeout) {
        return redisClient.execute(new RedisOperation<Boolean>() {

            @Override
            public Boolean execute(ShardedJedis shardedJedis) {
                Long result;
                if (step != null) {
                    result = shardedJedis.incrBy(key, step);
                } else {
                    result = shardedJedis.incr(key);
                }
                if (timeout > 0) {
                    shardedJedis.expire(key, timeout);
                }
                return result.intValue() >= count;
            }
        });
    }

    public boolean sismember(final String key, final String member) {
        return redisClient.execute(new RedisOperation<Boolean>() {

            @Override
            public Boolean execute(ShardedJedis shardedJedis) {
                return shardedJedis.sismember(key, member);
            }
        });
    }

    /**
     * 往集合中添加对象
     *
     * @param key
     * @param timeout
     * @param member
     */
    public void sadd(final String key, final int timeout, final String... member) {
        redisClient.execute(new RedisOperation<Long>() {

            @Override
            public Long execute(ShardedJedis shardedJedis) {
                Long value = shardedJedis.sadd(key, member);
                if (timeout > 0) {
                    shardedJedis.expire(key, timeout);
                }
                return value;
            }
        });
    }

    /**
     * 返回集合中的所有元素
     *
     * @param key
     * @return
     */
    public Set<String> smembers(final String key) {
        return redisClient.execute(new RedisOperation<Set<String>>() {
            @Override
            public Set<String> execute(ShardedJedis shardedJedis) {
                return shardedJedis.smembers(key);
            }
        });
    }

    /**
     * 删除集合中的一个或多个元素
     *
     * @param key
     * @param members
     * @return
     */
    public Long srem(final String key, final String[] members) {
        return redisClient.execute(new RedisOperation<Long>() {
            @Override
            public Long execute(ShardedJedis shardedJedis) {
                return shardedJedis.srem(key, members);
            }
        });
    }


    public boolean isExist(final String key) {
        return redisClient.execute(new RedisOperation<Boolean>() {

            @Override
            public Boolean execute(ShardedJedis shardedJedis) {
                return shardedJedis.exists(key);
            }
        });
    }

    public boolean hExist(final String key, final String field) {
        return redisClient.execute(new RedisOperation<Boolean>() {

            @Override
            public Boolean execute(ShardedJedis shardedJedis) {
                return shardedJedis.hexists(key, field);
            }
        });
    }

    /**
     * 是否重复
     *
     * @param key 唯一值
     * @param timeoutInSecond 以秒为单位
     * @return true 重复，false：不重复
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
            result = false;// 发生异常时默认不重复，防止redis异常影响正常业务逻辑
        }
        return result;
    }


}
