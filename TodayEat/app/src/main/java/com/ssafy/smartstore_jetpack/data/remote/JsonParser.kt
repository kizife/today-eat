package com.ssafy.smartstore_jetpack.data.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat


class JsonParser {
    fun parseProducts(json: String?): List<ProductTodayeat> {
        val gson = Gson()
        return gson.fromJson<List<ProductTodayeat>>(json, object : TypeToken<List<ProductTodayeat?>?>() {}.type)
    }
}
