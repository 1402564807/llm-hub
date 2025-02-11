package com.xermao.llmhub.security.model

data class Stream(
    private val stream: Boolean?
) {
    fun isStream(): Boolean {
        return stream ?: false
    }
}
