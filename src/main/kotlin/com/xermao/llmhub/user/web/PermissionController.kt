package com.xermao.llmhub.user.web

import com.xermao.llmhub.user.domain.model.Permission
import com.xermao.llmhub.user.domain.model.dto.PermissionInput
import com.xermao.llmhub.user.domain.repository.PermissionRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/permissions")
class PermissionController(
    private val permissionRepository: PermissionRepository
) {

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.xermao.llmhub.user.domain.enums.EPermission).READ_USER_ROLE_PERMISSION)")
    fun permissions(): List<Permission> = permissionRepository.findAll()

    @PreAuthorize("hasAuthority(T(com.xermao.llmhub.user.domain.enums.EPermission).WRITE_USER_ROLE_PERMISSION)")
    @PostMapping("/upsert")
    fun upsert(@RequestBody @Validated permissionInput: PermissionInput) {
        permissionRepository.upsert(permissionInput)
    }
}
