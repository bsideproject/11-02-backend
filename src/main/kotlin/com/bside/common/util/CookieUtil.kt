package com.bside.common.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import org.springframework.util.SerializationUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CookieUtil() {

    companion object {

        fun getCookie(request: HttpServletRequest, name: String): Cookie? {
            val cookies: Array<Cookie>? = request.cookies
            if (cookies != null && cookies.isNotEmpty()) {
                for (cookie in cookies) {
                    if (name == cookie.name) {
                        return cookie
                    }
                }
            }
            return null
        }

        fun addCookie(response: HttpServletResponse, name: String?, value: String?, maxAge: Int) {
            val cookie = Cookie(name, value)
            cookie.path = "/"
            cookie.isHttpOnly = true
            cookie.maxAge = maxAge
            cookie.domain = "localhost"
            response.addCookie(cookie)
        }

        fun addSecureCookie(
            response: HttpServletResponse,
            cookieMaxAge: Long,
            key: String,
            value: String
        ) {
            val cookie: ResponseCookie = ResponseCookie.from(key, value)
                .path("/")
                .maxAge(cookieMaxAge)
                .domain("localhost")
                .build()
            response.addHeader("Set-Cookie", cookie.toString())
        }

        fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
            val cookies = request.cookies
            if (cookies != null && cookies.isNotEmpty()) {
                for (cookie in cookies) {
                    if (name == cookie.name) {
                        cookie.value = ""
                        cookie.path = "/"
                        cookie.maxAge = 0
                        response.addCookie(cookie)
                    }
                }
            }
        }

        fun serialize(obj: Any?): String? {
            return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj))
        }

        fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
            return cls.cast(
                SerializationUtils.deserialize(
                    Base64.getUrlDecoder().decode(cookie.value)
                )
            )
        }


    }
}