package com.xermao.llmhub.common.domain.constant

import java.util.function.Predicate

object GlobalConstant {
    val SSE_DONE_PREDICATE: Predicate<String> = Predicate { it == "[DONE]" }
    const val CHAT_MODEL_IMPL: String = "chat_model_impl_"
    const val EMBED_MODEL_IMPL: String = "embed_model_impl_"
    const val IMAGE_MODEL_IMPL: String = "image_model_impl_"
    const val AUDIO_MODEL_IMPL: String = "audio_model_impl_"
    const val VIDEO_MODEL_IMPL: String = "video_model_impl_"

    const val SERVICE_PROVIDER: String = "service_provider"
    const val REQUEST_MODEL_NAME: String = "request_model_name"
}
