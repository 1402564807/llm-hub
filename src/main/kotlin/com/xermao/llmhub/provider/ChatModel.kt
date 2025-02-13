package com.xermao.llmhub.provider

import com.xermao.llmhub.model.ChatRequest
import org.springframework.http.HttpHeaders
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.Consumer

interface ChatModel {
    fun uri(): Mono<URI>

    fun headers(serverWebExchange: ServerWebExchange): Consumer<HttpHeaders>

    fun body(chatRequest: ChatRequest): Any
}