package com.xermao.llmhub.proxy.domain.constant

import com.xermao.llmhub.provider.domain.constant.ProviderName

object ProviderUrI {
    val chat = mapOf(
        Pair(ProviderName.VOLCENGINE, "/chat/completions"),
        Pair(ProviderName.OPEN_AI, "/chat/completions"),
        Pair(ProviderName.ALI, "/chat/completions"),
        Pair(ProviderName.AZURE_OPENAI, "/chat/completions"),
        Pair(ProviderName.ZHI_PU, "/chat/completions"),
    )
    val audio = mapOf(
        Pair(ProviderName.VOLCENGINE, "/chat/completions"),
        Pair(ProviderName.OPEN_AI, "/chat/completions"),
        Pair(ProviderName.ALI, "/chat/completions"),
        Pair(ProviderName.AZURE_OPENAI, "/chat/completions"),
        Pair(ProviderName.ZHI_PU, "/chat/completions"),
    )
    val embed = mapOf(
        Pair(ProviderName.VOLCENGINE, "/chat/completions"),
        Pair(ProviderName.OPEN_AI, "/chat/completions"),
        Pair(ProviderName.ALI, "/chat/completions"),
        Pair(ProviderName.AZURE_OPENAI, "/chat/completions"),
        Pair(ProviderName.ZHI_PU, "/chat/completions"),
    )
    val image = mapOf(
        Pair(ProviderName.VOLCENGINE, "/chat/completions"),
        Pair(ProviderName.OPEN_AI, "/chat/completions"),
        Pair(ProviderName.ALI, "/chat/completions"),
        Pair(ProviderName.AZURE_OPENAI, "/chat/completions"),
        Pair(ProviderName.ZHI_PU, "/chat/completions"),
    )
}
