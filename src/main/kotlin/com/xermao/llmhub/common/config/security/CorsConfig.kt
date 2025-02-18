package com.xermao.llmhub.common.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class CorsConfig : WebFluxConfigurer {
    @Value("\${cors.allowedOrigins}")
    private val allowedOrigins: String? = null

    @Value("\${cors.allowedMethods}")
    private val allowedMethods: String? = null

    @Value("\${cors.allowedHeaders}")
    private val allowedHeaders: String? = null

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(allowedOrigins)
        configuration.allowedMethods = listOf(allowedMethods)
        configuration.allowedHeaders = listOf(allowedHeaders)
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
