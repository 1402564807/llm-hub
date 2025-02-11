package com.xermao.llmhub.security.provider

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class CustomAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        TODO("Not yet implemented")
    }

    override fun supports(authentication: Class<*>?): Boolean {
        TODO("Not yet implemented")
    }
}