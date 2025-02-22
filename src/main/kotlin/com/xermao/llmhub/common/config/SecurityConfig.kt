package com.xermao.llmhub.common.config

import com.xermao.llmhub.auth.application.UserDetailsAppService
import com.xermao.llmhub.auth.security.converter.AuthenticationConverter
import com.xermao.llmhub.auth.security.filter.CacheRequestBodyFilter
import com.xermao.llmhub.auth.security.filter.JwtAuthenticationFilter
import com.xermao.llmhub.auth.security.filter.ReactiveRequestContextFilter
import com.xermao.llmhub.auth.security.filter.TokenAuthenticationFilter
import com.xermao.llmhub.auth.security.handler.AuthenticationDeniedHandler
import com.xermao.llmhub.auth.security.handler.AuthenticationEntryPoint
import com.xermao.llmhub.auth.security.handler.AuthenticationFailureHandler
import com.xermao.llmhub.auth.security.handler.AuthenticationSuccessHandler
import com.xermao.llmhub.auth.security.manage.AuthenticationManager
import com.xermao.llmhub.auth.security.manage.AuthorizationManager
import com.xermao.llmhub.auth.security.service.TokenDetailsServiceImpl
import com.xermao.llmhub.auth.security.utils.Jwt
import com.xermao.llmhub.proxy.cache.GlobalCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.cors.reactive.CorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    @Order(1)
    fun tokenAuthWebFilterChain(
        http: ServerHttpSecurity,
        tokenService: TokenDetailsServiceImpl,
        globalCache: GlobalCache
    ): SecurityWebFilterChain {
        val authenticationManager = AuthenticationManager(tokenService)
        val authenticationFilter = TokenAuthenticationFilter(authenticationManager)
        authenticationFilter.setServerAuthenticationConverter(AuthenticationConverter())
        authenticationFilter.setAuthenticationSuccessHandler(AuthenticationSuccessHandler())
        authenticationFilter.setAuthenticationFailureHandler(AuthenticationFailureHandler())

        http.csrf { it.disable() }
            .cors { it.disable() }
            .logout { it.disable() }
            .httpBasic { it.disable() }
            .securityMatcher(PathPatternParserServerWebExchangeMatcher("/v1/**"))
            .addFilterAt(CacheRequestBodyFilter(), SecurityWebFiltersOrder.FIRST)
            .addFilterAt(ReactiveRequestContextFilter(), SecurityWebFiltersOrder.REACTOR_CONTEXT)
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
            .authorizeExchange {
                // 授权，判断用户是否有权限。
                it.pathMatchers("/v1/**").access(AuthorizationManager(globalCache))
                    .anyExchange()
                    .permitAll()
            }
            .authenticationManager(authenticationManager)
            .exceptionHandling {
                it.accessDeniedHandler(AuthenticationDeniedHandler())
                it.authenticationEntryPoint(AuthenticationEntryPoint())
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Order(2)
    fun jwtAuthenticationFilter(
        http: ServerHttpSecurity,
        jwt: Jwt,
        userDetailsAppService: UserDetailsAppService
    ): SecurityWebFilterChain {
        http.csrf { it.disable() }
            .cors { it.disable() }
            .securityMatcher(PathPatternParserServerWebExchangeMatcher("/api/**"))
            .addFilterAt(JwtAuthenticationFilter(jwt, userDetailsAppService), SecurityWebFiltersOrder.AUTHORIZATION)
            .authorizeExchange {
                it.matchers(
                    PathPatternParserServerWebExchangeMatcher("/api/auth/sign-in", HttpMethod.POST),
                    PathPatternParserServerWebExchangeMatcher("/api/auth/sign-up", HttpMethod.POST),
                    PathPatternParserServerWebExchangeMatcher("/v3/api-docs/**", HttpMethod.GET),
                    PathPatternParserServerWebExchangeMatcher("/swagger-ui/**", HttpMethod.GET),
                    PathPatternParserServerWebExchangeMatcher("/swagger-ui.html", HttpMethod.GET),
                    PathPatternParserServerWebExchangeMatcher("/swagger-ui.yml", HttpMethod.GET),
                    PathPatternParserServerWebExchangeMatcher("/browser.zip", HttpMethod.GET),
                    PathPatternParserServerWebExchangeMatcher("/error"),
                )
                    .permitAll()
                    .anyExchange()
                    .authenticated()
            }
            .exceptionHandling {
                it.accessDeniedHandler(AuthenticationDeniedHandler())
                it.authenticationEntryPoint(AuthenticationEntryPoint())
            }

        return http.build()
    }

}
