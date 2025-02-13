package com.xermao.llmhub.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.xermao.llmhub.client.NettyClient
import com.xermao.llmhub.model.ChatRequest
import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.DefaultFullHttpRequest
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpVersion
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body
import reactor.core.publisher.Flux
import java.net.URI

@Service
class ChatService(
    private val webClient: WebClient
) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val objectMapper = ObjectMapper()

    fun chat(chatRequest: ChatRequest): Flux<out Any> {

        val uri = URI.create("https://ark.cn-beijing.volces.com/api/v3/chat/completions")

        val retrieve = webClient.post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer b18c7c87-4007-4e0e-b92f-0f8e85b39d2a")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(chatRequest)
            .retrieve()

        if (chatRequest.stream) {
            val serverSentEventFlux = retrieve.bodyToFlux(String::class.java)
                // 收到 [DONE] 后，取消通量流。
                .takeUntil{ "[DONE]" == it }
                .filter { "[DONE]" != it }
                .map {  }
            serverSentEventFlux.subscribe {
                println(it)
            }
            return Flux.empty()
        }

//        return retrieve.bodyToMono(Any::class.java)


        val string = objectMapper.writeValueAsString(chatRequest)
        val request = DefaultFullHttpRequest(
            HttpVersion.HTTP_1_1,
            HttpMethod.POST,
            uri.toASCIIString(),
            Unpooled.wrappedBuffer(string.encodeToByteArray())
        )
        request.headers().set(HttpHeaderNames.AUTHORIZATION, "Bearer b18c7c87-4007-4e0e-b92f-0f8e85b39d2a")

        return NettyClient("localhost", 8080)
            .connectAndSendRequest(request)
            .doOnNext {
                println(it)
            }

    }
}