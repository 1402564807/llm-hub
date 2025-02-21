package com.xermao.llmhub.proxy.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.xermao.llmhub.price.model.ModelPrice
import com.xermao.llmhub.provider.model.ServiceProvider
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class GlobalCache {

    val groupCache: Cache<String, MutableMap<String, MutableList<ServiceProvider>>> = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(20))
        .expireAfterWrite(Duration.ofMinutes(20))
        .recordStats()
        .build()

    val priceCache: Cache<String, MutableMap<String, ModelPrice>> = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(20))
        .expireAfterWrite(Duration.ofMinutes(20))
        .recordStats()
        .build()


    fun refreshGroupCache() {

//        val execute = sqlClient.createQuery(ServiceProvider::class) {
//            whereIfNotNull(key) {
//                sql(Boolean::class, "%v == ANY(%e)") {
//                    expression(table.group)
//                    value(key!!)
//                }
//            }
//            select(table)
//        }.execute()

//        val execute = sqlClient.createQuery(ServiceProvider::class) { select(table) }.execute()
//
//        if (execute.isEmpty()) {
//            log.info("service provider is empty, skipping refresh group cache...")
//            return
//        }
//
//        execute.forEach { provider ->
//            provider.group.forEach { g ->
//                provider.models.forEach { model ->
//
//                    val targetModel = provider.modelMap.filterValues { it == model }.keys.firstOrNull()?:model
//
//                    groupCache
//                        .get(g) { mutableMapOf() }
//                        .getOrPut(targetModel) { mutableListOf() }
//                        .add(provider)
//                    log.info("Group2Model2Provider 缓存路径 group: {} -> model: {} -> provider: {} 已刷新", g, targetModel, provider.id)
//                }
//            }
//        }

    }
}
