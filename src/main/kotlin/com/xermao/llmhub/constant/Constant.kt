package com.xermao.llmhub.constant

import java.util.function.Predicate

object Constant {
    val SSE_DONE_PREDICATE: Predicate<String> = Predicate { it == "[DONE]" }
    val SERVICE_PROVIDER: String = "service_provider"
    val REQUEST_MODEL_NAME: String = "request_model_name"
}