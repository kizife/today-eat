package com.ssafy.smartstore_jetpack.data.model.dto

data class OrderDetailInfoTodayeat(
    val dosirackPrice: Int,
    val dosirockId: Int,
    val id: Int,
    val mainDish: String,
    val orderId: Int,
    val quantity: Int,
    val sideDish1: String,
    val sideDish2: String,
    val soup: String,
    val totalPrice: Int
)