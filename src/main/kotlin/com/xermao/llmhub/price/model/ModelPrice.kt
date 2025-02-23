package com.xermao.llmhub.price.model

import com.xermao.llmhub.common.domain.model.superclass.Id
import com.xermao.llmhub.provider.domain.constant.ProviderEnum
import java.math.BigDecimal

interface ModelPrice : Id {
    val type: ProviderEnum

    val inputPrice: BigDecimal
    val outputPrice: BigDecimal
    val cacheKPrice: BigDecimal
}
