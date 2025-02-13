package com.xermao.llmhub.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatRequest(
    @JsonProperty("role") val model: String,
    @JsonProperty("role") val messages: List<Message>,
    @JsonProperty("role") val stream: Boolean,
    @JsonProperty("stream_options") val streamOptions: StreamOptions?,
    @JsonProperty("max_tokens") val maxTokens: Int?,
    @JsonProperty("stop") val stop: List<String>?,
    @JsonProperty("frequency_penalty") val frequencyPenalty: Float?,
    @JsonProperty("presence_penalty") val presencePenalty: Float?,
    @JsonProperty("temperature") val temperature: Float?,
    @JsonProperty("top_p") val topP: Float?,
    @JsonProperty("logprobs") val logprobs: Boolean?,
    @JsonProperty("top_logprobs") val topLogprobs: Int?,
    @JsonProperty("logitBias") val logitBias: Map<String, Int>?,
    @JsonProperty("tools") val tools: List<Tool>?,
) {


    data class StreamOptions(
        @JsonProperty("include_usage") val includeUsage: Boolean,
    )

    data class Tool(
        @JsonProperty("type") val type: String,
        @JsonProperty("function") val function: FunctionDefinition,
    )

    data class FunctionDefinition(
        @JsonProperty("name") val name: String,
        @JsonProperty("description") val description: String,
        @JsonProperty("parameters") val parameters: Any,
    )
}
