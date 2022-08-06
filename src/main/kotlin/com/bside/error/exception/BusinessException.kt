package com.bside.error.exception

import org.springframework.http.HttpStatus

abstract class BusinessException : RuntimeException {
    private var httpStatus: HttpStatus
    private var reason: String = ""

    constructor(httpStatus: HttpStatus, message: String, reason: String = "") : super(message) {
        this.httpStatus = httpStatus
        this.reason = reason
    }

    fun getHttpStatus() = this.httpStatus
    fun getReason() = this.reason
}