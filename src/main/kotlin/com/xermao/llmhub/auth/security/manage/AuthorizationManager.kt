package com.xermao.llmhub.auth.security.manage

import com.xermao.llmhub.auth.security.model.ModelAndStream
import com.xermao.llmhub.auth.security.model.TokenAuthenticationToken
import com.xermao.llmhub.auth.security.utils.ProviderRouter
import com.xermao.llmhub.proxy.cache.GlobalCache
import com.xermao.llmhub.common.domain.constant.GlobalConstant
import com.xermao.llmhub.common.utils.BeanManager
import com.xermao.llmhub.common.utils.JsonUtil
import com.xermao.llmhub.proxy.provider.ChatModel
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
class AuthorizationManager(
    private val globalCache: GlobalCache
) : ReactiveAuthorizationManager<AuthorizationContext> {

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
                sink.error(AccessDeniedException("您的 IP 不在令牌允许访问的列表中"))
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

            if (isSubModel and token.details.models.isNotEmpty()) {
                return@flatMap Mono.error(AccessDeniedException("该令牌无权访问模型: ${modelAndStream.model}"))
            }

            authorizationContext.exchange.attributes[GlobalConstant.REQUEST_MODEL_NAME] = modelAndStream.model

            val user = token.details.user
            if (user.usedQuota >= user.allQuota) {
                return@flatMap Mono.error(AccessDeniedException("用户配额已达到限制"))
            }

            val providers = globalCache.groupCache.getIfPresent(user.group)?.get(modelAndStream.model)
            if (providers.isNullOrEmpty()) {
                return@flatMap Mono.error(AccessDeniedException("模型: ${modelAndStream.model}, 无可用服务商"))
            }

            val provider = ProviderRouter(providers).route()
            val chatModel = BeanManager.getBean(
                "${GlobalConstant.CHAT_MODEL_IMPL}${provider.type.getTitle()}",
                ChatModel::class.java
            )
            authorizationContext.exchange.attributes[GlobalConstant.SERVICE_PROVIDER] = provider
            authorizationContext.exchange.attributes[GlobalConstant.CHAT_MODEL_IMPL] = chatModel

            Mono.just(AuthorizationDecision(true))

        }.defaultIfEmpty(AuthorizationDecision(false))

    }
}
