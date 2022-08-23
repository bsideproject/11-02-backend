package com.bside.common.type

import com.bside.error.exception.NotExistException


enum class ErrorMessage(val reason: String = "") {
    INTERNAL_SERVER_ERROR,
    USER_ALREADY_EXIST("이미 가입되어 있는 유저입니다."),
    UPLOAD_FILE_NOT_EXIST("업로드 파일이 존재 하지 않습니다."),
    TEST_ERROR_MESSAGE("reason why error occured::optional"),

    // member
    MEMBER_NICKNAME_ALREADY_EXIST("닉네임이 이미 존재하고 있습니다."),
    MEMBER_NOT_FOUND("멤버가 존재하지 않습니다."),

    // crew
    CREW_NAME_ALREADY_EXIST("이미 존재하는 크루 이름입니다."),
    CREW_NOT_EXIST("존재 하지 않는 크루 입니다."),
    ALREADY_JOINED_USER("이미 크루에 가입된 유저 입니다."),
    ALREADY_FULL_CAPACITY_CREW("이미 정원 모집이 마감된 크루입니다.")
}
