package com.xermao.llmhub.security.model

import com.xermao.llmhub.model.entity.Token
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.math.BigDecimal
import java.time.LocalDateTime

data class TokenDetail(
    val id: String,
    val userId: String,
    val name: String,
    val key: String,
    val subnet: List<String>,
    val models: List<String>,
    val usedQuota: BigDecimal,
    val allQuota: BigDecimal,
    val unlimitedQuota: Boolean,
    val createdTime: LocalDateTime,
    val accessedTime: LocalDateTime?,
    val expiredTime: LocalDateTime = LocalDateTime.MAX,
    val status: Boolean = true,
) : UserDetails {

    constructor(token: Token) : this(
        id = token.id,
        userId = token.userId,
        name = token.name,
        key = token.key,
        subnet = token.subnet,
        models = token.models,
        usedQuota = token.usedQuota,
        allQuota = token.allQuota,
        unlimitedQuota = token.unlimitedQuota,
        createdTime = token.createdTime,
        accessedTime = token.accessedTime,
        status = token.status
    )


    /**
     * 获取可使用模型和网络白名单
     */
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val list = mutableListOf<TokenGrantedAuthority>()
        subnet.map { TokenGrantedAuthority(subnet = it) }.forEach { list.add(it) }
        models.map { TokenGrantedAuthority(model = it) }.forEach { list.add(it) }
        return list
    }

    override fun getPassword(): String {
        return key
    }

    override fun getUsername(): String {
        return name
    }

    override fun isAccountNonExpired(): Boolean {
        return LocalDateTime.now().isBefore(expiredTime)
    }

    override fun isAccountNonLocked(): Boolean {
        return status
    }

    override fun isCredentialsNonExpired(): Boolean {
        return isAccountNonExpired
    }

    override fun isEnabled(): Boolean {
        return isAccountNonLocked
    }
}
