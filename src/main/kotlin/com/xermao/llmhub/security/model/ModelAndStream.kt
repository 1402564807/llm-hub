package com.xermao.llmhub.security.model

data class ModelAndStream(
    val model: String,
    private val stream: Boolean?
) {
    fun stream(): Boolean {
        return stream ?: false
    }
}
