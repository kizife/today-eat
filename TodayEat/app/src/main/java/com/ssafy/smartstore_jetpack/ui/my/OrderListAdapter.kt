package com.ssafy.smartstore_jetpack.ui.my

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.data.model.dto.OrderTodayeat
import com.ssafy.smartstore_jetpack.data.model.response.OrderDetailCombi
import com.ssafy.smartstore_jetpack.data.remote.OrderService
import com.ssafy.smartstore_jetpack.ui.ProductServiceObject
import com.ssafy.smartstore_jetpack.util.CommonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "OrderListAdapter_싸피"
class OrderListAdapter(
    private var list: List<OrderTodayeat>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<OrderListAdapter.OrderHolder>() {

    inner class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val menuImage = itemView.findViewById<ImageView>(R.id.orderMenuImg)
        val textMenuNames = itemView.findViewById<TextView>(R.id.orderMenuName)
        val textMenuPrice = itemView.findViewById<TextView>(R.id.orderMenuPrice)
        val textMenuDate = itemView.findViewById<TextView>(R.id.orderMenuDate)

        fun bindInfo(data: OrderTodayeat) {
            Log.d(TAG, "bindInfo: ${data}")

            val orderTime = data.deliveryTime
            textMenuDate.text = if (orderTime != null && orderTime.length >= 10) {
                orderTime.substring(0, 10) // 날짜 부분만 추출
            } else {
                "날짜 없음" // 기본값 설정
            }

            loadOrderDetails(data.orderId) { orderDetailList ->
                if (orderDetailList.isNotEmpty()) {
                    val totalPrice = orderDetailList.sumOf { it.totalPrice }
                    val orderDetail = orderDetailList[0]

                    textMenuNames.text = orderDetail.mainDish
                    textMenuPrice.text = CommonUtils.makeComma(totalPrice)

                    ProductServiceObject.findProduct(orderDetail.mainDish.toInt()) { product ->
                        textMenuNames.text = "${product.name} 외"

                        val resId = itemView.context.resources.getIdentifier(
                            product.img.replace(".png", ""),
                            "drawable", itemView.context.packageName
                        )

                        Glide.with(itemView)
                            .load(resId)
                            .into(menuImage)
                    }
                }
            }
        }

        private fun loadOrderDetails(orderId: Int, onResult: (List<OrderDetailCombi>) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val orderDetailList = ApplicationClass.retrofit.create(OrderService::class.java)
                        .getOrderDetail(orderId)

                    withContext(Dispatchers.Main) {
                        onResult(orderDetailList)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading order details: ${e.message}")
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_order_detail_list, parent, false)
        return OrderHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bindInfo(list[position])

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            onItemClicked(list[position].orderId)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}