package com.ssafy.smartstore_jetpack.data.model.dto

data class User (
    val id: String,
    val name: String,
    val pass: String
){
    constructor(id:String, pass:String):this(id, "",pass,)
}