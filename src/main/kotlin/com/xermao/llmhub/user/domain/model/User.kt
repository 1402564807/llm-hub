package com.xermao.llmhub.user.domain.model

import com.xermao.llmhub.common.domain.model.superclass.CreatedTime
import com.xermao.llmhub.common.domain.model.superclass.Id
import org.babyfish.jimmer.sql.*
import java.time.LocalDateTime

@Entity
@Table(name = "llmhub.user")
interface User:Id,CreatedTime {

    @Key
    val username: String

    val createTime: LocalDateTime

    val password: String

    val enable: Boolean

    @ManyToMany
    @JoinTable(name = "user_role_map", joinColumnName = "user_id", inverseJoinColumnName = "role_id")
    val roles: List<Role>
}
