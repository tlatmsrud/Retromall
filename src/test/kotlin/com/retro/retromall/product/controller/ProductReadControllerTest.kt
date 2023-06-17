package com.retro.retromall.product.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.ApiDocumentUtils
import com.retro.aop.JwtTokenProvider
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.service.ProductReadService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.SliceImpl
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import java.time.LocalDateTime

@WebMvcTest(ProductReadController::class)
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension::class)
class ProductReadControllerTest{

    private lateinit var objectMapper : ObjectMapper

    @MockBean
    lateinit var productReadService : ProductReadService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private lateinit var mockMvc: MockMvc

    private fun authenticationAttributesBuild() : AuthenticationAttributes {
        return AuthenticationAttributes(
            id = 1000L,
            roles = "USER",
            permissions = "CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT"
        )
    }

    private fun generateProductResponse() : ProductResponse{
        return ProductResponse(true, 1, "제품명", "내용", 10000, "author", "PS2",
            0, false, emptySet(), emptySet(), "서울특별시 종로구", LocalDateTime.now(), LocalDateTime.now(),
        )
    }

    private fun generateProductListResponse() : ProductListResponse{
        val content = listOf(
            ProductListResponse.Data( 2, "테스터", "수정제품", 100000, 0, "썸네일 이미지 URL",
                "서울특별시", LocalDateTime.now(), LocalDateTime.now()),
            ProductListResponse.Data( 1, "테스터1", "수정제품1", 10000, 0, "썸네일 이미지 URL",
                "서울특별시", LocalDateTime.now(), LocalDateTime.now())
        )

        return ProductListResponse(data = SliceImpl(content, generatePageable(), true))

    }

    private fun generatePageable() : Pageable {
        return PageRequest.of(0,20, Sort.Direction.DESC, "createdAt")
    }

    private fun headerBuild(): HttpHeaders {
        val header = HttpHeaders()
        header.set("Authorization","Bearer TestToken")
        return header
    }

    @BeforeEach
    fun setUp(webApplicationContext : WebApplicationContext,
              restDocumentationContextProvider : RestDocumentationContextProvider
    ){
        this.objectMapper = ObjectMapper()

        var authenticationAttributes = authenticationAttributesBuild()

        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider
                )
            )
            .addFilter<DefaultMockMvcBuilder?>(filter)
            .build()

        given(productReadService.getProduct(authenticationAttributes,1)).willReturn(
           generateProductResponse()
        )

        given(productReadService.getProduct(authenticationAttributes,100)).willThrow(
            IllegalArgumentException("요청하신 결과가 없습니다.")
        )

        given(productReadService.getProductList("PS2", generatePageable())).willReturn(
            generateProductListResponse()
        )

    }

    @Test
    @DisplayName("유효 ID에 대한 상품조회")
    fun readProductByValidRequest(){
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/products/{id}",1)
                .headers(headerBuild())
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("제품명")))
            .andDo(
                document("readProductByValidId"
                    ,ApiDocumentUtils.getDocumentRequest()
                    ,ApiDocumentUtils.getDocumentResponse()
                    ,pathParameters(
                        parameterWithName("id").description("제품 ID")
                    )
                    , responseFields(
                        fieldWithPath("isAuthor").type(JsonFieldType.BOOLEAN).description("자신이 작성자인지 여부"),
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("제품ID"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("가격"),
                        fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리 ID"),
                        fieldWithPath("likes").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                        fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN).description("자신의 좋아요 여부"),
                        fieldWithPath("hashTags").type(JsonFieldType.ARRAY).description("해시태크 리스트"),
                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("이미지 리스트"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성날짜"),
                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정날짜"),
                        fieldWithPath("author").type(JsonFieldType.STRING).description("작성자 ID"),
                    )
                )
            )

        Mockito.verify(productReadService).getProduct(
            any(AuthenticationAttributes::class.java), eq(1L))
    }

    @Test
    @DisplayName("유효하지 않는 ID에 대한 상품조회")
    fun readProductByInvalidRequest(){
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/products/{id}",100)
                .headers(headerBuild())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{\"message\":\"요청하신 결과가 없습니다.\"}"))
            .andDo(
                document("readProductByInvalidId"
                    ,pathParameters(
                        parameterWithName("id").description("제품 ID")
                    )
                )
            )

        Mockito.verify(productReadService).getProduct(
            any(AuthenticationAttributes::class.java), eq(100L))
    }

    @Test
    @DisplayName("유효 카테고리에 대한 제품 리스트 조회")
    fun getProductListByValidCategory(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/products")
                .headers(headerBuild())
                .contentType(MediaType.APPLICATION_JSON)
                .param("category", "PS2")
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("테스터")))
            .andExpect(content().string(containsString("pageable")))
            .andExpect(content().string(containsString("content")))

            .andDo(
                document("getProductListByValidCategory"
                    ,ApiDocumentUtils.getDocumentRequest()
                    ,ApiDocumentUtils.getDocumentResponse()
                    , requestParameters(
                        parameterWithName("category").description("카테고리 ID")
                    )
                    , responseFields(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("상품정보 리스트"),
                        fieldWithPath("data.content[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                        fieldWithPath("data.content[].author").type(JsonFieldType.STRING).description("작성자 ID"),
                        fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.content[].amount").type(JsonFieldType.NUMBER).description("가격"),
                        fieldWithPath("data.content[].likes").type(JsonFieldType.NUMBER).description("좋아요 수"),
                        fieldWithPath("data.content[].thumbnail").type(JsonFieldType.STRING).description("썸네일 이미지 URL"),
                        fieldWithPath("data.content[].addr").type(JsonFieldType.STRING).description("주소"),
                        fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING).description("작성일자"),
                        fieldWithPath("data.content[].modifiedAt").type(JsonFieldType.STRING).description("수정일자"),
                        fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이지 정보"),
                        fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("empty"),
                        fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                        fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("비정렬 여부"),
                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지당 사이즈"),
                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("paged"),
                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("unpaged"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지당 사이즈"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("페이지 번호"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("sortInfo"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("empty"),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("sorted"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("unsorted"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("조회된 데이터 수"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("empty")
                    )
                )
            )
    }


    private fun <T> any(type: Class<T>): T = Mockito.any(type)


}