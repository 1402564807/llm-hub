package com.xermao.llmhub.model.entity

import com.xermao.llmhub.constant.ModelType
import com.xermao.llmhub.common.domain.model.superclass.Id
import java.math.BigDecimal

interface ModelPrice: Id {
    val type: ModelType

    val inputPrice: BigDecimal
    val outputPrice: BigDecimal
    val cacheKPrice: BigDecimal
}