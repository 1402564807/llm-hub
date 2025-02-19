package com.xermao.llmhub.model

data class R<T>(
    val code: Int,
    val message: String,
    val success: Boolean,
    val data: T,
) {
    companion object {
        fun <T> success(data: T): R<T> {
            return R(200, "success", true, data)
        }
    }
}
