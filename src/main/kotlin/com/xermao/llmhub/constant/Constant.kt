package com.xermao.llmhub.constant

import java.util.function.Predicate

object Constant {
    val SSE_DONE_PREDICATE: Predicate<String> = Predicate { it == "[DONE]" }
    const val CHAT_MODEL_IMPL: String = "chat_model_impl"
    const val SERVICE_PROVIDER: String = "service_provider"
    const val REQUEST_MODEL_NAME: String = "request_model_name"
}