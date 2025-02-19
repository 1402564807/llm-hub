package com.xermao.llmhub.common.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@EnableCaching
@Configuration
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = SimpleCacheManager()
        cacheManager.setCaches(listOf<Cache>(verifyCodeCache()))
        return cacheManager
    }

    private fun verifyCodeCache(): CaffeineCache {
        val cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(1))
            .recordStats()
            .build<Any, Any>()
        return CaffeineCache(VERIFY_CODE, cache)
    }

    companion object {
        const val VERIFY_CODE: String = "verifyCode"
    }
}
