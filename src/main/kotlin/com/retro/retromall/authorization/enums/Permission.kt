package com.retro.retromall.authorization.enums

enum class Permission(
    private val korName: String
) {
    READ_PRODUCT("상품 조회"),
    CREATE_PRODUCT("상품 생성"),
    UPDATE_PRODUCT("상품 수정"),
    DELETE_PRODUCT("상품 삭제");

    companion object {
        fun fromValue(value: String): Permission? {
            return values().find { it.name == value }
        }
    }

    fun getMessage(): String {
        return korName
    }
}