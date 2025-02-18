package com.xermao.llmhub.auth.security.model

data class ModelAndStream(
    val model: String,
    private val stream: Boolean?
) {
    fun stream(): Boolean {
        return stream ?: false
    }
}
