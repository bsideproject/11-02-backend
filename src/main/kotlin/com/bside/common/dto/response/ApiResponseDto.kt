package com.bside.common.dto.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ApiResponseDto {
    companion object {
        fun <T> ok(data: T): ResponseEntity<T> = ResponseEntity.ok().body(data)
        fun <T> created(data: T): ResponseEntity<T> = ResponseEntity.status(HttpStatus.CREATED).body(data)
    }
}


