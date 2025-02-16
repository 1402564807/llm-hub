package com.xermao.llmhub.config

import com.xermao.llmhub.cache.GlobalCache
import com.xermao.llmhub.security.converter.AuthenticationConverter
import com.xermao.llmhub.security.filter.CacheRequestBodyFilter
import com.xermao.llmhub.security.filter.ReactiveRequestContextFilter
import com.xermao.llmhub.security.filter.TokenAuthenticationFilter
import com.xermao.llmhub.security.handler.AuthenticationDeniedHandler
import com.xermao.llmhub.security.handler.AuthenticationEntryPoint
import com.xermao.llmhub.security.handler.AuthenticationFailureHandler
import com.xermao.llmhub.security.handler.AuthenticationSuccessHandler
import com.xermao.llmhub.security.manage.AuthenticationManager
import com.xermao.llmhub.security.manage.AuthorizationManager
import com.xermao.llmhub.security.service.TokenDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        tokenService: TokenDetailsServiceImpl,
        globalCache: GlobalCache
    ): SecurityWebFilterChain {
        val authenticationManager = AuthenticationManager(tokenService)
        val authenticationFilter = TokenAuthenticationFilter(authenticationManager)
        val matchers = ServerWebExchangeMatchers.pathMatchers("/v1/**")
        authenticationFilter.setServerAuthenticationConverter(AuthenticationConverter())
        authenticationFilter.setAuthenticationSuccessHandler(AuthenticationSuccessHandler())
        authenticationFilter.setAuthenticationFailureHandler(AuthenticationFailureHandler())
        authenticationFilter.setRequiresAuthenticationMatcher(matchers)

        http.csrf { it.disable() }
            .cors { it.disable() }
            .logout { it.disable() }
            .httpBasic { it.disable() }
            .addFilterAt(CacheRequestBodyFilter(matchers), SecurityWebFiltersOrder.FIRST)
            .addFilterAt(ReactiveRequestContextFilter(), SecurityWebFiltersOrder.REACTOR_CONTEXT)
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
            .authorizeExchange {
                // 授权，判断用户是否有权限。
                it.pathMatchers("/v1/**").access(AuthorizationManager(globalCache))
            }
            .authenticationManager(authenticationManager)
            .exceptionHandling {
                it.accessDeniedHandler(AuthenticationDeniedHandler())
                it.authenticationEntryPoint(AuthenticationEntryPoint())
            }

        return http.build()
    }

}