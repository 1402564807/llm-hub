package com.xermao.llmhub.provider.domain.repository

import com.xermao.llmhub.provider.domain.model.Provider
import com.xermao.llmhub.provider.web.ProviderPageQueryVm
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.fetcher.Fetcher

interface ProviderAggregateRepository : KRepository<Provider, Long> {

    fun fetchAggregateWithPageBy(
        fetcher: Fetcher<Provider>,
        providerPageQueryVm: ProviderPageQueryVm
    ): Page<Provider> {
        return sql.createQuery(Provider::class) {

            select(table.fetch(fetcher))
        }.fetchPage(providerPageQueryVm.pageIndex, providerPageQueryVm.pageSize)
    }
}
