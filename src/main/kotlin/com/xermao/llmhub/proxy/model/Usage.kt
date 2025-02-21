package com.xermao.llmhub.proxy.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Usage(
    @JsonProperty("completion_tokens") val completionTokens: Int = 0,
    @JsonProperty("prompt_tokens") val promptTokens: Int = 0,
    @JsonProperty("total_tokens") val totalTokens: Int = 0,
    @JsonProperty("prompt_tokens_details") val promptTokensDetails: PromptTokensDetails,
    @JsonProperty("completion_tokens_details") val completionTokensDetails: CompletionTokenDetails,
    @JsonProperty("prompt_cache_hit_tokens") val promptCacheHitTokens: Int = 0,
    @JsonProperty("prompt_cache_miss_tokens") val promptCacheMissTokens: Int = 0,
) {

    data class PromptTokensDetails(
        @JsonProperty("audio_tokens") val audioTokens: Int = 0,
        @JsonProperty("cached_tokens") val cachedTokens: Int = 0,
    )

    data class CompletionTokenDetails(
        @JsonProperty("reasoning_tokens") val reasoningTokens: Int = 0,
        @JsonProperty("accepted_prediction_tokens") val acceptedPredictionTokens: Int = 0,
        @JsonProperty("audio_tokens") val audioTokens: Int = 0,
        @JsonProperty("rejected_prediction_tokens") val rejectedPredictionTokens: Int = 0,
    )
}
