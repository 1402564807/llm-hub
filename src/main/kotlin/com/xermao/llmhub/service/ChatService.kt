package com.xermao.llmhub.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.xermao.llmhub.model.ChatRequest
import com.xermao.llmhub.provider.ChatModel
import com.xermao.llmhub.security.utils.ContextHolder
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI

@Service
class ChatService(
    private val webClient: WebClient
) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val objectMapper = ObjectMapper()

    fun chat(chatRequest: ChatRequest): Publisher<out Any> {

        if (chatRequest.stream) {
            return ContextHolder.exchange.map {
                val chatModel = it.getRequiredAttribute<ChatModel>("chat_model")
                webClient.post().uri(URI.create(it.getRequiredAttribute("base_url")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(chatModel.headers(it))
                    .bodyValue(chatModel.body(chatRequest))
                    .retrieve()
            }.flatMapMany { spec ->
                spec.bodyToFlux(String::class.java)
                    .takeUntil { "[DONE]" == it }
                    .filter { "[DONE]" != it }
            }
        }
        return ContextHolder.exchange.map {
            val chatModel = it.getRequiredAttribute<ChatModel>("chat_model")
            webClient.post().uri(URI.create(it.getRequiredAttribute("base_url")))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(chatModel.headers(it))
                .bodyValue(chatModel.body(chatRequest))
                .retrieve()
        }.flatMap { it.bodyToMono(String::class.java) }

    }
}