package com.retro.retromall.product.dto

import javax.validation.constraints.NotBlank

data class CreateProductRequest(

    @field: NotBlank(message = "제목이 없습니다. 제목을 입력해주세요.")
    val title: String,

    @field: NotBlank(message = "내용이 없습니다. 내용을 입력해주세요.")
    val content: String?,

    val amount: Int,

    @field: NotBlank(message = "카테고리가 설정되어 있지 않습니다. 카테고리를 설정해주세요.")
    val category: String,

    val thumbnail: String?,

    val images: Set<String>,

    val hashTags: Set<String>
)
