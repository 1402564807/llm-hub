package com.xermao.llmhub.user.application

import com.xermao.llmhub.user.UserAppApi
import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput
import com.xermao.llmhub.user.domain.service.UserDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAppService(
    private val userDomainService: UserDomainService
) : UserAppApi {

    @Transactional(rollbackFor = [Throwable::class])
    override fun addGeneralUser(userRoleInputShort: UserRoleShortInput) {
        val savedId: Long = userDomainService.addGeneralUser(userRoleInputShort)
    }
}
