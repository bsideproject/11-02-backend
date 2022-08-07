package com.bside.member

import com.bside.member.dto.response.MemberResponseDto
import com.bside.repository.MemberRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * name : MemberServiceImpl
 * author : jisun.noh
 */
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
}