package com.xermao.llmhub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class LlmHubApplication

fun main(args: Array<String>) {
    runApplication<LlmHubApplication>(*args)
}
