package com.ssafy.smartstore_jetpack.data.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.smartstore_jetpack.data.model.dto.CommentTodayeat

data class ProductWithCommentResponse(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val productName: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("price") val productPrice: Int = 0,
    @SerializedName("img") val productImg: String = "",

    @SerializedName("commentCount") val productCommentTotalCnt: Int = 0,
    @SerializedName("averageStars") val productRatingAvg: Double = 0.0,
    @SerializedName("totalSells") val productTotalSellCnt: Int = 0,

    @SerializedName("comments") val comments: MutableList<CommentTodayeat> = mutableListOf()
)
