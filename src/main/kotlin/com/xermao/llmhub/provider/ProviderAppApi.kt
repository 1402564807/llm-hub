package com.xermao.llmhub.provider

import com.xermao.llmhub.provider.domain.model.dto.ProviderAddInput
import com.xermao.llmhub.provider.domain.model.dto.ProviderUpdateInput

interface ProviderAppApi {
    fun addProvider(providerAddInput: ProviderAddInput): Long
    fun updateProvider(providerUpdateInput: ProviderUpdateInput): Long
    fun deleteProvider(id: Long): Boolean
}
