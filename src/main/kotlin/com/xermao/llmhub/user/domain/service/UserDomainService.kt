package com.xermao.llmhub.user.domain.service

import com.xermao.llmhub.user.UserDomainApi
import com.xermao.llmhub.user.UserQueryDto
import com.xermao.llmhub.user.domain.enums.ERole
import com.xermao.llmhub.user.domain.model.*
import com.xermao.llmhub.user.domain.model.dto.UserAddInput
import com.xermao.llmhub.user.domain.model.dto.UserRolePermissionView
import com.xermao.llmhub.user.domain.model.dto.UserRoleShortInput
import com.xermao.llmhub.user.domain.model.dto.UserUpdateInput
import com.xermao.llmhub.user.domain.repository.RoleRepository
import com.xermao.llmhub.user.domain.repository.UserAggregateRepository
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserDomainService(
    private val userAggregateRepository: UserAggregateRepository,
    private val roleRepository: RoleRepository,
    private val sqlClient: KSqlClient,
    private val passwordEncoder: PasswordEncoder,
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

    override fun addGeneralUser(userRoleInputShort: UserRoleShortInput): Long {
        val roles = roleRepository.findByCodeIn(listOf(ERole.GENERAL.name))
        val password = passwordEncoder.encode(userRoleInputShort.password)
        val input = userRoleInputShort.copy(rolesId = roles.map { it.id }.toList(), password = password)
        val save = sqlClient.insert(input)
        return save.modifiedEntity.id
    }

    override fun addUser(userAddInput: UserAddInput): Long {
        val roles = roleRepository.findByCodeIn(listOf(ERole.GENERAL.name))
        val password = passwordEncoder.encode(userAddInput.password)
        val addInput = userAddInput.copy(
            rolesId = roles.map { it.id }.toList(),
            nickname = userAddInput.nickname.ifBlank { userAddInput.username },
            password = password
        )
        val save = sqlClient.insert(addInput)
        return save.modifiedEntity.id
    }

    override fun updateUser(userUpdateInput: UserUpdateInput): Long {
        val execute = sqlClient.createUpdate(User::class) {
            where(table.id.eq(userUpdateInput.id))
            userUpdateInput.password?.let { set(table.password, passwordEncoder.encode(it)) }
            userUpdateInput.enable?.let { set(table.enable, it) }
            userUpdateInput.email?.let { set(table.email, it) }
            userUpdateInput.allQuota?.let { set(table.allQuota, it) }
            userUpdateInput.group?.let { set(table.group, it) }
        }.execute()
        return execute.toLong()
    }

    override fun deleteUser(id: Long): Boolean {
        val result = sqlClient.deleteById(User::class, id)
        return result.totalAffectedRowCount != 0
    }

}
