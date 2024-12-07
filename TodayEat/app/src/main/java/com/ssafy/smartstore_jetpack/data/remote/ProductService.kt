package com.ssafy.smartstore_jetpack.data.remote

import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.data.model.response.ProductWithCommentResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    // 전체 상품의 목록을 반환한다
    @GET("rest/product")
    suspend fun getProductList(): List<ProductTodayeat>

    // {productId}에 해당하는 상품의 정보를 comment와 함께 반환한다.
    // comment 조회시 사용
    @GET("rest/product/{productId}")
    suspend fun getProductWithComments(@Path("productId") productId: Int): ProductWithCommentResponse

    @GET("rest/product/selected/{productId}")
    suspend fun getProduct(@Path("productId") productId: Int): ProductTodayeat
}