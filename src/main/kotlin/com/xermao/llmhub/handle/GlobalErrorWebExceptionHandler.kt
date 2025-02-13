package com.xermao.llmhub.handle

import com.xermao.llmhub.model.R
import com.xermao.llmhub.security.utils.writeErrorToResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestControllerAdvice
class GlobalErrorWebExceptionHandler : ErrorWebExceptionHandler {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        return writeErrorToResponse(exchange.response, ex)
    }

    @ExceptionHandler(Exception::class)
    fun handleCustomException(e: Exception): Mono<R> {
        log.info("捕获到未处理的Exception异常：{}", e.message, e)
        return R(500, e.message!!, false).toMono()
    }
}