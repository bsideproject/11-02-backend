package com.bside.common.dto.request

import org.springframework.data.domain.Sort

data class CursorPageable(
        val lastId: String?,
        val size: Int = 10,
        val direction: Sort.Direction = Sort.Direction.DESC
)
