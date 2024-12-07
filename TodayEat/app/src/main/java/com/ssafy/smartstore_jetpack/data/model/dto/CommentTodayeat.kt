package com.ssafy.smartstore_jetpack.data.model.dto


data class CommentTodayeat(
    val commentId: Int,
    val userId: String,
    val productId: Int,
    val rating: Int,
    val comment: String
)