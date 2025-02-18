package com.xermao.llmhub.auth.security.utils

import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

object ContextHolder {
    val CONTEXT_KEY: Class<ServerWebExchange> = ServerWebExchange::class.java

    val exchange: Mono<ServerWebExchange> get() = Mono.deferContextual { Mono.just(it.get(CONTEXT_KEY)) }
}
