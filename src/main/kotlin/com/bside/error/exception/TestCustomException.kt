package com.bside.error.exception

import org.springframework.http.HttpStatus

class TestCustomException : BusinessException {
    constructor(message: String) : super(HttpStatus.CONFLICT, message) {}
    constructor(message: String, reason: String) : super(HttpStatus.CONFLICT, message, reason) {}
}