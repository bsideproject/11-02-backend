package com.bside.dto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id


/**
 * name : RefreshToken
 * author : jisun.noh
 */
@Entity
class RefreshToken {
    @Id
    @Column(name = "rt_key")
    var key: String = ""

    @Column(name = "rt_value")
    var value: String = ""
}