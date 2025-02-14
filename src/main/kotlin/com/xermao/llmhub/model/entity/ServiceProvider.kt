package com.xermao.llmhub.model.entity

import com.xermao.llmhub.model.entity.superclass.CreatedTime
import com.xermao.llmhub.model.entity.superclass.Id
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Serialized
import java.math.BigDecimal

@Entity
interface ServiceProvider : Id, CreatedTime {

    val type: Int
    val name: String
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