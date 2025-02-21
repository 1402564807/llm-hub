package com.xermao.llmhub.proxy.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatCompletion(
    @JsonProperty("id") val id: String?,
    @JsonProperty("model") val model: String?,
    @JsonProperty("created") val created: Int?,
    @JsonProperty("object") val obj: String?,
//    @JsonProperty("choices") val choices: List<Choice>?,
    @JsonProperty("usage") val usage: Usage?,
)
