package com.xermao.llmhub.common

import com.xermao.llmhub.auth.security.utils.writeErrorToResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.firewall.RequestRejectedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RestControllerAdvice
class GlobalExceptionHandler : ErrorWebExceptionHandler, ResponseEntityExceptionHandler() {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(value = [BusinessException::class])
    fun handleBusinessException(ex: BusinessException, exchange: ServerWebExchange): Mono<Void> {
        log.error("Business Error Handled  ===> ", ex)
        return writeErrorToResponse(exchange.response, ex)
    }

    @ExceptionHandler(value = [RequestRejectedException::class])
    fun handleRequestRejectedException(
        ex: RequestRejectedException, exchange: ServerWebExchange
    ): Mono<Void> {
        log.error("RequestRejectedException Handled  ===> ", ex)
        return writeErrorToResponse(exchange.response, ex)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<Any> {
        throw ex
    }

    @ExceptionHandler(value = [Throwable::class])
    fun handleException(ex: Throwable, exchange: ServerWebExchange): Mono<Void> {
        log.error("System Error Handled  ===> ", ex)
        return writeErrorToResponse(exchange.response, ex)
    }

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        return writeErrorToResponse(exchange.response, ex)
    }
}
