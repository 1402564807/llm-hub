package com.xermao.llmhub.proxy.web

import com.xermao.llmhub.proxy.domain.model.ChatRequest
import com.xermao.llmhub.proxy.domain.service.ChatService
import org.reactivestreams.Publisher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class ChatController(
    private val chatService: ChatService
) {


    @PostMapping("/chat/completions")
    fun chat(@RequestBody chatRequest: ChatRequest): Publisher<out Any> {
        return chatService.chat(chatRequest)
    }


//    @PostMapping("/chat/completions")
//    suspend fun chat(@RequestBody chatRequest: ChatRequest): Any {
//        val attributes = ContextHolder.exchange.awaitSingle().attributes
//        val context = ReactiveSecurityContextHolder.getContext().awaitSingle()
//        return context.authentication
//    }
}
