package com.xermao.llmhub.user.web

data class UserPageQueryVm(
    val pageIndex: Int,
    val pageSize: Int,
    val username: String,
    val enable: Boolean
)
