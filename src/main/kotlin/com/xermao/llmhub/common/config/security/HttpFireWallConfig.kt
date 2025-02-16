package com.xermao.llmhub.common.config.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.firewall.RequestRejectedException
import org.springframework.security.web.firewall.RequestRejectedHandler
import org.springframework.security.web.firewall.StrictHttpFirewall

@Configuration
class HttpFireWallConfig {
    @Bean
    fun httpFirewall(): HttpFirewall = StrictHttpFirewall()

    @Bean
    fun requestRejectedHandler(): RequestRejectedHandler {
        return RequestRejectedHandler { _: HttpServletRequest, response: HttpServletResponse, requestRejectedException: RequestRejectedException ->
            response.status = HttpServletResponse.SC_BAD_REQUEST
            response.contentType = MediaType.TEXT_PLAIN_VALUE
            response.writer.use { writer ->
                requestRejectedException.message?.let { writer.write(it) }
            }
        }
    }
}
