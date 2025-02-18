package com.xermao.llmhub.scheduled

import com.xermao.llmhub.common.cache.GlobalCache
import com.xermao.llmhub.model.entity.ServiceProvider
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
        val serviceProviders = sqlClient.createQuery(ServiceProvider::class) { select(table) }.execute()

        if (serviceProviders.isEmpty()) {
            log.info("service provider is empty, skipping refresh group cache...")
            return
        }

        serviceProviders.forEach { provider ->
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