package com.xermao.llmhub.auth.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class Jwt(
    @param:Value("\${jwt.secret}") private val secret: String, @param:Value(
        "\${jwt.expiration-min}"
    ) private val expirationMin: Int
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

    fun extract(request: HttpServletRequest): String {
        val authorization = request.getHeader("Authorization")
        return if (authorization.isNotEmpty() && authorization.startsWith("Bearer")) {
            authorization.substring(7)
        } else {
            ""
        }
    }

    fun create(userIdentify: String): String {
        return JWT.create()
            .withSubject(userIdentify)
            .withIssuedAt(Date())
            .withExpiresAt(
                Date.from(
                    LocalDateTime.now()
                        .plusMinutes(expirationMin.toLong())
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                )
            )
            .sign(Algorithm.HMAC256(secret))
    }

    fun makeToken(
        request: HttpServletRequest?, response: HttpServletResponse, userIdentify: String
    ) {
        response.addHeader("Authorization", String.format("Bearer %s", create(userIdentify)))
    }

    fun removeToken(request: HttpServletRequest?, response: HttpServletResponse) {
        response.addHeader("Authorization", null)
    }
}
