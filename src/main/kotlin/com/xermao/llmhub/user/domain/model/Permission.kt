package com.xermao.llmhub.user.domain.model

import com.xermao.llmhub.common.domain.model.superclass.Id
import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "permission")
interface Permission:Id {

    @Key
    val code: String

    val name: String

    @ManyToMany(mappedBy = "permissions")
    val roles: List<Role>
}
