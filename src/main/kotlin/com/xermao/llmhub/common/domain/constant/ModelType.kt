package com.xermao.llmhub.common.domain.constant

import org.babyfish.jimmer.sql.EnumItem
import org.babyfish.jimmer.sql.EnumType

@EnumType(EnumType.Strategy.ORDINAL)
enum class ModelType(
    private val id: Int,
    private val title: String,
) {
    @EnumItem(ordinal = 0)
    OPEN_AI(0, "OpenAI"),

    @EnumItem(ordinal = 1)
    VOLC_ENGINE(1, "VolcEngine"),

    @EnumItem(ordinal = 2)
    ALI(2, "Ali"),

    @EnumItem(ordinal = 3)
    ZHI_PU(3, "ZhiPu"),

    @EnumItem(ordinal = 4)
    AZURE_OPEN_AI(4, "AzureOpenAI"),
}