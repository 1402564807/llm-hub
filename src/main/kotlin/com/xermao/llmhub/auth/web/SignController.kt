package com.xermao.llmhub.auth.web

import com.xermao.llmhub.auth.application.SignAppService
import com.xermao.llmhub.auth.security.model.JwtAuthenticationToken
import com.xermao.llmhub.auth.security.utils.Jwt
import com.xermao.llmhub.common.domain.model.R
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/auth")
class SignController(
    private val signAppService: SignAppService,
    private val jwt: Jwt
) {

    @PostMapping("/refresh-token")
    fun refreshToken(): Mono<R<SignResult>> {
        return ReactiveSecurityContextHolder.getContext().flatMap {
            if (it.authentication !is JwtAuthenticationToken) return@flatMap Mono.empty()
            val authentication: JwtAuthenticationToken = it.authentication as JwtAuthenticationToken
            val userDetails = authentication.principal as UserDetails
            val signResult = SignResult(
                jwt.create(userDetails.username),
                jwt.create(userDetails.username, jwt.expirationMax),
                LocalDateTime.now().plusMinutes(jwt.expirationMin),
            )
            Mono.just(R.success(signResult))
        }
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Validated signInVm: SignInVm): Mono<R<SignResult>> {
        val signResult = signAppService.signIn(signInVm)
        return Mono.just(R.success(signResult))
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Validated signUpVm: SignUpVm) {
        signAppService.signUp(signUpVm)
    }

    @PostMapping("/sign-out")
    fun signOut(): Mono<Void> {
        return jwt.removeToken()
    }
}
