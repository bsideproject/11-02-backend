package com.bside.service.impl

import com.bside.dto.MemberResponseDto
import com.bside.repository.MemberReposiroty
import com.bside.service.MemberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * name : MemberServiceImpl
 * author : jisun.noh
 */
@Service
@Transactional
class MemberServiceImpl(
    val userRepository: MemberReposiroty
) : MemberService {
    override fun getMemberInfo(email: String): MemberResponseDto {
        return MemberResponseDto().apply {
            this.email = userRepository.findByEmail(email).email
        }
    }
}