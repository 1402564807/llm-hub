package com.xermao.llmhub.proxy.scheduled

import com.xermao.llmhub.proxy.cache.GlobalCache
import com.xermao.llmhub.provider.domain.model.Provider
import jakarta.annotation.PostConstruct
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class GroupCacheScheduled(
    private val globalCache: GlobalCache,
    private val sqlClient: KSqlClient
) {

    private val log = LoggerFactory.getLogger(GroupCacheScheduled::class.java)

    @PostConstruct
    @Scheduled(cron = "0 */10 * * * *")
    fun job() {
        val providers = sqlClient.createQuery(Provider::class) { select(table) }.execute()

        if (providers.isEmpty()) {
            log.info("service provider is empty, skipping refresh group cache...")
            return
        }

        providers.forEach { provider ->
            provider.group.forEach { g ->
                provider.models.forEach { model ->

                    val targetModel = provider.modelMap.filterValues { it == model }.keys.firstOrNull() ?: model

                    globalCache.groupCache
                        .get(g) { mutableMapOf() }
                        .getOrPut(targetModel) { mutableListOf() }
                        .add(provider)
                    log.info(
                        "Group2Model2Provider 缓存路径 group: {} -> model: {} -> provider: {} 已刷新",
                        g,
                        targetModel,
                        provider.id
                    )
                }
            }
        }
    }

}
