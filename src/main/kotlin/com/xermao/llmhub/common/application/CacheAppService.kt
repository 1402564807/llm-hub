package com.xermao.llmhub.common.application

import com.xermao.llmhub.common.cache.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CacheAppService {
    @Cacheable(value = [CacheConfig.VERIFY_CODE], key = "{#identify}", unless = "#result == null")
    fun getVerifyCodeBy(identify: String?): String? {
        return null
    }

    @CachePut(value = [CacheConfig.VERIFY_CODE], key = "{#identify}")
    fun upsertVerifyCodeBy(identify: String?, value: String): String {
        return value
    }

    @CacheEvict(value = [CacheConfig.VERIFY_CODE], key = "{#identify}")
    fun removeVerifyCodeBy(identify: String?) {
    }

    @CacheEvict(value = [CacheConfig.VERIFY_CODE], allEntries = true)
    fun clearAllVerifyCode() {
    }
}
