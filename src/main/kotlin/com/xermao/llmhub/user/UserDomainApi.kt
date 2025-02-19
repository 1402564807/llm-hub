package com.xermao.llmhub.user

import com.xermao.llmhub.user.domain.model.dto.UserRolePermissionView
import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput

interface UserDomainApi {
    fun queryUniqueUserRolePermissionBy(userQueryDto: UserQueryDto): UserRolePermissionView?

    fun addGeneralUser(userRoleInputShort: UserRoleShortInput): Long
}
