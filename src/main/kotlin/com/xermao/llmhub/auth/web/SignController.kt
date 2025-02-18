package com.xermao.llmhub.auth.web

import com.xermao.llmhub.auth.application.SignAppService
import com.xermao.llmhub.auth.security.utils.Jwt
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth")
class SignController(
    private val signAppService: SignAppService,
    private val jwt: Jwt
) {

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Validated signInVm: SignInVm): Mono<Void> {
        return jwt.makeToken(signAppService.signIn(signInVm))
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Validated signUpVm: SignUpVm) {
        signAppService.signUp(signUpVm)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-out")
    fun signOut(): Mono<Void> {
        return jwt.removeToken()
    }
}
