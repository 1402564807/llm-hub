package com.xermao.llmhub.proxy.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class EmbedRequest(
    @JsonProperty("input") val input: Any,
    @JsonProperty("model") var model: String,
)
