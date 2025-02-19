package com.xermao.llmhub.auth.application

import com.xermao.llmhub.user.UserDomainApi
import com.xermao.llmhub.user.UserQueryDto
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsAppService(
    private val userDomainApi: UserDomainApi
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(id: String): UserDetails {
        val userRolePermissionView = userDomainApi.queryUniqueUserRolePermissionBy(UserQueryDto(id.toLong(), null))
            ?: throw UsernameNotFoundException(String.format("uid %s user not found", id))
        return User(
            userRolePermissionView.username,
            userRolePermissionView.password,
            userRolePermissionView.enable,
            true,
            true,
            true,
            userRolePermissionView.roles
                .flatMap { role -> role.permissions.map { SimpleGrantedAuthority(it.code) } }
                .toList())
    }
}
