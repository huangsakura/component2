package org.yui.redis.bean.service;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Getter
@Setter
public final class RedisRokkuInfo implements Serializable {

    /**
     * redis的锁的key
     */
    @NotBlank
    private String key;

    /**
     * 等待时间，单位秒
     */
    private long waitTime;

    /**
     * 锁的自动释放时间，单位秒
     */
    private long leaseTime;

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RedisRokkuInfo)) {
            return false;
        }
        RedisRokkuInfo redisRokkuInfo = (RedisRokkuInfo)obj;
        return this.key.equals(redisRokkuInfo.getKey());
    }
}
