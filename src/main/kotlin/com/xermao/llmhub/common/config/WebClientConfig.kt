package com.xermao.llmhub.common.config

import io.netty.channel.ChannelOption
import io.netty.handler.ssl.SslContextBuilder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.HttpProtocol
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.tcp.SslProvider.SslContextSpec
import java.time.Duration

@Configuration
class WebClientConfig {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun webClient(): WebClient {
        // 连接池配置
        val provider = ConnectionProvider.builder("prod-pool")
            .maxConnections(512)
            .pendingAcquireMaxCount(1000)
            .evictInBackground(Duration.ofSeconds(30))
//            .metrics(true, Supplier<MeterRegistrar?> { registry. })
            .build()

        // Netty 客户端配置
        val httpClient = HttpClient.create(provider)
            .protocol(HttpProtocol.H2, HttpProtocol.HTTP11)
            .responseTimeout(Duration.ofMinutes(5))
            .compress(true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .secure { ssl: SslContextSpec ->
                ssl.sslContext(
                    SslContextBuilder.forClient().protocols("TLSv1.3").build()
                )
            }

        // WebClient 配置
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs {
                        it.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
                    }
                    .build())
            .filter(logRequest())
            .build()
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction { request, next ->
            log.info("Request: {} {}", request.method(), request.url())
            next.exchange(request)
        }
    }
}
