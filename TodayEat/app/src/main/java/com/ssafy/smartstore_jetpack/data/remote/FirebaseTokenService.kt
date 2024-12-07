package com.ssafy.smartstoredb.service
import retrofit2.Call
import retrofit2.http.*

data class TokenRequest(val token: String)  // Token 정보를 담을 데이터 클래스

data class BroadcastRequest(val title: String, val body: String)  // Broadcast 내용을 담을 데이터 클래스

data class NotificationRequest(
    val title: String,
    val body: String,
    val targetToken: String // FCM Token
)

interface FirebaseTokenService {
    // Token정보 서버로 전송
    @POST("token")
    fun uploadToken(@Body tokenRequest: TokenRequest): Call<String>

    // broadcast를 서버로 전송
    @POST("broadcast")
    fun broadcast(@Body broadcastRequest: BroadcastRequest): Call<String>

    @POST("sendNotification")
    fun sendNotification(@Body notificationRequest: NotificationRequest): Call<String>

    @POST("sendMessageTo")
    fun sendPushMessage( @Query("token") token: String,
                         @Query("title") title: String,
                         @Query("body") body: String): Call<String>
}