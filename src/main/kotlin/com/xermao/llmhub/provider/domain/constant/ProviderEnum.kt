package com.xermao.llmhub.provider.domain.constant

import com.fasterxml.jackson.annotation.JsonValue
import org.babyfish.jimmer.sql.EnumItem
import org.babyfish.jimmer.sql.EnumType

@EnumType(EnumType.Strategy.ORDINAL)
enum class ProviderEnum(
    @JsonValue
    val key: Int,
    val value: String,
    val label: String,
) {
    @EnumItem(ordinal = 0)
    OPEN_AI(0, ProviderName.OPEN_AI, "OpenAI"),

    @EnumItem(ordinal = 1)
    VOLCENGINE(1, ProviderName.VOLCENGINE, "火山引擎"),

    @EnumItem(ordinal = 2)
    ALI(2, ProviderName.ALI, "阿里巴巴"),

    @EnumItem(ordinal = 3)
    ZHI_PU(3, ProviderName.ZHI_PU, "智普清言"),

    @EnumItem(ordinal = 4)
    AZURE_OPEN_AI(4, ProviderName.AZURE_OPENAI, "AzureOpenAI"), ;


}
