package com.xermao.llmhub.user.web

import com.xermao.llmhub.common.domain.model.R
import com.xermao.llmhub.user.application.UserAppService
import com.xermao.llmhub.user.domain.model.User
import com.xermao.llmhub.user.domain.model.by
import com.xermao.llmhub.user.domain.model.dto.UserAddInput
import com.xermao.llmhub.user.domain.model.dto.UserUpdateInput
import com.xermao.llmhub.user.domain.repository.UserAggregateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userAggregateRepository: UserAggregateRepository,
    private val userAppService: UserAppService
) {

    @PostMapping("/search")
    @PreAuthorize("hasAuthority(T(com.xermao.llmhub.user.domain.enums.EPermission).READ_USER_ROLE_PERMISSION)")
    suspend fun pageQuery(@RequestBody userPageQueryVm: UserPageQueryVm): R<Page<User>> {
        val page = userAggregateRepository.fetchAggregateWithPageBy(
            USER_AGGREGATE, userPageQueryVm
        )
        return R.success(page)
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.xermao.llmhub.user.domain.enums.EPermission).WRITE_USER_ROLE_PERMISSION)")
    suspend fun addUser(@RequestBody userAddInput: UserAddInput): R<Long> {
        val user = withContext(Dispatchers.IO) {
            userAppService.addUser(userAddInput)
        }
        return R.success(user)
    }

    @PutMapping
    @PreAuthorize("hasAuthority(T(com.xermao.llmhub.user.domain.enums.EPermission).WRITE_USER_ROLE_PERMISSION)")
    suspend fun updateUser(@RequestBody userUpdateInput: UserUpdateInput): R<Long> {
        val user = withContext(Dispatchers.IO) {
            userAppService.updateUser(userUpdateInput)
        }
        return R.success(user)
    }

    @DeleteMapping("/{id}")
    suspend fun deleteUser(@PathVariable("id") id: Long): R<Boolean> {
        val result = withContext(Dispatchers.IO) {
            userAppService.deleteUser(id)
        }
        return R.success(result)
    }

    companion object {
        private val USER_AGGREGATE: Fetcher<User> = newFetcher(User::class).by {
            allTableFields()
            roles {
                allTableFields()
                permissions { allTableFields() }
            }
        }
    }
}
