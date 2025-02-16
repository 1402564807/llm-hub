package com.xermao.llmhub.common.domain.model.superclass

import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
interface UpdatedTime {
    val updatedTime: LocalDateTime?
}