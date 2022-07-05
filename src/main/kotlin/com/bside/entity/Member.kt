package com.bside.entity

import com.bside.entity.type.Authority
import javax.persistence.*


/**
 * name : Member
 * author : jisun.noh
 */
@Entity
@Table(name = "MEMBER")
class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
    var email: String = ""
    var password: String = ""

    @Enumerated(EnumType.STRING)
    var authority: Authority = Authority.ROLE_USER
}