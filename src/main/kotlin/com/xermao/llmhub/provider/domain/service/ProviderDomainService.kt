package com.xermao.llmhub.provider.domain.service

import com.xermao.llmhub.provider.ProviderDomainApi
import com.xermao.llmhub.provider.domain.model.*
import com.xermao.llmhub.provider.domain.model.dto.ProviderAddInput
import com.xermao.llmhub.provider.domain.model.dto.ProviderUpdateInput
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.KExpression
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Service

@Service
class ProviderDomainService(private val sqlClient: KSqlClient) : ProviderDomainApi {

    override fun addProvider(providerAddInput: ProviderAddInput): Long {
        val result = sqlClient.insert(providerAddInput)
        return result.modifiedEntity.id
    }

    override fun updateProvider(providerUpdateInput: ProviderUpdateInput): Long {
        val execute = sqlClient.createUpdate(Provider::class) {
            where(table.id.eq(providerUpdateInput.id))
            providerUpdateInput.name?.let { set(table.name, it) }
            providerUpdateInput.key?.let { set(table.key, it) }
            providerUpdateInput.baseUrl?.let { set(table.baseUrl, it) }
            providerUpdateInput.models?.let { set(table.models, it) }
            providerUpdateInput.group?.let { set(table.group, it) }
            providerUpdateInput.modelMap?.let { set(table.modelMap, it) }
            providerUpdateInput.priority?.let { set(table.priority, it) }
            providerUpdateInput.weight?.let { set(table.weight, it) }
            providerUpdateInput.status?.let { set(table.status, it) }
        }.execute()
        return execute.toLong()
    }

    override fun deleteProvider(id: Long): Boolean {
        val result = sqlClient.deleteById(Provider::class, id)
        return result.totalAffectedRowCount != 0
    }
}
