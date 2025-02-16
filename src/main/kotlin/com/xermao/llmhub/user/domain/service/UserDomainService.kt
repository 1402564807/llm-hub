package com.xermao.llmhub.user.domain.service

import com.xermao.llmhub.user.UserDomainApi
import com.xermao.llmhub.user.UserQueryDto
import com.xermao.llmhub.user.domain.enums.ERole
import com.xermao.llmhub.user.domain.model.User
import com.xermao.llmhub.user.domain.model.by
import com.xermao.llmhub.user.domain.model.dto.UserRolePermissionView
import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput
import com.xermao.llmhub.user.domain.repository.RoleRepository
import com.xermao.llmhub.user.domain.repository.UserAggregateRepository
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.stereotype.Service

@Service
class UserDomainService(
    private val userAggregateRepository: UserAggregateRepository,
    private val roleRepository: RoleRepository,
    private val sqlClient: KSqlClient,
) : UserDomainApi {

    override fun queryUniqueUserRolePermissionBy(userQueryDto: UserQueryDto): UserRolePermissionView? {
        val userModel: User? = userAggregateRepository.fetchUniqueUserBy(
            newFetcher(User::class).by {
                allTableFields()
                roles {
                    allTableFields()
                    permissions { allTableFields() }
                }
            },
            userQueryDto
        )

        return if (userModel == null) {
            null
        } else {
            UserRolePermissionView(userModel)
        }
    }

    override fun addGeneralUser(userRoleInputShort: UserRoleShortInput): String {
        val roles = roleRepository.findByCodeIn(listOf(ERole.GENERAL.name))
        val input = userRoleInputShort.copy(rolesId = roles.map { it.id }.toList())
        val save = sqlClient.save(input)
        return save.modifiedEntity.id
    }
}
