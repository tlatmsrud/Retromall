package com.retro.common.annotation


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class MemberAuthentication(
    val required: Boolean = true
)
