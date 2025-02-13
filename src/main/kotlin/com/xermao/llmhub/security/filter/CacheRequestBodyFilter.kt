package com.xermao.llmhub.security.filter

import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 解决body不能重复读的问题
 */
class CacheRequestBodyFilter(
    private var requiresAuthenticationMatcher: ServerWebExchangeMatcher,
) : WebFilter {


    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        return requiresAuthenticationMatcher.matches(exchange)
            .filter { it.isMatch }
            .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
            .flatMap {
                DataBufferUtils.join(exchange.request.body).flatMap { dataBuffer ->
                    val retain = DataBufferUtils.retain(dataBuffer)
                    val mutatedRequest = object : ServerHttpRequestDecorator(exchange.request) {
                        override fun getBody(): Flux<DataBuffer> {
                            return Flux.defer { Flux.just(retain.slice(0, dataBuffer.readableByteCount())) }
                        }
                    }

                    chain.filter(exchange.mutate().request(mutatedRequest).build())
                }
            }

    }
}