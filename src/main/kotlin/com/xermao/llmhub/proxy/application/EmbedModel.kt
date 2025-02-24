package com.xermao.llmhub.proxy.application

import com.xermao.llmhub.provider.domain.model.Provider
import com.xermao.llmhub.proxy.domain.model.EmbedRequest
import com.xermao.llmhub.proxy.domain.constant.ProviderMode
import java.net.URI

interface EmbedModel : PreProcessing {
    fun uri(provider: Provider): URI {
        return super.url(provider, ProviderMode.EMBED)
    }

    fun body(embedRequest: EmbedRequest): Any {
        return embedRequest
    }

    fun usage(response: Any)
}
