package com.bside.error.exception

import org.springframework.http.HttpStatus

class NotExistException : BusinessException {
    constructor(message: String) : super(HttpStatus.NOT_FOUND, message) {}
    constructor(message: String, reason: String) : super(HttpStatus.NOT_FOUND, message, reason) {}
}