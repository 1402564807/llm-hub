package com.xermao.llmhub.auth.security.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class Jwt(
    @param:Value("\${jwt.secret}") private val secret: String,
    @param:Value("\${jwt.expiration-min}") val expirationMin: Long,
    @param:Value("\${jwt.expiration-max}") val expirationMax: Long
) {
    private val verifier: JWTVerifier = JWT.require(Algorithm.HMAC256(secret)).build()

    fun getSubject(token: String): String {
        return JWT.decode(token).subject
    }

    fun verify(token: String): Boolean {
        return try {
            verifier.verify(token)
            true
        } catch (e: JWTVerificationException) {
            false
        }
    }

    fun extract(request: ServerHttpRequest): String {
        val authorization = request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull() ?: return ""
        return if (authorization.isNotEmpty() && authorization.startsWith("Bearer")) {
            authorization.substring(7)
        } else {
            ""
        }
    }

    fun create(userIdentify: String): String {
        return create(userIdentify, expirationMin)
    }

    fun create(userIdentify: String, expiration: Long = expirationMin): String {
        return JWT.create()
            .withSubject(userIdentify)
            .withIssuedAt(Date())
            .withExpiresAt(
                Date.from(
                    LocalDateTime.now()
                        .plusMinutes(expiration)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                )
            )
            .sign(Algorithm.HMAC256(secret))
    }

    fun makeToken(userIdentify: String): Mono<String> {
        val token = create(userIdentify)
        return ContextHolder.exchange.map { exchangeContext ->
            exchangeContext.response.headers.add(HttpHeaders.AUTHORIZATION, "Bearer $token")
            return@map token
        }.flatMap { Mono.just(it) }
    }

    fun removeToken(): Mono<Void> {
        return ContextHolder.exchange.map { exchangeContext ->
            exchangeContext.response.headers.remove(HttpHeaders.AUTHORIZATION)
        }.flatMap { Mono.empty() }
    }
}
