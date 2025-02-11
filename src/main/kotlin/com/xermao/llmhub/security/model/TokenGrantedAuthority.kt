package com.xermao.llmhub.security.model

import org.springframework.security.core.GrantedAuthority

data class TokenGrantedAuthority(
    val model: String? = null,
    val subnet: String? = null,
) : GrantedAuthority {
    override fun getAuthority(): String {
        if (!model.isNullOrEmpty()) {
            return MODEL_PREFIX + model
        }
        if (!subnet.isNullOrEmpty()) {
            return SUBNET_PREFIX + subnet
        }
        return NONE
    }

    companion object {
        private const val MODEL_PREFIX = "model_"
        private const val SUBNET_PREFIX = "subnet_"
        private const val NONE = "none"

        fun isModel(authority: TokenGrantedAuthority): Boolean {
            return authority.authority.startsWith(MODEL_PREFIX)
        }

        fun isSubnet(authority: TokenGrantedAuthority): Boolean {
            return authority.authority.startsWith(SUBNET_PREFIX)
        }
    }
}
