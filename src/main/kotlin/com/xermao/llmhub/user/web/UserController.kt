package com.xermao.llmhub.user.web

import com.xermao.llmhub.user.domain.model.User
import com.xermao.llmhub.user.domain.model.by
import com.xermao.llmhub.user.domain.repository.UserAggregateRepository
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userAggregateRepository: UserAggregateRepository
) {

    @PreAuthorize("hasAuthority(T(com.xermao.llmhub.user.domain.enums.EPermission).READ_USER_ROLE_PERMISSION)")
    @GetMapping("/search")
    fun pageQuery(
        @RequestParam pageIndex: Int,
        @RequestParam pageSize: Int,
        @ModelAttribute userPageQueryVm: UserPageQueryVm
    ): Page<User> {
        return userAggregateRepository.fetchAggregateWithPageBy(
            USER_AGGREGATE, userPageQueryVm, pageIndex, pageSize
        )
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
