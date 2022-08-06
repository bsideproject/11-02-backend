package com.bside.common.type


enum class ErrorMessage(val reason: String = "") {
    INTERNAL_SERVER_ERROR,
    USER_ALREADY_EXIST("이미 가입되어 있는 유저입니다."),
    UPLOAD_FILE_NOT_EXIST("업로드 파일이 존재 하지 않습니다."),
    TEST_ERROR_MESSAGE("reason why error occured::optional")
}
