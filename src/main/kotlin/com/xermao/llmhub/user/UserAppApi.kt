package com.xermao.llmhub.user

import com.xermao.llmhub.user.domain.model.dto.UserAddInput
import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput
import com.xermao.llmhub.user.domain.model.dto.UserUpdateInput

interface UserAppApi {
    fun addGeneralUser(userRoleInputShort: UserRoleShortInput): Long
    fun addUser(userAddInput: UserAddInput): Long
    fun updateUser(userUpdateInput: UserUpdateInput): Long
    fun deleteUser(id: Long): Boolean
}
