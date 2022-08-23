package com.bside.crew

import com.bside.common.dto.request.CursorPageable
import com.bside.common.dto.response.ApiResponseDto
import com.bside.common.dto.response.PageDto
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
        val memberId = principal.username
        val response = crewService.save(memberId, crewCreateRequest)
        return ApiResponseDto.created(response)
    }

    @ApiOperation(value = "CREW 조회 API")
    @GetMapping()
    fun findAll(cursorPageable: CursorPageable): ResponseEntity<PageDto<CrewResponse>> {
        val response = crewService.findAll(cursorPageable)
        return ApiResponseDto.ok(response)
    }

    @ApiOperation(value = "나의 CREW 조회 API")
    @GetMapping("/my")
    fun findAllByMemberId(@ApiIgnore @AuthenticationPrincipal principal: UserDetails): ResponseEntity<List<CrewResponse>> {
        val memberId = principal.username
        val response = crewService.findAllByMemberId(memberId)
        return ApiResponseDto.ok(response)
    }

    @ApiOperation(value = "CREW 가입하기")
    @PatchMapping("/{crewId}/join")
    fun join(@ApiIgnore @AuthenticationPrincipal principal: UserDetails, @PathVariable crewId: String): ResponseEntity<CrewResponse> {
        val memberId = principal.username
        val response = crewService.join(crewId, memberId)
        return ApiResponseDto.ok(response)
    }
}