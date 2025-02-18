package com.xermao.llmhub.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.xermao.llmhub.auth.security.utils.ContextHolder
import com.xermao.llmhub.common.domain.constant.Constant
import com.xermao.llmhub.model.ChatRequest
import com.xermao.llmhub.model.entity.ServiceProvider
import com.xermao.llmhub.provider.ChatModel
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.CompletableFuture

@Service
class ChatService(
    private val webClient: WebClient
) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val objectMapper = ObjectMapper()

    fun chat(chatRequest: ChatRequest): Publisher<out Any> {

//        val publisherMono = ContextHolder.exchange.map { exchange ->
//            val chatModel = exchange.getRequiredAttribute<ChatModel>(Constant.REQUEST_MODEL_NAME)
//            val provider = exchange.getRequiredAttribute<ServiceProvider>(Constant.SERVICE_PROVIDER)
//            val retrieve = webClient.post().uri(URI.create(exchange.getRequiredAttribute(Constant.SERVICE_PROVIDER)))
//                .contentType(MediaType.APPLICATION_JSON)
//                .headers(chatModel.headers(provider))
//                .bodyValue(chatModel.body(chatRequest))
//                .retrieve()
//
//            if (chatRequest.stream) {
//                return@map retrieve
//                    .bodyToFlux(String::class.java)
//                    .takeUntil(SSE_DONE_PREDICATE)
//                    .filter(SSE_DONE_PREDICATE.negate())
//                    .map { chatModel.usage(it) }
//            }
//            return@map retrieve.bodyToMono(Map::class.java)
//
//        }
//
//        if (chatRequest.stream) {
//            return publisherMono.flatMapMany { it }
//        }
//
//        return publisherMono.flatMap { it }


        if (chatRequest.stream) {
            return ContextHolder.exchange.map { exchange ->
                val chatModel = exchange.getRequiredAttribute<ChatModel>(Constant.CHAT_MODEL_IMPL)
                val provider = exchange.getRequiredAttribute<ServiceProvider>(Constant.SERVICE_PROVIDER)
                val targetModel = provider.modelMap.getOrElse(chatRequest.model) { chatRequest.model }
                chatRequest.model = targetModel
                webClient.post().uri(chatModel.uri(provider))
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(chatModel.headers(provider))
                    .bodyValue(chatModel.body(chatRequest))
                    .retrieve()
                    .bodyToFlux(String::class.java)
//                    .takeUntil(SSE_DONE_PREDICATE)
//                    .filter(SSE_DONE_PREDICATE.negate())
                    .doOnNext { CompletableFuture.runAsync { chatModel.usage(it) } }
            }.flatMapMany { it }
        }
        return ContextHolder.exchange.map {
            val chatModel = it.getRequiredAttribute<ChatModel>(Constant.CHAT_MODEL_IMPL)
            val provider = it.getRequiredAttribute<ServiceProvider>(Constant.SERVICE_PROVIDER)
            val targetModel = provider.modelMap.getOrElse(chatRequest.model) { chatRequest.model }
            chatRequest.model = targetModel
            webClient.post().uri(chatModel.uri(provider))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(chatModel.headers(provider))
                .bodyValue(chatModel.body(chatRequest))
                .retrieve()
                .bodyToMono(Map::class.java)
                .doOnNext { CompletableFuture.runAsync { chatModel.usage(it) } }
        }.flatMap { it }

    }
}