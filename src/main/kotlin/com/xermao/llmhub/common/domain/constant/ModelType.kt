package com.xermao.llmhub.common.domain.constant

import org.babyfish.jimmer.sql.EnumItem
import org.babyfish.jimmer.sql.EnumType

@EnumType(EnumType.Strategy.ORDINAL)
enum class ModelType(
    private val id: Int,
    private val title: String,
) {
    @EnumItem(ordinal = 0)
    OPEN_AI(0, "open_ai"),

    @EnumItem(ordinal = 1)
    VOLCENGINE(1, "volcengine"),

    @EnumItem(ordinal = 2)
    ALI(2, "ali"),

    @EnumItem(ordinal = 3)
    ZHI_PU(3, "zhi_pu"),

    @EnumItem(ordinal = 4)
    AZURE_OPEN_AI(4, "azure_open_ai"),;

    fun getTitle(): String {
        return this.title
    }
}