package com.ssafy.smartstore_jetpack.data.remote

import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstoredb.service.FirebaseTokenService

class RetrofitUtil {
    companion object{
        val commentService = ApplicationClass.retrofit.create(CommentService::class.java)
        val orderService = ApplicationClass.retrofit.create(OrderService::class.java)
        val productService = ApplicationClass.retrofit.create(ProductService::class.java)
        val userService = ApplicationClass.retrofit.create(UserService::class.java)
        val combiService = ApplicationClass.retrofit.create(CombiService::class.java)
        val cartService = ApplicationClass.retrofit.create(CartService::class.java)
        val fcmService = ApplicationClass.retrofit.create(FirebaseTokenService::class.java)

        val gptService = ApplicationClass.retrofit.create(ChatGPTApi::class.java)
    }
}