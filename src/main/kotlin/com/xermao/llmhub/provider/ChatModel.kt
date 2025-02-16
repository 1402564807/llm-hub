package com.xermao.llmhub.provider

import com.xermao.llmhub.model.ChatRequest
import com.xermao.llmhub.model.entity.ServiceProvider
import org.springframework.http.HttpHeaders
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.Consumer

interface ChatModel {
    fun uri(provider: ServiceProvider): URI

    fun headers(serverWebExchange: ServiceProvider): Consumer<HttpHeaders>

    fun body(chatRequest: ChatRequest): Any

    fun usage(response: Any)
}