package com.xermao.llmhub.auth.security.filter

import com.xermao.llmhub.auth.security.utils.ContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class ReactiveRequestContextFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange)
            .contextWrite { it.put(ContextHolder.CONTEXT_KEY, exchange) }
    }
}