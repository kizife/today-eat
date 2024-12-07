package com.ssafy.smartstore_jetpack.data.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Combination(
    var dosirackPrice: Int,
    var dosirockId: Int,
    var main: Int,
    var sideDish1: Int,
    var sideDish2: Int,
    var soup: Int,
    var userId: String
) : Parcelable