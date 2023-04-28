package com.retro.retromall.product.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ProductUpdateRequest (
    @NotBlank(message = "제목은 빈 값이 될 수 없습니다.")
    @Size(message = "제목의 최대 길이는 255입니다.", max = 255)
    val title: String?,

    @Size(message = "내용의 최대 길이는 1000입니다.", max = 1000)
    val content: String?,

    val amount: Int,

    @NotBlank(message = "카테고리가 설정되어 있지 않습니다. 카테고리를 설정해주세요.")
    val category: String,
    val images: Set<String>,
    val hashTags: Set<String>
)