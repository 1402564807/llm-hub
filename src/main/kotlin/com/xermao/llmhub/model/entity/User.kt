package com.xermao.llmhub.model.entity

import com.xermao.llmhub.model.entity.superclass.CreatedTime
import com.xermao.llmhub.model.entity.superclass.Id
import com.xermao.llmhub.model.entity.superclass.UpdatedTime
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.OneToMany
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
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