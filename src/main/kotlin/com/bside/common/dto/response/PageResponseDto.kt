package com.bside.common.dto.response

data class PageDto<T>(
        val entities: List<T>,
        val lastId: String? = null
)
