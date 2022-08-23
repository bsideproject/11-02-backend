package com.bside.error

import com.bside.common.type.ErrorMessage
import com.bside.error.dto.ErrorResponseDto
import com.bside.error.exception.BusinessException
import com.bside.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger by logger()

    @ExceptionHandler(Exception::class)
    fun handleAllException(e: Exception): ResponseEntity<ErrorResponseDto> {
        logger.error(e.message, e)
        return ErrorResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.INTERNAL_SERVER_ERROR.name)
    }

    @ExceptionHandler(
            BusinessException::class
    )
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponseDto> {
        logger.error(e.message, e)
        return ErrorResponseDto.error(e.getHttpStatus(), e.message!!, e.getReason())
    }

    @ExceptionHandler(
            MethodArgumentNotValidException::class
    )
    fun handleMethodArgumentNotValid(e: Exception): ResponseEntity<ErrorResponseDto> {
        logger.error(e.message, e)

        if (e is MethodArgumentNotValidException) {
            return ErrorResponseDto.error(HttpStatus.BAD_REQUEST, e.fieldError?.defaultMessage ?: "MethodArgumentNotValidException")
        }
        return ErrorResponseDto.error(HttpStatus.BAD_REQUEST, e.message!!)
    }
}

