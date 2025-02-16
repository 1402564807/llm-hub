package com.xermao.llmhub.provider.volcengine

import com.xermao.llmhub.constant.Constant
import com.xermao.llmhub.model.ChatRequest
import com.xermao.llmhub.model.Usage
import com.xermao.llmhub.model.entity.ServiceProvider
import com.xermao.llmhub.provider.ChatModel
import com.xermao.llmhub.security.utils.ContextHolder
import com.xermao.llmhub.common.utils.JsonUtil
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.Consumer

@Component(Constant.CHAT_MODEL_IMPL+"1")
class VolcengineChatModel: ChatModel {

    private val log = LoggerFactory.getLogger(VolcengineChatModel::class.java)

    override fun uri(provider: ServiceProvider): URI {
        return URI.create("${provider.baseUrl}chat/completions")
    }

    override fun headers(serverWebExchange: ServiceProvider): Consumer<HttpHeaders> {
        return Consumer { httpHeaders ->
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer ${serverWebExchange.key}")
        }
    }

    override fun body(chatRequest: ChatRequest): Any {
        if (chatRequest.stream) {
            chatRequest.streamOptions = ChatRequest.StreamOptions(true)
        }
        return chatRequest
    }

    override fun usage(response: Any) {
        if (response is Map<*, *>) {
            val usage = JsonUtil.toJson(response["usage"])
            log.info(usage)
        }
        if (response is String) {
            val fromJson = JsonUtil.fromJson(response, Map::class.java)
            fromJson["usage"]?.let {
                log.info(JsonUtil.toJson(it))
            }
        }
    }
}