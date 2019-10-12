package org.yui.redis.config;

import org.yui.threadpool.config.ThreadPoolConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.yui.base.bean.constant.StringConstant;
import org.yui.redis.bean.CustomJacksonCodec;

import javax.annotation.Resource;

/**
 * @author huangjinlong
 *
 * 开启分布式session
 *
 * 开启cache
 */
@Log4j2
@Configuration
@AutoConfigureAfter(value = ThreadPoolConfig.class)
public class RedisConfig {

    @Autowired
    private Environment environment;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static final String REDIS_CLUSTER_PREFIX = "redis://";

    /**
     *
     * @return
     */
    @Bean
    public RedissonSpringCacheManager redissonSpringCacheManager() {
        RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redissonClient());
        redissonSpringCacheManager.setCodec(CustomJacksonCodec.INSTANCE);
        return redissonSpringCacheManager;
    }

    /**
     *
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {

        Config config = new Config();
        config.setCodec(CustomJacksonCodec.INSTANCE);
        config.setTransportMode(TransportMode.NIO);
        config.setExecutor(threadPoolTaskExecutor.getThreadPoolExecutor());

        /**
         * 先判断redis单例
         */
        String singleAddress = environment.getProperty("spring.redis.host");
        String singlePort = environment.getProperty("spring.redis.port","6379");
        if (StringUtils.isNotBlank(singleAddress)) {

            SingleServerConfig singleServerConfig = config.useSingleServer().setAddress(
                    REDIS_CLUSTER_PREFIX + singleAddress + StringConstant.COLON + singlePort);

            {
                String value = environment.getProperty("spring.redis.password");
                if (StringUtils.isNotBlank(value)) {
                    singleServerConfig.setPassword(value);
                }
            }
            {
                String value = environment.getProperty("spring.redis.timeout");
                if (StringUtils.isNotBlank(value)) {
                    singleServerConfig.setTimeout(Integer.parseInt(value));
                }
            }
            {
                String value = environment.getProperty("spring.redis.pool.min-idle","8");
                singleServerConfig.setConnectionMinimumIdleSize(Integer.parseInt(value));
            }
            {
                String value = environment.getProperty("spring.redis.pool.max-idle","16");
                singleServerConfig.setConnectionPoolSize(Integer.parseInt(value));
            }
            {
                String value = environment.getProperty("spring.redis.database","0");
                singleServerConfig.setDatabase(Integer.parseInt(value));
            }
        } else {
            /**
             * 再判断redis集群
             */
            String clusterNodes = environment.getProperty("spring.redis.cluster.nodes");
            if (StringUtils.isNotBlank(clusterNodes)) {
                ClusterServersConfig clusterServersConfig = config.useClusterServers();
                for (String address : clusterNodes.split(StringConstant.GENERAL_COMMA_SPLIT)) {
                    clusterServersConfig.addNodeAddress(REDIS_CLUSTER_PREFIX + address);
                }
            }
        }
        return Redisson.create(config);
    }
}
