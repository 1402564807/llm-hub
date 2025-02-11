package com.xermao.llmhub.security.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

data class TokenAuthenticationToken(
    val details: UserDetails
) : AbstractAuthenticationToken(details.authorities) {
    override fun getCredentials(): Any {
        if (details is TokenDetail)
            return details.key
        return details.password
    }

    override fun getPrincipal(): Any {
        if (details is TokenDetail)
            return details.name
        return details.username
    }
}
