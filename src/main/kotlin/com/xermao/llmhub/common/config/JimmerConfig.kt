package com.xermao.llmhub.common.config

import org.babyfish.jimmer.meta.ImmutableProp
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.sql.cache.Cache
import org.babyfish.jimmer.sql.cache.caffeine.CaffeineValueBinder
import org.babyfish.jimmer.sql.cache.chain.ChainCacheBuilder
import org.babyfish.jimmer.sql.kt.cache.KCacheFactory
import org.babyfish.jimmer.sql.meta.DatabaseNamingStrategy
import org.babyfish.jimmer.sql.runtime.DefaultDatabaseNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class JimmerConfig {

    @Bean
    fun databaseNamingStrategy(): DatabaseNamingStrategy =
        DefaultDatabaseNamingStrategy.LOWER_CASE

    @Bean
    fun cacheFactory(): KCacheFactory =
        object : KCacheFactory {
            override fun createObjectCache(type: ImmutableType): Cache<*, *> =
                ChainCacheBuilder<Any, Any>()
                    .add(
                        CaffeineValueBinder
                            .forObject<Any, Any>(type)
                            .maximumSize(1024)
                            .duration(Duration.ofMinutes(10))
                            .build()
                    )
                    .build()

            override fun createAssociatedIdCache(prop: ImmutableProp): Cache<*, *> =
                createPropCache(prop, Duration.ofMinutes(10))

            override fun createAssociatedIdListCache(prop: ImmutableProp): Cache<*, List<*>> =
                ChainCacheBuilder<Any, List<*>>()
                    .add(
                        CaffeineValueBinder
                            .forProp<Any, List<*>>(prop)
                            .maximumSize(512)
                            .duration(Duration.ofMinutes(5))
                            .build()
                    )
                    .build()

            // 计算属性缓存
            override fun createResolverCache(prop: ImmutableProp): Cache<*, *> =
                createPropCache(prop, Duration.ofMinutes(5))

            private fun createPropCache(prop: ImmutableProp, duration: Duration): Cache<*, *> =
                ChainCacheBuilder<Any, Any>()
                    .add(
                        CaffeineValueBinder
                            .forProp<Any, Any>(prop)
                            .maximumSize(512)
                            .duration(duration)
                            .build()
                    )
                    .build()
        }
}