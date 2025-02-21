package com.xermao.llmhub.auth.security.service

import com.xermao.llmhub.auth.security.model.TokenDetail
import com.xermao.llmhub.token.model.Token
import com.xermao.llmhub.token.model.fetchBy
import com.xermao.llmhub.token.model.key
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TokenDetailsServiceImpl(
    private val sqlClient: KSqlClient
) : ReactiveUserDetailsService {
    override fun findByUsername(token: String): Mono<UserDetails> {

        val tokenDetail = sqlClient.createQuery(Token::class) {
            where(table.key.eq(token))
            select(table.fetchBy {
                allTableFields()
                user {
                    allQuota()
                    usedQuota()
                    group()
                }
            })
        }.fetchOneOrNull() ?: return Mono.empty()

        return Mono.just(TokenDetail(tokenDetail))
    }
}
