package com.xermao.llmhub.security.handler

import com.xermao.llmhub.security.utils.writeErrorToResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


/**
 * 处理 未认证用户 尝试访问受保护资源时的逻辑。
 */
class AuthenticationEntryPoint : ServerAuthenticationEntryPoint {

    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> {
        return writeErrorToResponse(exchange.response, ex)
    }
}