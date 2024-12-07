package com.ssafy.smartstore_jetpack.data.model.dto

data class OrderTodayeat(
    val address: String,
    var completed: String,
    val deliveryTime: String,
    val detailInfos: List<OrderDetailInfoTodayeat>,
    val details: List<OrderDetailTodayeat>,
    val orderId: Int,
    val orderTime: String,
    val userId: String
)