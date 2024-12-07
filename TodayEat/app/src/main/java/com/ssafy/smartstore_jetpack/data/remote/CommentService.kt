package com.ssafy.smartstore_jetpack.data.remote

import com.ssafy.smartstore_jetpack.data.model.dto.CommentTodayeat
import retrofit2.http.*

interface CommentService {
    // comment를 추가한다.
    @POST("rest/comment")
    suspend fun insert(@Body comment: CommentTodayeat): Boolean

    // comment를 수정한다.
    @PUT("rest/comment")
    suspend fun update(@Body comment: CommentTodayeat): Boolean

    // {id}에 해당하는 comment를 삭제한다.
    @DELETE("rest/comment/{id}")
    suspend fun delete(@Path("id") id: Int): Boolean
}