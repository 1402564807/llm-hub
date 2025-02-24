package com.xermao.llmhub.proxy.domain.service

import com.xermao.llmhub.auth.security.utils.ContextHolder
import com.xermao.llmhub.common.domain.constant.GlobalConstant
import com.xermao.llmhub.provider.domain.model.Provider
import com.xermao.llmhub.proxy.application.EmbedModel
import com.xermao.llmhub.proxy.domain.model.EmbedRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Service
class EmbedService(private val webClient: WebClient) {

    fun embedding(embedRequest: EmbedRequest): Mono<Any> {
        return ContextHolder.exchange.map { exchange ->
            val (retrieve, chatModel) = handle(exchange, embedRequest)
            retrieve
                .bodyToMono(Map::class.java)
                .doOnNext { CoroutineScope(IO).launch { chatModel.usage(it) } }
        }.flatMap { it }
    }

    private fun handle(
        exchange: ServerWebExchange,
        embedRequest: EmbedRequest
    ): Pair<WebClient.ResponseSpec, EmbedModel> {
        val embedModel = exchange.attributes[GlobalConstant.EMBED_MODEL_IMPL] as EmbedModel
        val provider = exchange.attributes[GlobalConstant.SERVICE_PROVIDER] as Provider

        embedRequest.model = provider.modelMap.getOrElse(embedRequest.model) { embedRequest.model }
        val retrieve = webClient.post()
            .uri(embedModel.uri(provider))
            .contentType(MediaType.APPLICATION_JSON)
            .headers(embedModel.headers(provider))
            .bodyValue(embedModel.body(embedRequest))
            .retrieve()
        return Pair(retrieve, embedModel)
    }
}
