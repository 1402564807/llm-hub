package com.xermao.llmhub.provider.web

import com.xermao.llmhub.common.domain.model.R
import com.xermao.llmhub.provider.application.ProviderAppService
import com.xermao.llmhub.provider.domain.constant.ProviderEnum
import com.xermao.llmhub.provider.domain.model.Provider
import com.xermao.llmhub.provider.domain.model.by
import com.xermao.llmhub.provider.domain.model.dto.ProviderAddInput
import com.xermao.llmhub.provider.domain.model.dto.ProviderUpdateInput
import com.xermao.llmhub.provider.domain.repository.ProviderAggregateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/provider")
class ProviderController(
    private val providerAppService: ProviderAppService,
    private val providerAggregateRepository: ProviderAggregateRepository,
) {

    @GetMapping("/list")
    suspend fun providers(): R<List<Map<String, Any>>> {
        val provides = ProviderEnum.entries
            .map { mapOf(Pair("key", it.key), Pair("label", it.label)) }
            .toList()
        return R.success(provides)
    }

    @PostMapping("/search")
    suspend fun search(@RequestBody providerPageQueryVm: ProviderPageQueryVm): R<Page<Provider>> {
        val providerPage =
            providerAggregateRepository.fetchAggregateWithPageBy(SERVICE_PROVIDER_PAGE, providerPageQueryVm)
        return R.success(providerPage)
    }

    @PostMapping
    suspend fun addProvider(@RequestBody providerAddInput: ProviderAddInput): R<Long> {
        val provider = withContext(Dispatchers.IO) {
            providerAppService.addProvider(providerAddInput)
        }
        return R.success(provider)
    }

    @PutMapping
    suspend fun updateProvider(@RequestBody providerUpdateInput: ProviderUpdateInput): R<Long> {
        val provider = withContext(Dispatchers.IO) {
            providerAppService.updateProvider(providerUpdateInput)
        }
        return R.success(provider)
    }

    @DeleteMapping("/{id}")
    suspend fun deleteProvider(@PathVariable("id") id: Long): R<Boolean> {
        val provider = withContext(Dispatchers.IO) {
            providerAppService.deleteProvider(id)
        }
        return R.success(provider)
    }

    companion object {
        val SERVICE_PROVIDER_PAGE = newFetcher(Provider::class).by {
            allTableFields()
            key(false)
        }
    }
}
