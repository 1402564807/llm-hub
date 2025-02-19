package com.xermao.llmhub.user.domain.model

import com.xermao.llmhub.common.domain.model.superclass.Id
import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "llm_hub.role")
interface Role : Id {

    @Key
    val code: String

    val name: String

    @ManyToMany(mappedBy = "roles")
    val users: List<User>

    @ManyToMany
    @JoinTable(name = "llm_hub.role_permission_map", joinColumnName = "role_id", inverseJoinColumnName = "permission_id")
    val permissions: List<Permission>
}
