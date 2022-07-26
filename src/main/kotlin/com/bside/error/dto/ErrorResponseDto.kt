package com.bside.error.dto

import io.swagger.annotations.ApiModelProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ErrorResponseDto(
        val message: String,
        val reason: String? = ""
) {
    companion object {
        fun error(status: HttpStatus, message: String, reason: String? = ""): ResponseEntity<ErrorResponseDto> {
            return ResponseEntity.status(status).body(ErrorResponseDto(message, reason))
        }
    }

}