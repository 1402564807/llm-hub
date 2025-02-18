package com.xermao.llmhub.auth.security.filter

import com.xermao.llmhub.auth.application.UserDetailsAppService
import com.xermao.llmhub.auth.security.model.JwtAuthenticationToken
import com.xermao.llmhub.auth.security.utils.Jwt
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Slf4j
class JwtAuthenticationFilter(
    private val jwt: Jwt,
    private val userDetailsAppService: UserDetailsAppService
) : WebFilter {

    private val log: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

//    override fun doFilterInternal(
//        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
//    ) {
//        val token = jwt.extract(request)
//        if (token.isNotEmpty() && jwt.verify(token)) {
//            try {
//                val userDetails: UserDetails = userDetailsAppService.loadUserByUsername(jwt.getSubject(token))
//                val authenticated: JwtAuthenticationToken =
//                    JwtAuthenticationToken.authenticated(userDetails, token, userDetails.authorities)
//                authenticated.details = WebAuthenticationDetailsSource().buildDetails(request)
//                SecurityContextHolder.getContext().authentication = authenticated
//            } catch (e: Exception) {
//                log.error("jwt with invalid user id {}", jwt.getSubject(token), e)
//            }
//        }
//        filterChain.doFilter(request, response)
//    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = jwt.extract(exchange.request)
        if (token.isNotEmpty() && jwt.verify(token)) {
            try {

                val userDetails = userDetailsAppService.loadUserByUsername(jwt.getSubject(token))
                val authenticated = JwtAuthenticationToken.authenticated(userDetails, token, userDetails.authorities)
                return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticated))
            } catch (e: Exception) {
                log.error("jwt with invalid user id {}", jwt.getSubject(token), e)
            }
        }
        return chain.filter(exchange)
    }
}
