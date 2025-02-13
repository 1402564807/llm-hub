package com.xermao.llmhub.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatCompletionChunk(
    @JsonProperty("id") val id: String?,
    @JsonProperty("model") val model: String?,
    @JsonProperty("created") val created: Int?,
    @JsonProperty("object") val obj: String?,
    @JsonProperty("choices") val choices: List<StreamChoice>?,
    @JsonProperty("usage") val usage: Int?,
) {

    data class StreamChoice(
        @JsonProperty("index") val index: Int?,
        @JsonProperty("finish_reason") val finishReason: String?,
        @JsonProperty("delta") val delta: ChoiceDelta?,
        @JsonProperty("logprobs") val logprobs: ChoiceLogprobs?,
    )

    data class ChoiceDelta(
        @JsonProperty("role") val role: String?,
        @JsonProperty("content") val content: String?,
        @JsonProperty("tool_calls") val toolCalls: List<ChoiceDeltaToolCall>?,
    )

    data class ChoiceDeltaToolCall(
        @JsonProperty("index") val index: Int?,
        @JsonProperty("id") val id: String?,
        @JsonProperty("type") val type: List<ChoiceDeltaToolCall>?,
    )

    data class ChoiceLogprobs(
        @JsonProperty("content") val content: TokenLogprob?,
    )

    data class TokenLogprob(
        @JsonProperty("token") val token: String?,
        @JsonProperty("bytes") val bytes: List<Int>?,
        @JsonProperty("logprob") val logprob: Float?,
        @JsonProperty("top_logprobs") val topLogprobs: List<TopLogprob>?,
    )

    data class TopLogprob(
        @JsonProperty("token") val token: String?,
        @JsonProperty("bytes") val bytes: List<Int>?,
        @JsonProperty("logprob") val logprob: Float?,
    )
}
