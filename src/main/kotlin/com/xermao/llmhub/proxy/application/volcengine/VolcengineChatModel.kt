package com.xermao.llmhub.proxy.application.volcengine

import com.fasterxml.jackson.annotation.JsonProperty
import com.xermao.llmhub.common.domain.constant.GlobalConstant.SSE_DONE_PREDICATE
import com.xermao.llmhub.common.utils.JsonUtil
import com.xermao.llmhub.proxy.domain.model.Usage
import com.xermao.llmhub.proxy.domain.constant.ProviderImplName
import com.xermao.llmhub.proxy.application.ChatModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component(ProviderImplName.Chat.VOLCENGINE_SERVICE_PROVIDER)
class VolcengineChatModel : ChatModel {

    private val log = LoggerFactory.getLogger(VolcengineChatModel::class.java)

    override fun usage(response: Any) {
        if (response is Map<*, *>) {
            val usage = JsonUtil.toJson(response["usage"])
            log.info(usage)
        }
        if (response is String) {
            // SSE 结束标识 [DONE]
            if (SSE_DONE_PREDICATE.test(response)) {
                return
            }
            val chunk = JsonUtil.fromJson(response, ChatCompletionChunk::class.java)

            chunk.usage?.let {
                log.info(JsonUtil.toJson(it))
            }
        }
    }

    data class ChatCompletionChunk(
        @JsonProperty("usage") val usage: Usage?,
    )
}
