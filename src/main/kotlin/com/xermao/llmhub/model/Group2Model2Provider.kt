package com.xermao.llmhub.model

data class Group2Model2Provider(
    val group: List<Model2Provider>
)

data class Model2Provider(
    val model: List<Provider>
)

data class Provider(
    val id: String
)
