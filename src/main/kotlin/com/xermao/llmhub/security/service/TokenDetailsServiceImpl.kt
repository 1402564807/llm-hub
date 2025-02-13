package com.xermao.llmhub.security.service

import com.xermao.llmhub.model.entity.Token
import com.xermao.llmhub.model.key
import com.xermao.llmhub.security.model.TokenDetail
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
            select(table)
        }.fetchOneOrNull() ?: return Mono.empty()

        return Mono.just(TokenDetail(tokenDetail))
    }
}