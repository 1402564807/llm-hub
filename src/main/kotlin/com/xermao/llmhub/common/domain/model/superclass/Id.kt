package com.xermao.llmhub.common.domain.model.superclass

import com.xermao.llmhub.common.utils.SnowIdGenerator
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.MappedSuperclass

@MappedSuperclass
interface Id {
    @Id
    @GeneratedValue(generatorType = SnowIdGenerator::class)
    val id: Long
}