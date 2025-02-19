package com.xermao.llmhub.user.domain.repository

import com.xermao.llmhub.user.domain.model.Role
import com.xermao.llmhub.user.domain.model.dto.RolePermissionShortInput
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.fetcher.Fetcher
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

interface RoleRepository : KRepository<Role, Long> {
    fun findByCode(code: String): Role

    fun findByCodeIn(codeList: List<String>): List<Role>

    fun fetchRoleComplexBy(fetcher: Fetcher<Role>): List<Role> {
        return sql.createQuery(Role::class) {
            select(table.fetch(fetcher))
        }.execute()
    }

    fun upsert(rolePermissionShortInput: RolePermissionShortInput) {
        sql.save<Any>(rolePermissionShortInput)
    }
}
