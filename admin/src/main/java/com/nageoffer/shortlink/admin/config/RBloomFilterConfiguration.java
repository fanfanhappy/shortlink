package com.nageoffer.shortlink.admin.config;

/**
 * @projectName: shortlink
 * @package: com.nageoffer.shortlink.admin.config
 * @className: RBloomFilterConfiguration
 * @author: 姬紫衣
 * @description: TODO
 * @date: 2024/7/30 16:56
 * @version: 1.0
 */

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置
 */
@Configuration
public class RBloomFilterConfiguration {

    /**
     * 防止用户注册查询数据库的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userRegisterCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(100000000L, 0.001);
        return cachePenetrationBloomFilter;
    }
}
