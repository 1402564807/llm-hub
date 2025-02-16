package com.xermao.llmhub.user.domain.repository

import com.xermao.llmhub.user.domain.model.Permission
import com.xermao.llmhub.user.domain.model.dto.PermissionInput
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode

interface PermissionRepository : KRepository<Permission, Long> {
    fun findByCode(code: String?): Permission?

    fun findByName(name: String?): Permission?

    fun upsert(permissionInput: PermissionInput) {
        sql.save<Any>(permissionInput, SaveMode.UPSERT)
    }
}
