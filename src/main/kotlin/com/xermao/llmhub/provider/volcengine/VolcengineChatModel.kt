package com.xermao.llmhub.provider.volcengine

import com.xermao.llmhub.model.ChatRequest
import com.xermao.llmhub.provider.ChatModel
import com.xermao.llmhub.security.utils.ContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.Consumer

class VolcengineChatModel: ChatModel {
    private val uri = URI.create("https://ark.cn-beijing.volces.com/api/v3/chat/completions")


    override fun uri(): Mono<URI> {
        return ContextHolder.exchange.map { URI.create(it.getRequiredAttribute("base_url")) }
    }

    override fun headers(serverWebExchange: ServerWebExchange): Consumer<HttpHeaders> {
        return Consumer { httpHeaders ->
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "B" + serverWebExchange.getRequiredAttribute("key"))
        }
    }

    override fun body(chatRequest: ChatRequest): Any {
        return chatRequest
    }
}