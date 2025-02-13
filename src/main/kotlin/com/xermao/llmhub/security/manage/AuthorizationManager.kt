package com.xermao.llmhub.security.manage

import com.xermao.llmhub.security.model.ModelAndStream
import com.xermao.llmhub.security.model.TokenAuthenticationToken
import com.xermao.llmhub.utils.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.security.web.util.matcher.IpAddressMatcher
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

    @Deprecated("Deprecated in Java")
    override fun check(
        authentication: Mono<Authentication>,
        authorizationContext: AuthorizationContext
    ): Mono<AuthorizationDecision> {

        return authentication.handle<TokenAuthenticationToken> { token, sink ->
            if (token !is TokenAuthenticationToken) {
                return@handle
            }
            val host = authorizationContext.exchange.request.uri.host
            val isSubnet = token.details.subnet
                .map { IpAddressMatcher(it) }
                .any { it.matches(host) }
            if (!isSubnet and token.details.subnet.isNotEmpty()) {
                sink.error(AccessDeniedException("IP 为黑名单地址"))
                return@handle
            }
            sink.next(token)
        }.flatMap { token ->
            val bufferFlux = authorizationContext.exchange.request.body

            val inputStream = DataBufferUtils.subscriberInputStream(bufferFlux, 256)
            val modelAndStream = JsonUtil.fromJson(inputStream, ModelAndStream::class.java)

            authorizationContext.exchange.response.headers.contentType = if (modelAndStream.stream())
                MediaType.TEXT_EVENT_STREAM
            else
                MediaType.APPLICATION_JSON

            val isSubModel = token.details.models.any { model -> model == modelAndStream.model }
            Mono.just(AuthorizationDecision(isSubModel or token.details.models.isEmpty()))

        }.defaultIfEmpty(AuthorizationDecision(false))

    }
}