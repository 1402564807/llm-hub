package com.xermao.llmhub.common.domain.constant

import java.util.function.Predicate

object GlobalConstant {
    val SSE_DONE_PREDICATE: Predicate<String> = Predicate { it == "[DONE]" }
    const val CHAT_MODEL_IMPL: String = "chat_model_impl_"
    const val SERVICE_PROVIDER: String = "service_provider"
    const val REQUEST_MODEL_NAME: String = "request_model_name"
}

object ProviderNames {
    const val OPEN_AI_SERVICE_PROVIDER = GlobalConstant.CHAT_MODEL_IMPL + "open_ai"
    const val VOLCENGINE_SERVICE_PROVIDER = GlobalConstant.CHAT_MODEL_IMPL + "volcengine"
}