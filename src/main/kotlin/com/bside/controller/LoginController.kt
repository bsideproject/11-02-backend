package com.bside.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/login")
class LoginController {
    @GetMapping("")
    fun loginPage(): String {
        return "index.html"
    }

    @GetMapping("/redirect")
    @ResponseBody
    fun loginOk(): String {
        return "login_ok"
    }


}