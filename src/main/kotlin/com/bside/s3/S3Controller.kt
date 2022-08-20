package com.bside.s3

import com.bside.common.dto.response.ApiResponseDto

import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/s3")
class S3Controller(val s3UploderService: NcpS3UploaderService) {

    @PostMapping("/upload/files")
    @ApiOperation(value = "파일 업로드 API")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "파일 업로드 성공(업로드 실패한 파일 url 은 null 리턴 / ex: ['https://...'(성공), null(실패)])")
            ]
    )
    fun upload(@RequestPart(value = "files") multipartFiles: List<MultipartFile>): ResponseEntity<List<String?>> {
        val response = s3UploderService.uploadFiles(multipartFiles);
        return ApiResponseDto.ok(response)
    }
}
