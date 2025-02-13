package com.xermao.llmhub.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Message(
    @JsonProperty("role") val role: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("tool_calls") val toolCalls: List<ToolCalls>,
    @JsonProperty("tool_call_id") val toolCallId: String,
    ) {



    data class ToolCalls(
        @JsonProperty("id") val id: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("function") val function: Function,
    )

    data class Function(
        @JsonProperty("name") val name: String,
        @JsonProperty("arguments") val arguments: String,
    )
}