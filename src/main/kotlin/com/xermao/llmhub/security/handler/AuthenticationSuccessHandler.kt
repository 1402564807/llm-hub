package com.xermao.llmhub.security.handler

import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono

class AuthenticationSuccessHandler: ServerAuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange?,
        authentication: Authentication?
    ): Mono<Void> {
        val response: ServerHttpResponse = webFilterExchange!!.exchange.response
        response.headers.add("X-Authenticated-User", authentication!!.name)
        return webFilterExchange.chain.filter(webFilterExchange.exchange)
    }
}