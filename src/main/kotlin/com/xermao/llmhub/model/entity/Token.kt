package com.xermao.llmhub.model.entity

import com.xermao.llmhub.common.domain.model.superclass.Id
import org.babyfish.jimmer.sql.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
interface Token : Id {

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User

    @Key
    val name: String

    val key: String

    @Column(sqlElementType = "varchar")
    val subnet: List<String>

    @Column(sqlElementType = "varchar")
    val models: List<String>

    val usedQuota: BigDecimal
    val allQuota: BigDecimal
    val unlimitedQuota: Boolean

    val createdTime: LocalDateTime
    val accessedTime: LocalDateTime?
    val expiredTime: LocalDateTime?

    val status: Boolean
}