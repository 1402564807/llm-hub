package com.xermao.llmhub.auth.security.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class ApiKeyAuthenticationToken(
    val authorities: List<GrantedAuthority>
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any {
        return authorities
    }

    override fun getPrincipal(): Any {
        return authorities
    }
}