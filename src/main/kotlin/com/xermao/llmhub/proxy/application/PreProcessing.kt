package com.xermao.llmhub.proxy.application

import com.xermao.llmhub.provider.domain.model.Provider
import com.xermao.llmhub.proxy.domain.constant.ProviderMode
import com.xermao.llmhub.proxy.domain.constant.ProviderUrI
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.net.URI
import java.util.function.Consumer

interface PreProcessing {

    fun url(provider: Provider, mode: ProviderMode): URI {

        val baseUrl = provider.baseUrl.dropLastWhile { it == '/' }
        val uri = when (mode) {
            ProviderMode.CHAT -> baseUrl.plus(ProviderUrI.chat[baseUrl])
            ProviderMode.AUDIO -> baseUrl.plus(ProviderUrI.audio[baseUrl])
            ProviderMode.EMBED -> baseUrl.plus(ProviderUrI.embed[baseUrl])
            ProviderMode.IMAGE -> baseUrl.plus(ProviderUrI.image[baseUrl])
        }
        return URI.create(uri)
    }


    fun headers(provider: Provider): Consumer<HttpHeaders> {
        return Consumer { httpHeaders ->
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer ${provider.key}")
        }
    }
}
