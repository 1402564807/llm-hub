package com.xermao.llmhub.auth.security.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.SpringSecurityCoreVersion
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serial

class JwtAuthenticationToken : AbstractAuthenticationToken {
    private val principal: Any

    private val credentials: String

    constructor(principal: Any, credentials: String) : super(null) {
        this.principal = principal
        this.credentials = credentials
        super.setAuthenticated(false)
    }

    constructor(principal: Any, credentials: String, authorities: Collection<GrantedAuthority?>?) : super(authorities) {
        this.principal = principal
        this.credentials = credentials
        super.setAuthenticated(true)
    }

    override fun getCredentials(): String {
        return this.credentials
    }

    override fun getPrincipal(): Any {
        return this.principal
    }

    companion object {
        @Serial
        private val serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID

        fun unauthenticated(userIdentify: String, token: String): JwtAuthenticationToken {
            return JwtAuthenticationToken(userIdentify, token)
        }

        fun authenticated(
            principal: UserDetails, token: String, authorities: Collection<GrantedAuthority?>?
        ): JwtAuthenticationToken {
            return JwtAuthenticationToken(principal, token, authorities)
        }
    }
}
