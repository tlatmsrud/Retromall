import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.service.ProductLikeService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

class ProductLikeServiceTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var productLikeService: ProductLikeService
    private lateinit var product: ProductEntity
    private lateinit var authenticationAttributes: AuthenticationAttributes

    @BeforeEach
    fun setUp() {
        productRepository = mock(ProductRepository::class.java)
        productLikeService = ProductLikeService(productRepository)
        product = ProductEntity(
            title = "Test Title",
            content = "Test Content",
            amount = 1000,
            authorId = 1L,
            category = "Xbox",
            addressId = 1L
        )
        authenticationAttributes = AuthenticationAttributes(1L, "USER", "MODIFY_PRODUCT")

        `when`(productRepository.findById(anyLong())).thenReturn(Optional.of(product))
    }

    @Test
    fun `addProductLike should increase likes when product is not liked by user`() {
        val initialLikes = product.likes

        productLikeService.addProductLike(authenticationAttributes, 1L)

        assertEquals(initialLikes + 1, product.likes)
        assertTrue(product.productLikeEntities.any { it.memberId == authenticationAttributes.id && it.isLiked })
    }

    @Test
    fun `removeProductLike should decrease likes when product is liked by user`() {
        product.addLikes(authenticationAttributes.id!!)
        val initialLikes = product.likes

        productLikeService.removeProductLike(authenticationAttributes, 1L)

        assertEquals(initialLikes - 1, product.likes)
        assertTrue(product.productLikeEntities.all { it.memberId != authenticationAttributes.id || !it.isLiked })
    }
}
