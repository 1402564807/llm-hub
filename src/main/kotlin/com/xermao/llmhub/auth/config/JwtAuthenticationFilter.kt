package com.xermao.llmhub.auth.config

import com.xermao.llmhub.auth.application.UserDetailsAppService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Slf4j
class JwtAuthenticationFilter(
    private val jwt: Jwt,
    private val userDetailsAppService: UserDetailsAppService
) : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val token = jwt.extract(request)
        if (token.isNotEmpty() && jwt.verify(token)) {
            try {
                val userDetails: UserDetails = userDetailsAppService.loadUserByUsername(jwt.getSubject(token))
                val authenticated: JwtAuthenticationToken =
                    JwtAuthenticationToken.authenticated(userDetails, token, userDetails.authorities)
                authenticated.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticated
            } catch (e: Exception) {
                log.error("jwt with invalid user id {}", jwt.getSubject(token), e)
            }
        }
        filterChain.doFilter(request, response)
    }
}
