package com.xermao.llmhub.user.domain.model

import com.xermao.llmhub.common.domain.model.superclass.Id
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany
import org.babyfish.jimmer.sql.Table

@Entity
@Table(name = "permission")
interface Permission : Id {

    @Key
    val code: String

    val name: String

    @ManyToMany(mappedBy = "permissions")
    val roles: List<Role>
}
