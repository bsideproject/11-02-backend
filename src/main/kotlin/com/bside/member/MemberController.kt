package com.bside.member

import com.bside.common.dto.ApiResponseDto
import com.bside.member.dto.request.MemberModifyRequest
import com.bside.member.dto.response.MemberModifyResponse
import com.bside.member.dto.response.MemberResponseDto
import com.bside.member.entity.Member
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

    // 조회 api 추후 변경 예정
    @GetMapping("email")
    fun getEmail(@RequestParam email: String): MemberResponseDto {
        return memberService.getMemberInfo(email)
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