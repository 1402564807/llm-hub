package com.xermao.llmhub.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.xermao.llmhub.auth.security.utils.ContextHolder
import com.xermao.llmhub.common.domain.constant.GlobalConstant
import com.xermao.llmhub.model.ChatRequest
import com.xermao.llmhub.model.entity.ServiceProvider
import com.xermao.llmhub.provider.ChatModel
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ServerWebExchange
import java.util.concurrent.CompletableFuture

@Service
class ChatService(
    private val webClient: WebClient
) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val objectMapper = ObjectMapper()

    fun chat(chatRequest: ChatRequest): Publisher<out Any> {

        if (chatRequest.stream) {
            return ContextHolder.exchange.map { exchange ->
                val (retrieve, chatModel) = handle(exchange, chatRequest)
                retrieve
                    .bodyToFlux(String::class.java)
                    .doOnNext { CompletableFuture.runAsync { chatModel.usage(it) } }
            }.flatMapMany { it }
        }
        return ContextHolder.exchange.map { exchange ->
            val (retrieve, chatModel) = handle(exchange, chatRequest)
            retrieve
                .bodyToMono(Map::class.java)
                .doOnNext { CompletableFuture.runAsync { chatModel.usage(it) } }
        }.flatMap { it }

    }


    private fun handle(exchange: ServerWebExchange, chatRequest: ChatRequest): Pair<WebClient.ResponseSpec, ChatModel> {
        val chatModel = exchange.getRequiredAttribute<ChatModel>(GlobalConstant.CHAT_MODEL_IMPL)
        val provider = exchange.getRequiredAttribute<ServiceProvider>(GlobalConstant.SERVICE_PROVIDER)
        chatRequest.model = provider.modelMap.getOrElse(chatRequest.model) { chatRequest.model }
        val retrieve = webClient.post().uri(chatModel.uri(provider))
            .contentType(MediaType.APPLICATION_JSON)
            .headers(chatModel.headers(provider))
            .bodyValue(chatModel.body(chatRequest))
            .retrieve()

        return Pair(retrieve, chatModel)
    }
}