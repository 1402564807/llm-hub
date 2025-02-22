package com.xermao.llmhub.user

import com.xermao.llmhub.user.domain.model.dto.UserAddInput
import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput
import com.xermao.llmhub.user.domain.model.dto.UserUpdateInput
import reactor.core.publisher.Mono

interface UserAppApi {
    fun addGeneralUser(userRoleInputShort: UserRoleShortInput): Long
    fun addUser(userAddInput: UserAddInput): Mono<Long>
    fun updateUser(userUpdateInput: UserUpdateInput): Mono<Long>
}
