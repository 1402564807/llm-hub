package com.xermao.llmhub.auth.security.handler

import com.xermao.llmhub.auth.security.utils.writeErrorToResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import reactor.core.publisher.Mono

class AuthenticationFailureHandler : ServerAuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange,
        exception: AuthenticationException
    ): Mono<Void> {
        val response = webFilterExchange.exchange.response
        return writeErrorToResponse(response, exception)
    }
}