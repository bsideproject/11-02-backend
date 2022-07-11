package com.bside.service

import com.bside.dto.MemberResponseDto
import com.bside.repository.MemberReposiroty
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * name : MemberServiceImpl
 * author : jisun.noh
 */
@Service
@Transactional
class MemberService(
    val userRepository: MemberReposiroty
) {
    fun getMemberInfo(email: String): MemberResponseDto {
        return MemberResponseDto().apply {
            this.email = userRepository.findByEmail(email).email
        }
    }
}