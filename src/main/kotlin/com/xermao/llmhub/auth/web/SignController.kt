package com.xermao.llmhub.auth.web

import com.xermao.llmhub.auth.application.SignAppService
import com.xermao.llmhub.auth.config.Jwt
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class SignController(
    private val signAppService: SignAppService,
    private val jwt: Jwt
) {

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-in")
    fun signIn(request: HttpServletRequest, response: HttpServletResponse, @RequestBody @Validated signInVm: SignInVm) {
        jwt.makeToken(request, response, signAppService.signIn(signInVm))
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Validated signUpVm: SignUpVm) {
        signAppService.signUp(signUpVm)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-out")
    fun signOut(request: HttpServletRequest, response: HttpServletResponse) {
        jwt.removeToken(request, response)
    }
}
