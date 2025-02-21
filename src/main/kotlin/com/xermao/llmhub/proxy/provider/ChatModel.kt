package com.xermao.llmhub.proxy.provider

import com.xermao.llmhub.proxy.model.ChatRequest
import com.xermao.llmhub.provider.model.ServiceProvider
import org.springframework.http.HttpHeaders
import java.net.URI
import java.util.function.Consumer

interface ChatModel {
    fun uri(provider: ServiceProvider): URI

    fun headers(serverWebExchange: ServiceProvider): Consumer<HttpHeaders>

    fun body(chatRequest: ChatRequest): Any

    fun usage(response: Any)
}
