package com.xermao.llmhub.controller

import com.xermao.llmhub.model.ChatRequest
import com.xermao.llmhub.service.ChatService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/v1")
class ChatController(
    private val chatService: ChatService
) {


    @PostMapping("/chat/completions")
    fun chat(@RequestBody chatRequest: ChatRequest): Flux<out Any> {
        return chatService.chat(chatRequest)
    }
}