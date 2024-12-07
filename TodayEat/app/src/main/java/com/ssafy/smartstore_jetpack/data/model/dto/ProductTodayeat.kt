package com.ssafy.smartstore_jetpack.data.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductTodayeat(
    @SerializedName("productId") var productId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("price") val price: Int,
    @SerializedName("img") val img: String,
    @SerializedName("description") val description: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),         // productId
        parcel.readString() ?: "", // name
        parcel.readString() ?: "", // type
        parcel.readInt(),         // price
        parcel.readString() ?: "", // img
        parcel.readString() ?: ""  // description
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)  // productId
        parcel.writeString(name)    // name
        parcel.writeString(type)    // type
        parcel.writeInt(price)      // price
        parcel.writeString(img)     // img
        parcel.writeString(description) // description
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ProductTodayeat> {
        override fun createFromParcel(parcel: Parcel): ProductTodayeat {
            return ProductTodayeat(parcel)
        }

        override fun newArray(size: Int): Array<ProductTodayeat?> {
            return arrayOfNulls(size)
        }
    }
}
