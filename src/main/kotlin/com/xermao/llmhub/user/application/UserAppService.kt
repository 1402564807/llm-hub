package com.xermao.llmhub.user.application

import com.xermao.llmhub.user.UserAppApi
import com.xermao.llmhub.user.domain.model.dto.UserAddInput
import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput
import com.xermao.llmhub.user.domain.model.dto.UserUpdateInput
import com.xermao.llmhub.user.domain.service.UserDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAppService(
    private val userDomainService: UserDomainService
) : UserAppApi {

    @Transactional(rollbackFor = [Throwable::class])
    override fun addGeneralUser(userRoleInputShort: UserRoleShortInput): Long {
        val savedId: Long = userDomainService.addGeneralUser(userRoleInputShort)
        return savedId
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun addUser(userAddInput: UserAddInput): Long {
        return userDomainService.addUser(userAddInput)
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun updateUser(userUpdateInput: UserUpdateInput): Long {
        return userDomainService.updateUser(userUpdateInput)
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun deleteUser(id: Long): Boolean {
        return userDomainService.deleteUser(id)
    }
}
