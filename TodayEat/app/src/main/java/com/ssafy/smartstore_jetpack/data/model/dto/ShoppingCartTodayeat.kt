package com.ssafy.smartstore_jetpack.data.model.dto

data class ShoppingCartTodayeat(
    val productId: Int,
    val productImg: String,
    val productName: String,
    var productCnt: Int,
    val productPrice: Int,
    var totalPrice: Int = productCnt*productPrice,
    val type: String
)