package com.retro.retromall.product.service

import com.retro.retromall.autocomplete.service.AutocompleteService
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.dto.ProductListResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StopWatch

@Service
@Transactional(readOnly = true)
class ProductReadService(
    private val productRepository: ProductRepository
    , private val autocompleteService : AutocompleteService
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ProductReadService::class.java)
    }

    fun getProduct(authenticationAttributes: AuthenticationAttributes, productId: Long): ProductResponse {
        val stopWatch = StopWatch()
        stopWatch.start("복수쿼리 조회 측정")
        val result = productRepository.selectProduct(productId, authenticationAttributes.id)
        stopWatch.stop()
        logger.info("{}", stopWatch.prettyPrint())
        return result
    }

    fun getProductList(category: String?, pageable: Pageable): ProductListResponse? {
        return productRepository.selectProductList(category, pageable)
    }

    fun searchProductList(searchWord: String, pageable: Pageable): ProductListResponse? {
        autocompleteService.addAutocomplete(searchWord)
        return productRepository.selectProductListBySearchWord(searchWord,pageable)
    }
}