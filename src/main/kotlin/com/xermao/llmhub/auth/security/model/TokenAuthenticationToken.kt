package com.xermao.llmhub.auth.security.model

import org.springframework.security.authentication.AbstractAuthenticationToken

data class TokenAuthenticationToken(
    val details: TokenDetail
) : AbstractAuthenticationToken(details.authorities) {

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun getCredentials(): Any {
        return details.key
    }

    override fun getPrincipal(): Any {
        return details.name
    }
}
