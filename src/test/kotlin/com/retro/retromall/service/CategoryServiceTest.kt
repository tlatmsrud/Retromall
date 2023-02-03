package com.retro.retromall.service

import com.retro.retromall.category.domain.CategoryRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CategoryServiceTest(
    @Mock
    private val categoryRepository: CategoryRepository
) {

}