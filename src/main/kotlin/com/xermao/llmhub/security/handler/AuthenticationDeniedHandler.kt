package com.xermao.llmhub.security.handler

import com.xermao.llmhub.security.utils.writeErrorToResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * 处理 认证失败 的逻辑。
 */
class AuthenticationDeniedHandler : ServerAccessDeniedHandler {


    override fun handle(exchange: ServerWebExchange, denied: AccessDeniedException): Mono<Void> {
        return writeErrorToResponse(exchange.response, denied)
    }
}