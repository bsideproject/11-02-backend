package com.bside.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * name : AdminController
 * author : jisun.noh
 */
@RestController
@RequestMapping("/admin")
class AdminController {

    @GetMapping("/get-info")
    fun getAdmin(): String {
        return "admin"
    }
}