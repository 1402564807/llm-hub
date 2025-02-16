package com.xermao.llmhub.auth.config

import com.xermao.llmhub.auth.application.UserDetailsAppService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig(
    private val userDetailsAppService: UserDetailsAppService,
    private val jwt: Jwt,
    private val corsConfigurationSource: CorsConfigurationSource
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun publicEndPointMatcher(): RequestMatcher {
        return OrRequestMatcher(
            AntPathRequestMatcher("/auth/sign-in", HttpMethod.POST.name()),
            AntPathRequestMatcher("/auth/sign-up", HttpMethod.POST.name()),
            AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.name()),
            AntPathRequestMatcher("/swagger-ui/**", HttpMethod.GET.name()),
            AntPathRequestMatcher("/swagger-ui.html", HttpMethod.GET.name()),
            AntPathRequestMatcher("/swagger-ui.yml", HttpMethod.GET.name()),
            AntPathRequestMatcher("/browser.zip", HttpMethod.GET.name()),
            AntPathRequestMatcher("/error")
        )
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val restfulAuthenticationEntryPointHandler =
            RestfulAuthenticationEntryPointHandler()
        /*
    <Stateless API CSRF protection>
    http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
    */
        http.cors { corsConfigurer: CorsConfigurer<HttpSecurity?> ->
            corsConfigurer.configurationSource(
                corsConfigurationSource
            )
        }
        http.csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(publicEndPointMatcher())
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .exceptionHandling { exceptionHandling: ExceptionHandlingConfigurer<HttpSecurity?> ->
                exceptionHandling
                    .accessDeniedHandler(restfulAuthenticationEntryPointHandler)
                    .authenticationEntryPoint(restfulAuthenticationEntryPointHandler)
            }
            .sessionManagement { session: SessionManagementConfigurer<HttpSecurity?> ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterAt(
                JwtAuthenticationFilter(jwt, userDetailsAppService),
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }
}
