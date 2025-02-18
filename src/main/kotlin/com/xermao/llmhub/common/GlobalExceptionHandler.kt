package com.xermao.llmhub.common

import com.xermao.llmhub.auth.security.utils.writeErrorToResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.firewall.RequestRejectedException
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RestControllerAdvice
class GlobalExceptionHandler : ErrorWebExceptionHandler, ResponseEntityExceptionHandler() {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(value = [BusinessException::class])
    fun handleBusinessException(ex: BusinessException, exchange: ServerWebExchange): Mono<ResponseEntity<Any>> {
        log.error("Business Error Handled  ===> ", ex)
        val errorResponseException = ErrorResponseException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.message),
            ex.cause
        )
        return handleExceptionInternal(
            errorResponseException,
            errorResponseException.body,
            errorResponseException.headers,
            errorResponseException.statusCode,
            exchange
        )
    }

//    public override fun handleMethodArgumentNotValid(
//        ex: MethodArgumentNotValidException,
//        status: HttpStatus,
//        exchange: ServerWebExchange
//    ): Mono<ResponseEntity<Any>> {
//        log.error("MethodArgumentNotValidException Handled  ===> ", ex)
//        val errorResponseException = ErrorResponseException(
//            status, ProblemDetail.forStatusAndDetail(status, ex.message), ex.cause
//        )
//        return handleExceptionInternal(
//            errorResponseException,
//            errorResponseException.body,
//            errorResponseException.headers,
//            errorResponseException.statusCode,
//            exchange
//        )
//    }

    @ExceptionHandler(value = [RequestRejectedException::class])
    fun handleRequestRejectedException(
        ex: RequestRejectedException, exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        log.error("RequestRejectedException Handled  ===> ", ex)
        val errorResponseException = ErrorResponseException(
            HttpStatus.BAD_REQUEST,
            ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message),
            ex.cause
        )
        return handleExceptionInternal(
            errorResponseException,
            errorResponseException.body,
            errorResponseException.headers,
            errorResponseException.statusCode,
            exchange
        )
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<Any> {
        throw ex
    }

    @ExceptionHandler(value = [Throwable::class])
    fun handleException(ex: Throwable, exchange: ServerWebExchange): Mono<ResponseEntity<Any>> {
        log.error("System Error Handled  ===> ", ex)
        val errorResponseException = ErrorResponseException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "System Error"),
            ex.cause
        )
        return handleExceptionInternal(
            errorResponseException,
            errorResponseException.body,
            errorResponseException.headers,
            errorResponseException.statusCode,
            exchange
        )
    }

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        return writeErrorToResponse(exchange.response, ex)
    }
}
