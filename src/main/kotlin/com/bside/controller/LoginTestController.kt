package com.bside.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/login")
class LoginTestController {
    @GetMapping("")
    fun loginPage(): String {
        return "index.html"
    }

    @GetMapping("/redirect")
    @ResponseBody
    fun loginOk(@RequestParam token: String?, @RequestParam error: String?): String {
        return "login_ok"
    }


}