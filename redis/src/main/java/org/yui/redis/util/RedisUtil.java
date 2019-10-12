package org.yui.redis.util;

import org.yui.base.exception.BusinessException;
import org.yui.redis.bean.constant.RedisConstant;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.NotBlank;
import org.redisson.api.RLock;
import org.yui.base.bean.constant.StringConstant;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjinlong
 * @time 2019-03-15 09:45
 * @description
 */
@Log4j2
public abstract class RedisUtil {

    /**
     * redis 锁的key的前缀
     */
    @Deprecated
    private static final String REDIS_LOCK_PREFIX = "REDIS_LOCK_";

    /**
     * 组装 redis lock key
     * @param redisKeyBody
     * @param salt
     * @return
     */
    @Deprecated
    public static String packageRedisLockKey(@NotBlank String redisKeyBody, @NotBlank String salt) {
        return REDIS_LOCK_PREFIX + redisKeyBody + StringConstant.UNDERLINE + salt;
    }

    /**
     *
     * @param prefix
     * @param salt
     * @return
     */
    public static String packageRedissonRokkuKey(@NotBlank String prefix, @NotBlank String salt) {
        return RedisConstant.REDIS_LOCK_PREFIX + prefix + StringConstant.UNDERLINE + salt;
    }


    /**
     *
     * @param rLock
     * @param waitTime
     * @param leaseTime
     */
    public static void lock(RLock rLock,long waitTime,long leaseTime) {
        try {
            if (!rLock.tryLock(waitTime,leaseTime, TimeUnit.SECONDS)) {
                log.error("请求redis锁超时，key:{},waitTime:{}",rLock.getName(),waitTime);
                throw new BusinessException("OVERTIME_WHEN_ACQUIRE_LOCK","操作失败",false);
            }
            log.info("获取redis锁成功,key:{},waitTime:{},leaseTime:{},threadId:{}",
                    rLock.getName(),waitTime,leaseTime,Thread.currentThread().getId());
        } catch (InterruptedException e) {
            log.error("获取redis锁失败:{}",e.getMessage());
            throw new BusinessException("ACQUIRE_LOCK_INTERRUPTED","操作失败",false);
        }
    }


    /**
     *
     * @param rLock
     */
    public static void lock(RLock rLock) {
        lock(rLock,RedisConstant.DEFAULT_WAIT_TIME,RedisConstant.DEFAULT_LEASE_TIME);
    }


    /**
     *
     * @param waitTime
     * @param leaseTime
     */
    @Deprecated
    public static void validateLockTime(long waitTime,long leaseTime) {
    }

    /**
     *
     * @param rLock
     */
    public static void unlock(@Nullable RLock rLock) {
        if (null != rLock) {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                log.info("释放redis锁成功,key:{}", rLock.getName());
            } else {
                log.info("当前锁不被当前线程持有,故不做解锁操作,key:{}", rLock.getName());
            }
        }
    }
}
