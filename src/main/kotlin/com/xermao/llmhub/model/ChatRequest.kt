package com.xermao.llmhub.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ChatRequest(
    @JsonProperty("messages") val messages: List<Message>,
    @JsonProperty("stream") val stream: Boolean,
    @JsonProperty("model") var model: String,
    @JsonProperty("stream_options") var streamOptions: StreamOptions?,
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


    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class StreamOptions(
        @JsonProperty("include_usage") val includeUsage: Boolean,
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Tool(
        @JsonProperty("type") val type: String,
        @JsonProperty("function") val function: FunctionDefinition,
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class FunctionDefinition(
        @JsonProperty("name") val name: String,
        @JsonProperty("description") val description: String,
        @JsonProperty("parameters") val parameters: Any,
    )
}
