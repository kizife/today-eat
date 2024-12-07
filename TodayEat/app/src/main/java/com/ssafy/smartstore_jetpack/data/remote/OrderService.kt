package com.ssafy.smartstore_jetpack.data.remote

import com.ssafy.smartstore_jetpack.data.model.dto.OrderTodayeat
import com.ssafy.smartstore_jetpack.data.model.response.OrderDetailCombi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    // order 객체를 저장하고 추가된 Order의 id를 반환한다.
    @POST("rest/order")
    suspend fun makeOrder(@Body body: OrderTodayeat): Int

    // {orderId}에 해당하는 주문의 상세 내역을 반환한다.
    @GET("rest/order/{orderId}")
    suspend fun getOrderDetail(@Path("orderId") orderId: Int): List<OrderDetailCombi>

    // {id}에 해당하는 사용자의 최근 1개월간 주문 내역을 반환한다.
    // 반환 정보는 1차 주문번호 내림차순, 2차 주문 상세 내림차순으로 정렬된다.
    @GET("rest/order/byUser")
    suspend fun getLastMonthOrder(@Query("id") id: String): List<OrderTodayeat>

    @GET("rest/order/byUserIn6Months")
    suspend fun getLast6MonthOrder(@Query("id") id: String): List<OrderDetailCombi>

    @PUT("rest/order/updateStatus/{orderId}")
    suspend fun updateOrderStatus(@Path("orderId") orderId: Int): Response<Int>
}