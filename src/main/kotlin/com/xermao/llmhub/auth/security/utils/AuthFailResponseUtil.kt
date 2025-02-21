package com.xermao.llmhub.auth.security.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.xermao.llmhub.common.domain.model.R
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.core.publisher.Mono

private val objectMapper = ObjectMapper()

fun writeErrorToResponse(response: ServerHttpResponse, throwable: Throwable): Mono<Void> {
    response.setStatusCode(HttpStatus.UNAUTHORIZED)
    response.headers.contentType = MediaType.APPLICATION_JSON
    val body = R(500, throwable.message ?: "", false, null)
    val dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(body))
    return response.writeWith(Mono.just(dataBuffer))
}
