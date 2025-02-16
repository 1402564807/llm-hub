package com.xermao.llmhub.user.web

import com.xermao.llmhub.user.domain.model.Role
import com.xermao.llmhub.user.domain.model.by
import com.xermao.llmhub.user.domain.model.dto.RolePermissionShortInput
import com.xermao.llmhub.user.domain.repository.RoleRepository
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/roles")
class RoleController(
    private val roleRepository: RoleRepository
) {

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.xermao.llmhub.user.domain.enums.EPermission).READ_USER_ROLE_PERMISSION)")
    fun roles(): List<Role> = roleRepository.fetchRoleComplexBy(ROLE_FETCHER)

    @PreAuthorize("hasAuthority(T(com.xermao.llmhub.user.domain.enums.EPermission).WRITE_USER_ROLE_PERMISSION)")
    @PostMapping("/upsert")
    fun upsert(@RequestBody @Validated rolePermissionShortInput: RolePermissionShortInput) {
        roleRepository.upsert(rolePermissionShortInput)
    }

    companion object {
        private val ROLE_FETCHER: Fetcher<Role> = newFetcher(Role::class).by {
            allTableFields()
            permissions {
                allTableFields()
            }
        }
    }
}
