package com.ssafy.smartstore_jetpack.ui

import MenuAdapter
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ProductServiceObject_싸피"

object ProductServiceObject {

    fun getProductList(productList:MutableList<ProductTodayeat>, menuAdapter: MenuAdapter, type: String, context: Context): List<ProductTodayeat> {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitUtil.productService.getProductList() // List<ProductTodayeat> 응답

                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        productList.clear()
                        productList.addAll(response.filter { it.type == type })
                        menuAdapter.notifyDataSetChanged()
                    } else {
                        // 빈 목록 처리 (Toast 메시지로 사용자에게 알리기)
                        Toast.makeText(context, "상품 목록이 비어 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(
                        TAG,
                        "getProductList: ProductList 가져오는 중 에러 발생 "
                    )
                    Toast.makeText(context, "상품 목록을 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return productList
    }


   fun findProduct(id: Int, callback: (ProductTodayeat) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                RetrofitUtil.productService.getProduct(id)
            }.onSuccess {
                val product = it
                callback(product) // 데이터를 받은 후 callback 호출
            }.onFailure {
                callback(ProductTodayeat(0, "", "", 0, "", "")) // 실패 시 빈 값 반환
            }
        }
    }


}