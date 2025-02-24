package com.xermao.llmhub.proxy.application

import com.xermao.llmhub.proxy.domain.model.ChatRequest
import com.xermao.llmhub.provider.domain.model.Provider
import com.xermao.llmhub.proxy.domain.constant.ProviderMode
import java.net.URI

interface ChatModel: PreProcessing {
    fun uri(provider: Provider): URI {
        return super.url(provider, ProviderMode.CHAT)
    }

    fun body(chatRequest: ChatRequest): Any {
        if (chatRequest.stream) {
            chatRequest.streamOptions = ChatRequest.StreamOptions(true)
        }
        return chatRequest
    }

    fun usage(response: Any)
}
