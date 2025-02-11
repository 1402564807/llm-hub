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
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ChatService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val objectMapper = ObjectMapper()

    fun chat(chatRequest: ChatRequest): Flux<Any> {
        val string = objectMapper.writeValueAsString(chatRequest)
        val request = DefaultFullHttpRequest(
            HttpVersion.HTTP_1_1,
            HttpMethod.POST,
            "http://127.0.0.1:3000/v1/chat/completions",
            Unpooled.wrappedBuffer(string.encodeToByteArray())
        )
        request.headers().set(HttpHeaderNames.AUTHORIZATION, "sk-RJGbQrvHajKZ67wy26935fF314F24dD5990e4c0aD09d4bFe")

        return NettyClient("127.0.0.1", 3000)
            .connectAndSendRequest(request)
            .doOnNext {
                println(it)
            }

    }
}