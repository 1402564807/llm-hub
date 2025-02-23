package com.xermao.llmhub.provider.domain.model

import com.xermao.llmhub.common.domain.model.superclass.CreatedTime
import com.xermao.llmhub.common.domain.model.superclass.Id
import com.xermao.llmhub.provider.domain.constant.ProviderEnum
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Serialized
import org.babyfish.jimmer.sql.Table
import java.math.BigDecimal

@Entity
@Table(name = "llm_hub.provider")
interface Provider : Id, CreatedTime {

    val type: ProviderEnum
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
