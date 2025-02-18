package com.xermao.llmhub.auth.security.converter

import com.xermao.llmhub.auth.security.model.ApiKeyAuthenticationToken
import com.xermao.llmhub.auth.security.model.ApiKeyGrantedAuthority
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class AuthenticationConverter : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        val authorization = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (authorization.isNullOrEmpty()) {
            return Mono.empty()
        }
        var apiKey = authorization.substringAfter("Bearer ")
        if (apiKey.isEmpty()) {
            return Mono.empty()
        }
        apiKey = apiKey.substringAfter("sk-")
        if (apiKey.isEmpty()) {
            return Mono.empty()
        }
        val authorities = listOf(ApiKeyGrantedAuthority(apiKey))
        return Mono.just(ApiKeyAuthenticationToken(authorities))
    }
}