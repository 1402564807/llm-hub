package com.xermao.llmhub.proxy.domain.service

import com.xermao.llmhub.auth.security.utils.ContextHolder
import com.xermao.llmhub.common.domain.constant.GlobalConstant
import com.xermao.llmhub.provider.domain.model.Provider
import com.xermao.llmhub.proxy.application.ChatModel
import com.xermao.llmhub.proxy.domain.model.ChatRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ServerWebExchange

@Service
class ChatService(
    private val webClient: WebClient
) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    fun chat(chatRequest: ChatRequest): Publisher<out Any> {

        if (chatRequest.stream) {
            return ContextHolder.exchange.map { exchange ->
                val (retrieve, chatModel) = handle(exchange, chatRequest)
                retrieve
                    .bodyToFlux(String::class.java)
                    .doOnNext { CoroutineScope(IO).launch { chatModel.usage(it) } }
            }.flatMapMany { it }
        }
        return ContextHolder.exchange.map { exchange ->
            val (retrieve, chatModel) = handle(exchange, chatRequest)
            retrieve
                .bodyToMono(Map::class.java)
                .doOnNext { CoroutineScope(IO).launch { chatModel.usage(it) } }
        }.flatMap { it }
    }


    private fun handle(exchange: ServerWebExchange, chatRequest: ChatRequest): Pair<WebClient.ResponseSpec, ChatModel> {
        val chatModel = exchange.attributes[GlobalConstant.CHAT_MODEL_IMPL] as ChatModel
        val provider = exchange.attributes[GlobalConstant.SERVICE_PROVIDER] as Provider

        chatRequest.model = provider.modelMap.getOrElse(chatRequest.model) { chatRequest.model }
        val retrieve = webClient.post()
            .uri(chatModel.uri(provider))
            .contentType(MediaType.APPLICATION_JSON)
            .headers(chatModel.headers(provider))
            .bodyValue(chatModel.body(chatRequest))
            .retrieve()
        return Pair(retrieve, chatModel)
    }
}
