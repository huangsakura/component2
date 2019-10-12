package org.yui.redis.bean.constant;

/**
 * @author huangjinlong
 * @time 2019-03-14 19:51
 * @description
 */
public interface RedisConstant {

    /**
     * 默认等待时长，单位秒
     */
    long DEFAULT_WAIT_TIME = 5;

    /**
     * 默认释放时间，单位秒
     */
    long DEFAULT_LEASE_TIME = 60;

    /**
     * redis 锁的key的前缀
     */
    String REDIS_LOCK_PREFIX = "LOCK_";
}
