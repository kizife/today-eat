package com.ssafy.smartstore_jetpack.data.model.response;

import com.google.gson.annotations.SerializedName

data class OrderDetailCombi(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("orderId") val orderId: Int = 0,
        @SerializedName("dosirockId") val dosirockId: Int = 0,
        @SerializedName("quantity") val quantity: Int = 0,
        @SerializedName("mainDish") val mainDish: String = "",
        @SerializedName("sideDish1") val sideDish1: String = "",
        @SerializedName("sideDish2") val sideDish2: String = "",
        @SerializedName("soup") val soup: String = "",
        @SerializedName("dosirackPrice") val dosirackPrice: Int = 0,
        @SerializedName("totalPrice") val totalPrice: Int = 0
)