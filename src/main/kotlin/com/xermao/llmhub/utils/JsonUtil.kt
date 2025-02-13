package com.xermao.llmhub.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.InputStream

object JsonUtil {
    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun <T> toJson(obj: T): String {
        return objectMapper.writeValueAsString(obj)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return objectMapper.readValue(json, clazz)
    }

    fun <T> fromJson(inputStream: InputStream, clazz: Class<T>): T {
        return objectMapper.readValue(inputStream, clazz)
    }

}