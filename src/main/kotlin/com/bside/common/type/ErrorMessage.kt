package com.bside.common.type


enum class ErrorMessage(val reason: String = "") {
    INTERNAL_SERVER_ERROR,
    USER_ALREADY_EXIST("이미 가입되어 있는 유저입니다."),
    TEST_ERROR_Message_("reason why error occured::optional")
}
