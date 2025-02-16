package com.xermao.llmhub.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Message(
    @JsonProperty("role") val role: String,
    @JsonProperty("name") val name: String?,
    @JsonProperty("content") val content: Any,
    @JsonProperty("tool_calls") val toolCalls: List<ToolCalls>?,
    // Deprecated
    @JsonProperty("function_call") val functionCall: Function?,
    @JsonProperty("tool_call_id") val toolCallId: String?,
    @JsonProperty("refusal") val refusal: String?,
    @JsonProperty("audio") val audio: String?,
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ToolCalls(
        @JsonProperty("id") val id: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("function") val function: Function,
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Function(
        @JsonProperty("name") val name: String,
        @JsonProperty("arguments") val arguments: String,
    )
}