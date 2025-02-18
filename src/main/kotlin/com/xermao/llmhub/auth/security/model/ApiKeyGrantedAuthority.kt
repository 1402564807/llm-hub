package com.xermao.llmhub.auth.security.model

import org.springframework.security.core.GrantedAuthority

data class ApiKeyGrantedAuthority(
    val apiKey: String
) : GrantedAuthority {
    override fun getAuthority(): String {
        return apiKey
    }
}
