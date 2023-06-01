package com.retro.retromall.authorization.enums

enum class Permission(
    private val korName: String
) {
    CREATE_PRODUCT("상품 생성"),
    UPDATE_PRODUCT("상품 수정"),
    DELETE_PRODUCT("상품 삭제");

    fun getKorName(): String {
        return korName
    }
}