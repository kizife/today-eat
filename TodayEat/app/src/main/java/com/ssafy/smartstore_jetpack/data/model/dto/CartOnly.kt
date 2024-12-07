package com.ssafy.smartstore_jetpack.data.model.dto

data class CartOnly(
    val cartId: Int,
    val dosirockId: Int,
    val quantity: Int,
    val userId: String
)