package com.bside.member

import com.bside.common.type.ErrorMessage
import com.bside.error.exception.AlreadyExistException
import com.bside.error.exception.NotExistException
import com.bside.member.dto.request.MemberModifyRequest
import com.bside.member.dto.response.MemberModifyResponse
import com.bside.member.dto.response.MemberResponseDto
import com.bside.member.entity.Member
import com.bside.repository.MemberRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InvalidObjectException


@Service
@Transactional
class MemberService(
    val memberRepository: MemberRepository
) {
    fun getMemberInfo(email: String): MemberResponseDto {
        return MemberResponseDto().apply {
            this.email = memberRepository.findByEmail(email)!!.email
        }
    }

    fun modify(userId: String, memberModifyRequest: MemberModifyRequest): MemberModifyResponse {
        if (memberRepository.existsByNickname(memberModifyRequest.nickname)){
            throw AlreadyExistException(
                ErrorMessage.MEMBER_NICKNAME_ALREADY_EXIST.name, ErrorMessage.MEMBER_NICKNAME_ALREADY_EXIST.reason
            )
        }

        val member: Member = memberRepository.findByEmail(userId) ?: throw NotExistException(
            ErrorMessage.MEMBER_NOT_FOUND.name, ErrorMessage.MEMBER_NOT_FOUND.reason
        )
        member.nickname = memberModifyRequest.nickname
        member.profile = memberModifyRequest.profile
        memberRepository.save(member)

        return MemberModifyResponse(member)
    }
}