package com.xermao.llmhub.auth.application

import com.xermao.llmhub.auth.security.utils.Jwt
import com.xermao.llmhub.auth.web.SignInVm
import com.xermao.llmhub.auth.web.SignResult
import com.xermao.llmhub.auth.web.SignUpVm
import com.xermao.llmhub.common.BusinessException
import com.xermao.llmhub.user.UserAppApi
import com.xermao.llmhub.user.UserDomainApi
import com.xermao.llmhub.user.UserQueryDto
import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SignAppService(
    private val userDomainApi: UserDomainApi,
    private val userAppApi: UserAppApi,
    private val passwordEncoder: PasswordEncoder,
    private val jwt: Jwt,
) {

    fun signIn(signInVm: SignInVm): SignResult {
        val userRolePermissionView = userDomainApi.queryUniqueUserRolePermissionBy(
            UserQueryDto(null, signInVm.username)
        )
        if (userRolePermissionView == null) {
            throw BusinessException(String.format("%s user not found", signInVm.username))
        }
        if (!passwordEncoder.matches(signInVm.password, userRolePermissionView.password)) {
            throw BusinessException("password invalid")
        }
        val username = userRolePermissionView.username
        return SignResult(
            userRolePermissionView = userRolePermissionView,
            accessToken = jwt.create(username),
            refreshToken = jwt.create(username, jwt.expirationMax),
            expires = LocalDateTime.now().plusMinutes(jwt.expirationMin)
        )
    }

    fun signUp(signUpVm: SignUpVm) {
        if (isUsernameDuplicate(signUpVm.username)) {
            throw BusinessException("username ${signUpVm.username} already exist")
        }
        userAppApi.addGeneralUser(
            UserRoleShortInput(signUpVm.username, passwordEncoder.encode(signUpVm.password))
        )
    }

    fun isUsernameDuplicate(username: String): Boolean {
        val userRolePermissionView = userDomainApi.queryUniqueUserRolePermissionBy(UserQueryDto(null, username))
        return userRolePermissionView != null
    }
}
