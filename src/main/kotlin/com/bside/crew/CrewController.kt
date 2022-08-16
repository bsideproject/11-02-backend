package com.bside.crew

import com.bside.common.dto.ApiResponseDto

import com.bside.crew.dto.request.CrewCreateRequest
import com.bside.crew.dto.response.CrewResponse

import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/crew")
class CrewController(val crewService: CrewService) {

    @ApiOperation(value = "CREW 생성 API")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "201", description = "크루 생성 성공", content = [Content(mediaType = "application/json", schema = Schema(implementation = CrewResponse::class))])
            ]
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping()
    fun save(@ApiIgnore @AuthenticationPrincipal principal: UserDetails, @Validated @RequestBody crewCreateRequest: CrewCreateRequest): ResponseEntity<CrewResponse> {
        val userId = principal.username
        val response = crewService.save(userId, crewCreateRequest)
        return ApiResponseDto.created(response)
    }
}