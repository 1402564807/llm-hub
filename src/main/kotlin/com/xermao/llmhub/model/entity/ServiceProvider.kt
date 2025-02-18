package com.xermao.llmhub.model.entity

import com.xermao.llmhub.common.domain.constant.ModelType
import com.xermao.llmhub.common.domain.model.superclass.CreatedTime
import com.xermao.llmhub.common.domain.model.superclass.Id
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Serialized
import java.math.BigDecimal

@Entity
interface ServiceProvider : Id, CreatedTime {

    val type: ModelType
    val name: String
    val priority: Int
    val weight: Int
    val key: String
    val baseUrl: String

    @Column(sqlElementType = "varchar")
    val models: List<String>

    @Column(sqlElementType = "varchar")
    val group: List<String>
    val usedQuota: BigDecimal

    @Serialized
    val modelMap: Map<String, String>
    val status: Boolean
}