package com.retro.retromall.service

import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.domain.CategoryRepository
import com.retro.retromall.product.controller.dto.AddProductRequest
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductRepository
import com.retro.retromall.product.service.ProductService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProductServiceTest(
    @Mock
    private val productRepository: ProductRepository,

    @Mock
    private val categoryRepository: CategoryRepository
) {
    @Test
    fun addProduct() {
        val category = Category(category = "PC", korValue = "데스크탑")
        val product = Product(content = "", amount = 10000, category = category)
//        given(categoryRepository.findById(anyString())).willReturn(Optional.ofNullable(any(Category::class.java)))
//        given(productRepository.save(any(Product::class.java))).willReturn(any(Product::class.java))

        given(categoryRepository.findById(anyString())).willReturn(Optional.of(category))
        given(productRepository.save(any(Product::class.java))).willReturn(product)


        val service = ProductService(productRepository, categoryRepository)
        service.addProduct(AddProductRequest("", 1000, "PC"))

        verify(categoryRepository).findById(anyString())
        verify(productRepository).save(any(Product::class.java))
    }
}