package com.xermao.llmhub.auth.security.filter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.*
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class TokenAuthenticationFilter(
    private var authenticationManagerResolver: ReactiveAuthenticationManagerResolver<ServerWebExchange>,
    private var authenticationSuccessHandler: ServerAuthenticationSuccessHandler,
    private var authenticationConverter: ServerAuthenticationConverter,
    private var authenticationFailureHandler: ServerAuthenticationFailureHandler,
    private var securityContextRepository: ServerSecurityContextRepository,
    private var requiresAuthenticationMatcher: ServerWebExchangeMatcher,
) : WebFilter {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    constructor(authenticationManager: ReactiveAuthenticationManager) : this(
        ReactiveAuthenticationManagerResolver { Mono.just(authenticationManager) },
        WebFilterChainServerAuthenticationSuccessHandler(),
        ServerHttpBasicAuthenticationConverter(),
        ServerAuthenticationEntryPointFailureHandler(HttpBasicServerAuthenticationEntryPoint()),
        WebSessionServerSecurityContextRepository(),
        ServerWebExchangeMatchers.anyExchange(),
    )


    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val headers = exchange.request.headers
        val authorization = headers.getFirst(HttpHeaders.AUTHORIZATION)
        log.info("认证Token: {}", authorization)
//        ReactiveSecurityContextHolder.withSecurityContext

        return requiresAuthenticationMatcher.matches(exchange)
            .filter { it.isMatch }
            .flatMap { authenticationConverter.convert(exchange) }
            .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
            .flatMap { authenticate(exchange, chain, it) }
            .onErrorResume(AuthenticationException::class.java) {
                authenticationFailureHandler.onAuthenticationFailure(WebFilterExchange(exchange, chain), it)
            }
    }

    private fun authenticate(exchange: ServerWebExchange, chain: WebFilterChain, token: Authentication): Mono<Void> {
        return this.authenticationManagerResolver.resolve(exchange)
            .flatMap { it.authenticate(token) }
            .switchIfEmpty(Mono.defer { Mono.error(IllegalStateException("No provider found for " + token.javaClass)) })
            .flatMap { onAuthenticationSuccess(it, WebFilterExchange(exchange, chain)) }
            .doOnError(AuthenticationException::class.java) {
                log.debug("Authentication failed: {}", it.message, it)
            }
    }

    private fun onAuthenticationSuccess(
        authentication: Authentication,
        webFilterExchange: WebFilterExchange
    ): Mono<Void> {
        val exchange = webFilterExchange.exchange
        val securityContext = SecurityContextImpl()
        securityContext.authentication = authentication
        return this.securityContextRepository.save(exchange, securityContext)
            .then(authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication))
            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
    }

    /**
     * Sets the repository for persisting the SecurityContext. Default is
     * [NoOpServerSecurityContextRepository]
     * @param securityContextRepository the repository to use
     */
    fun setSecurityContextRepository(securityContextRepository: ServerSecurityContextRepository) {
        this.securityContextRepository = securityContextRepository
    }

    /**
     * Sets the authentication success handler. Default is
     * [WebFilterChainServerAuthenticationSuccessHandler]
     * @param authenticationSuccessHandler the success handler to use
     */
    fun setAuthenticationSuccessHandler(authenticationSuccessHandler: ServerAuthenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler
    }

    /**
     * Sets the strategy used for converting from a [ServerWebExchange] to an
     * [Authentication] used for authenticating with the provided
     * [ReactiveAuthenticationManager]. If the result is empty, then it signals that
     * no authentication attempt should be made. The default converter is
     * [ServerHttpBasicAuthenticationConverter]
     * @param authenticationConverter the converter to use
     * @since 5.1
     */
    fun setServerAuthenticationConverter(authenticationConverter: ServerAuthenticationConverter) {
        this.authenticationConverter = authenticationConverter
    }

    /**
     * Sets the failure handler used when authentication fails. The default is to prompt
     * for basic authentication.
     * @param authenticationFailureHandler the handler to use. Cannot be null.
     */
    fun setAuthenticationFailureHandler(authenticationFailureHandler: ServerAuthenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler
    }

    /**
     * Sets the matcher used to determine when creating an [Authentication] from
     * [.setServerAuthenticationConverter] to be
     * authentication. If the converter returns an empty result, then no authentication is
     * attempted. The default is any request
     * @param requiresAuthenticationMatcher the matcher to use. Cannot be null.
     */
    fun setRequiresAuthenticationMatcher(requiresAuthenticationMatcher: ServerWebExchangeMatcher) {
        this.requiresAuthenticationMatcher = requiresAuthenticationMatcher
    }


}