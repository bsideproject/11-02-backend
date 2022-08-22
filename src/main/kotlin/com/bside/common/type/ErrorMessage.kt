package com.bside.common.type


enum class ErrorMessage(val reason: String = "") {
    INTERNAL_SERVER_ERROR,
    USER_ALREADY_EXIST("이미 가입되어 있는 유저입니다."),
    UPLOAD_FILE_NOT_EXIST("업로드 파일이 존재 하지 않습니다."),
    TEST_ERROR_MESSAGE("reason why error occured::optional"),
    MEMBER_NICKNAME_ALREADY_EXIST("닉네임이 이미 존재하고 있습니다."),
    MEMBER_NOT_FOUND("멤버가 존재하지 않습니다.")
}
