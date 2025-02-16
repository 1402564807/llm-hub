package com.xermao.llmhub.model.entity

import com.xermao.llmhub.common.domain.model.superclass.CreatedTime
import com.xermao.llmhub.common.domain.model.superclass.Id
import com.xermao.llmhub.common.domain.model.superclass.UpdatedTime
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.OneToMany
import org.babyfish.jimmer.sql.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "\"user\"")
interface User: Id, CreatedTime, UpdatedTime {
    val username: String
    val password: String
    val nickname: String
    val role: String
    val email: String
    val allQuota: BigDecimal
    val usedQuota: BigDecimal
    val requestCount: Int
    val group: String
    val deletedTime: LocalDateTime?

    @OneToMany(mappedBy = "user")
    val tokens: List<Token>
}