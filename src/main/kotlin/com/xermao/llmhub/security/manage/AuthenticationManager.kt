package com.xermao.llmhub.security.manage

import com.xermao.llmhub.security.model.TokenAuthenticationToken
import com.xermao.llmhub.security.model.TokenDetail
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import reactor.core.publisher.Mono

/**
 * ReactiveAuthenticationManager
 *
 * 职责	认证：验证用户身份是否合法
 * 输入	Authentication 对象（用户名和密码）
 * 输出	包含用户权限的 Authentication 对象
 * 使用场景	登录验证、Token 验证
 * 依赖关系	通常依赖 UserDetailsService 或数据库
 * 调用时机	在用户登录或访问受保护资源时调用
 */
class AuthenticationManager(
    private val tokenService: ReactiveUserDetailsService
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {

        val authorities = authentication.authorities
        return tokenService.findByUsername(authorities.first().authority)
            .switchIfEmpty(Mono.error(BadCredentialsException("Token 不存在")))
            .filter { it.isAccountNonExpired }
            .switchIfEmpty(Mono.error(AccountExpiredException("Token 过期")))
            .filter { it.isAccountNonLocked }
            .switchIfEmpty(Mono.error(LockedException("Token 已被锁定")))
            .map { TokenAuthenticationToken(it as TokenDetail) }
    }
}