package com.ssafy.smartstore_jetpack.data.remote

import com.ssafy.smartstore_jetpack.data.model.dto.CartOnly
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CartService {

    // 특정 사용자에 해당하는 쇼핑 카트 아이템 조회
    @GET("rest/shoppingCart/user/{userId}")
    suspend fun getCartItemsByUser(@Path("userId") userId: String): List<CartOnly>

    // 특정 도시락 아이디에 해당하는 쇼핑 카트 아이템 조회
    @GET("rest/shoppingCart/dosirack/{dosirackId}")
    suspend fun getCartItemsByDosirock(@Path("dosirackId") dosirackId: Int): List<CartOnly>

    // 쇼핑 카트에 아이템 추가
    @POST("rest/shoppingCart")
    suspend fun addCartItem(@Body shoppingCart: CartOnly)

    // 쇼핑 카트 아이템 수정
    @PUT("rest/shoppingCart/{cartId}")
    suspend fun updateCartItem(@Path("cartId") cartId: Int, @Body shoppingCart: CartOnly)

    // 쇼핑 카트 아이템 삭제
    @DELETE("rest/shoppingCart/{cartId}")
    suspend fun removeCartItem(@Path("cartId") cartId: Int)

    // 모든 쇼핑 카트 아이템 조회
    @GET("rest/shoppingCart")
    suspend fun getAllCartItems(): List<CartOnly>
}
