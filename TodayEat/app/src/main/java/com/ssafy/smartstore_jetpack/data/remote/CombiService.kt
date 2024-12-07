package com.ssafy.smartstore_jetpack.data.remote

import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CombiService {

    //도시락 아이디에 해당하는 도시락 조합
    @GET("rest/combination/{dosirackId}")
    suspend fun getCombi (@Path("dosirackId") dosirackId:Int): Combination

    //아이디별 도시락 조합
    @GET("rest/combination/user/{userId}")
    suspend fun getCombiList(@Path("userId") userId: String):  List<Combination>

    // combi정보를 업데이트 한다
    @PUT("rest/combination/{dosirackId}")
    suspend fun combiUpdate(@Body combi: Combination)

    // combi삭제
    @DELETE("rest/combination/{dosirackId}")
    suspend fun delete(@Path("dosirackId") dosirackId: Int)

    @POST("rest/combination")
    suspend fun insertCombi(@Body  combi: Combination): Int


}