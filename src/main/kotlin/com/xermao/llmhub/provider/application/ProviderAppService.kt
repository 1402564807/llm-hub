package com.xermao.llmhub.provider.application

import com.xermao.llmhub.provider.ProviderAppApi
import com.xermao.llmhub.provider.ProviderDomainApi
import com.xermao.llmhub.provider.domain.model.dto.ProviderAddInput
import com.xermao.llmhub.provider.domain.model.dto.ProviderUpdateInput
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProviderAppService(
    private val providerDomainApi: ProviderDomainApi,
) : ProviderAppApi {

    @Transactional(rollbackFor = [Throwable::class])
    override fun addProvider(providerAddInput: ProviderAddInput): Long {
        return providerDomainApi.addProvider(providerAddInput)
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun updateProvider(providerUpdateInput: ProviderUpdateInput): Long {
        return providerDomainApi.updateProvider(providerUpdateInput)
    }

}
