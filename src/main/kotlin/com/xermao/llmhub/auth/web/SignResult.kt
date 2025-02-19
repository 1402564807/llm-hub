package com.xermao.llmhub.auth.web

import com.xermao.llmhub.user.domain.model.dto.UserRolePermissionView
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

data class SignResult(
    val username: String,
    val nickname: String?,
    val roles: Set<String>?,
    val permissions: Set<String>?,
    val accessToken: String,
    val refreshToken: String,
    val expires: LocalDateTime,
) {

    constructor(
        userRolePermissionView: UserRolePermissionView,
        accessToken: String,
        refreshToken: String,
        expires: LocalDateTime,
    ) : this(
        username = userRolePermissionView.username,
        nickname = userRolePermissionView.nickname,
        roles = userRolePermissionView.roles.map { it.name }.toSet(),
        permissions = userRolePermissionView.roles.flatMap { it.permissions.map { perm -> perm.name } }.toSet(),
        accessToken = accessToken,
        refreshToken = refreshToken,
        expires = expires,
    )

    constructor(
        userDetails: UserDetails,
        accessToken: String,
        refreshToken: String,
        expires: LocalDateTime
    ) : this(
        username = userDetails.username,
        accessToken = accessToken,
        refreshToken = refreshToken,
        expires = expires,
        nickname = null,
        roles = null,
        permissions = null,
    )

}
