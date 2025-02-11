package com.xermao.llmhub.security.manage

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.util.AntPathMatcher
import reactor.core.publisher.Mono

/**
 * ReactiveAuthorizationManager
 * 
 * 职责	授权：判断用户是否有权限访问资源
 * 输入	Authentication 对象和资源（URL/方法）
 * 输出	AuthorizationDecision（是否授权）
 * 使用场景	URL 权限检查、方法权限检查
 * 依赖关系	通常依赖用户的权限信息
 * 调用时机	在访问受保护资源时调用
 */
class AuthorizationManager : ReactiveAuthorizationManager<AuthorizationContext> {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private val antPathMatcher = AntPathMatcher()

    @Deprecated("Deprecated in Java")
    override fun check(
        authentication: Mono<Authentication>,
        authorizationContext: AuthorizationContext
    ): Mono<AuthorizationDecision> {

        return authentication.map {
            val request = authorizationContext.exchange.request
            val authorities = it.authorities
            for (authority in authorities) {

                val pattern = authority.authority
                val path = request.uri.path
                if (antPathMatcher.match(pattern, path)) {
                    log.info("用户请求API校验通过, Authority: {}, Path: {}", pattern, path)
                    AuthorizationDecision(true)
                }
            }
            AuthorizationDecision(false)
        }.defaultIfEmpty(AuthorizationDecision(false))

    }
}