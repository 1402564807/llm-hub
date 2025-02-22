package com.xermao.llmhub.user.domain.model

import com.xermao.llmhub.common.domain.model.superclass.CreatedTime
import com.xermao.llmhub.common.domain.model.superclass.Id
import com.xermao.llmhub.token.model.Token
import org.babyfish.jimmer.sql.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "llm_hub.user")
interface User : Id, CreatedTime {

    @Key
    val username: String
    val password: String

    val nickname: String
    val email: String
    val allQuota: BigDecimal
    val usedQuota: BigDecimal
    val requestCount: Int
    @Column(name = "\"group\"")
    val group: String
    val deletedTime: LocalDateTime?

    val enable: Boolean

    @OneToMany(mappedBy = "user")
    val tokens: List<Token>

    @ManyToMany
    @JoinTable(name = "llm_hub.user_role_map", joinColumnName = "user_id", inverseJoinColumnName = "role_id")
    val roles: List<Role>
}
