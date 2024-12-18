package com.ssafy.smartstore_jetpack.data.remote

import com.ssafy.smartstore_jetpack.data.model.dto.User
import com.ssafy.smartstore_jetpack.data.model.dto.UserTodayeat
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserService {
    // 사용자 정보를 추가한다.
    @POST("rest/user")
    suspend fun insert(@Body body: UserTodayeat): Boolean
    
    // 사용자의 정보와 함께 사용자의 주문 내역, 사용자 등급 정보를 반환한다.
    @GET("rest/user/info")
    suspend fun getUserInfo(@Query("id") id:String): UserTodayeat

    // request parameter로 전달된 id가 이미 사용중인지 반환한다.
    @GET("rest/user/isUsed")
    suspend fun isUsedId(@Query("id") id: String): Boolean

    // 로그인 처리 후 성공적으로 로그인 되었다면 loginId라는 쿠키를 내려준다.
    @POST("rest/user/login")
    suspend fun login(@Body body: User): UserTodayeat

    //회원 주소 변경
    @PUT("rest/user/update")
    suspend fun updateUserInfo(@Query("id") id:String,@Query("address") address: String): UserTodayeat
}