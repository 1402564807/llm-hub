package com.xermao.llmhub.security.filter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class TokenAuthenticationFilter(
    private val authenticationManager: ReactiveAuthenticationManager,
) : AuthenticationWebFilter(authenticationManager) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)


    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val headers = exchange.request.headers
        val authorization = headers.getFirst(HttpHeaders.AUTHORIZATION)
        log.info("认证Token: {}", authorization)

        return super.filter(exchange, chain)
    }


}