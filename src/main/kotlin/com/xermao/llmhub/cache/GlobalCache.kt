package com.xermao.llmhub.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import com.github.benmanes.caffeine.cache.RemovalListener
import com.xermao.llmhub.model.entity.ServiceProvider
import com.xermao.llmhub.model.entity.group
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.sql
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class GlobalCache(private val sqlClient: KSqlClient) {

    private val log = LoggerFactory.getLogger(GlobalCache::class.java)

    val groupCache: Cache<String, MutableMap<String, MutableList<ServiceProvider>>> = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofHours(1))
        .expireAfterWrite(Duration.ofHours(1))
        .removalListener(RemovalListener<String, MutableMap<String, MutableList<ServiceProvider>>> { key, _, cause ->
            refreshCache(key, cause)
        })
        .recordStats()
        .build()


    private fun refreshCache(key: String?, cause: RemovalCause) {
        if (key == null) return

        if (cause != RemovalCause.COLLECTED && cause != RemovalCause.EXPIRED) {
            log.info("Group2Model2Provider 缓存 {} 被移除, 原因 {}", key, cause)
            return
        }

        val execute = sqlClient.createQuery(ServiceProvider::class) {
            where(sql(Boolean::class, "%v == ANY(%e)") {
                expression(table.group)
                value(key)
            })
            select(table)
        }.execute()

        if (execute.isEmpty()) {
            return
        }

        execute.forEach { provider ->
            provider.group.forEach { g ->
                provider.models.forEach { model ->
                    groupCache
                        .get(g) { mutableMapOf() }
                        .getOrPut(model) { mutableListOf() }
                        .add(provider)
                    log.info("Group2Model2Provider 缓存路径 {} -> {} -> {} 已刷新", g, model, provider)
                }
            }
        }

    }
}