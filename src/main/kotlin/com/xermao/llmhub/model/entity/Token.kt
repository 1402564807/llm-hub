package com.xermao.llmhub.model.entity

import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Key
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
interface Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String

    val userId: String

    @Key
    val name: String

    val key: String

    val subnet: List<String>

    val models: List<String>

    val usedQuota: BigDecimal
    val allQuota: BigDecimal
    val unlimitedQuota: Boolean

    val createdTime: LocalDateTime
    val accessedTime: LocalDateTime?
    val expiredTime: LocalDateTime?

    val status: Boolean
}