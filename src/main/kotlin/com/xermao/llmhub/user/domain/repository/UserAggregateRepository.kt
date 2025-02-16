package com.xermao.llmhub.user.domain.repository

import com.xermao.llmhub.user.UserQueryDto
import com.xermao.llmhub.user.domain.model.User
import com.xermao.llmhub.user.domain.model.enable
import com.xermao.llmhub.user.domain.model.id
import com.xermao.llmhub.user.domain.model.username
import com.xermao.llmhub.user.web.UserPageQueryVm
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.`eq?`
import org.springframework.stereotype.Repository

@Repository
class UserAggregateRepository(
    private val sqlClient: KSqlClient
) {

    fun fetchUniqueUserBy(fetcher: Fetcher<User>, userQueryDto: UserQueryDto): User? {
        return sqlClient.createQuery(User::class) {
            where(table.username.`eq?`(userQueryDto.username))
            where(table.id.`eq?`(userQueryDto.id))
            select(table.fetch(fetcher))
        }.fetchOne()
    }

    fun fetchAggregateWithPageBy(
        fetcher: Fetcher<User>,
        userPageQueryVm: UserPageQueryVm,
        pageIndex: Int,
        pageSize: Int
    ): Page<User> {

        return sqlClient.createQuery(User::class) {
            where(table.enable.`eq?`(userPageQueryVm.enable))
            where(table.username.`eq?`(userPageQueryVm.username))
            select(table.fetch(fetcher))
        }.fetchPage(pageIndex, pageSize)

    }
}
