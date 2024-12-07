package com.ssafy.smartstore_jetpack.data.remote

import com.ssafy.smartstore_jetpack.data.model.dto.ChatGPTRequest
import com.ssafy.smartstore_jetpack.data.model.response.ChatGPTResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ChatGPTApi {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer api-key"
    )

    @POST("https://api.openai.com/v1/chat/completions")
    fun getChatResponse(@Body request: ChatGPTRequest?): Call<ChatGPTResponse?>?
}