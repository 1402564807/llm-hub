package com.xermao.llmhub.proxy.provider.volcengine

import com.xermao.llmhub.common.domain.constant.ProviderNames
import com.xermao.llmhub.common.utils.JsonUtil
import com.xermao.llmhub.proxy.model.ChatRequest
import com.xermao.llmhub.provider.model.ServiceProvider
import com.xermao.llmhub.proxy.provider.ChatModel
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.net.URI
import java.util.function.Consumer

@Component(ProviderNames.VOLCENGINE_SERVICE_PROVIDER)
class VolcengineChatModel : ChatModel {

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
