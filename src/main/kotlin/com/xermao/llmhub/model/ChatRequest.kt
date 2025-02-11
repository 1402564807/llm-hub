package com.xermao.llmhub.model

data class ChatRequest(
    val model: String,
    val stream: Boolean,
    val messages: List<Message>,
)
