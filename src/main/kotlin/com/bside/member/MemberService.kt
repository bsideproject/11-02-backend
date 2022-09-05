package com.bside.member

import com.bside.common.type.ErrorMessage
import com.bside.error.exception.AlreadyExistException
import com.bside.error.exception.NotExistException
import com.bside.member.dto.request.MemberModifyRequest
import com.bside.member.dto.response.MemberModifyResponse
import com.bside.member.dto.response.MemberResponse
import com.bside.member.entity.Member
import com.bside.member.repository.MemberRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional



@Service
@Transactional
class MemberService(
    val memberRepository: MemberRepository
) {
    fun getMemberInfo(userId: String): MemberResponse {
        val member: Member = memberRepository.findByEmail(userId) ?: throw NotExistException(
            ErrorMessage.MEMBER_NOT_FOUND.name, ErrorMessage.MEMBER_NOT_FOUND.reason
        )
        return MemberResponse.fromEntity(member)
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

    fun checkNickname(nickname: String): String {
        return if (memberRepository.existsByNickname(nickname)){
            "duplicated"
        } else {
            "ok"
        }
    }
}