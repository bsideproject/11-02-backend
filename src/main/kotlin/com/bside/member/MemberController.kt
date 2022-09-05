package com.bside.member

import com.bside.common.dto.response.ApiResponseDto
import com.bside.member.dto.request.MemberModifyRequest
import com.bside.member.dto.response.MemberModifyResponse
import com.bside.member.dto.response.MemberResponse
import com.bside.util.logger
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/member")
class MemberController(
    val memberService: MemberService
) {
    private val logger by logger()


    @ApiOperation(value = "Member 조회 API")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "멤버 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = MemberResponse::class)
                )]
            )
        ]
    )
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    fun getEmail(@ApiIgnore @AuthenticationPrincipal principal: UserDetails): ResponseEntity<MemberResponse> {
        val userId = principal.username
        val response = memberService.getMemberInfo(userId)
        return ApiResponseDto.ok(response)
    }

    @ApiOperation(value = "Member 닉네임 체크 API")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "닉네임 체크",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            )
        ]
    )
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/nickname-check")
    fun checkNickname(@RequestParam nickname: String): ResponseEntity<String> {
        val response = memberService.checkNickname(nickname)
        return ApiResponseDto.ok(response)
    }


    @ApiOperation(value = "Member 수정 API")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "멤버 수정 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = MemberModifyResponse::class)
                )]
            )
        ]
    )
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping
    fun modifyMember(
        @RequestBody memberModifyRequest: MemberModifyRequest,
        @ApiIgnore @AuthenticationPrincipal principal: UserDetails
    ): ResponseEntity<MemberModifyResponse> {
        val userId = principal.username
        val response = memberService.modify(userId, memberModifyRequest)
        return ApiResponseDto.ok(response)
    }
}