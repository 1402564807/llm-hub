package com.xermao.llmhub.model.entity.superclass

import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
interface CreatedTime {
    val createdTime: LocalDateTime
}