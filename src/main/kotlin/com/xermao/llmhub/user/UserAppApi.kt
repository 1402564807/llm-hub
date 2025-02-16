package com.xermao.llmhub.user

import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput

interface UserAppApi {
    fun addGeneralUser(userRoleInputShort: UserRoleShortInput)
}
