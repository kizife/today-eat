package com.ssafy.smartstore_jetpack.ui.my

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.model.response.OrderDetailCombi
import com.ssafy.smartstore_jetpack.ui.ProductServiceObject
import com.ssafy.smartstore_jetpack.util.CommonUtils

private const val TAG = "OrderedListAdapter_싸피"

class OrderedListAdapter(val context: Context, var orderId: Int,var list: List<OrderDetailCombi>) :
    RecyclerView.Adapter<OrderedListAdapter.OrderdListHolder>() {
    var totalPrice = 0
    init {
        // 어댑터 초기화 시 전체 합계 계산
        totalPrice = list.sumOf { it.totalPrice }
        Log.d(TAG, "init: totalPrice 계산 완료 -> $totalPrice")
    }
    inner class OrderdListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val main = itemView.findViewById<View>(R.id.mainlayout)
        val mainName = main.findViewById<TextView>(R.id.textShoppingMenuName)
        val mainPrice = main.findViewById<TextView>(R.id.textShoppingMenuMoney)
        val mainType = main.findViewById<TextView>(R.id.textShoppingNum)
        val mainImg = main.findViewById<ImageView>(R.id.menuImage)

        val side1 = itemView.findViewById<View>(R.id.side1layout)
        val side1Name = side1.findViewById<TextView>(R.id.textShoppingMenuName)
        val side1Price = side1.findViewById<TextView>(R.id.textShoppingMenuMoney)
        val side1Type = side1.findViewById<TextView>(R.id.textShoppingNum)
        val side1Img = side1.findViewById<ImageView>(R.id.menuImage)

        val side2 = itemView.findViewById<View>(R.id.side2layout)
        val side2Name = side2.findViewById<TextView>(R.id.textShoppingMenuName)
        val side2Price = side2.findViewById<TextView>(R.id.textShoppingMenuMoney)
        val side2Type = side2.findViewById<TextView>(R.id.textShoppingNum)
        val side2Img = side2.findViewById<ImageView>(R.id.menuImage)

        val soup = itemView.findViewById<View>(R.id.souplayout)
        val soupName = soup.findViewById<TextView>(R.id.textShoppingMenuName)
        val soupPrice = soup.findViewById<TextView>(R.id.textShoppingMenuMoney)
        val soupType = soup.findViewById<TextView>(R.id.textShoppingNum)
        val soupImg = soup.findViewById<ImageView>(R.id.menuImage)

        val totalDosirak = itemView.findViewById<TextView>(R.id.dosirack_price)
        val quantuty = itemView.findViewById<TextView>(R.id.quantity_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderdListHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_ordered_list_detail, parent, false)
        return OrderdListHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${list.size}")
        return list.size
    }

    override fun onBindViewHolder(holder: OrderdListHolder, position: Int) {
        val orderDetail = list[position]

        Log.d(TAG, "onBindViewHolder: $orderDetail")
        holder.quantuty.text = "수량: ${orderDetail.quantity}"

        totalPrice += orderDetail.totalPrice
        holder.totalDosirak.text = "도시락합계: ${orderDetail.totalPrice.toString()}원"
        // 각 항목을 비동기적으로 처리 후 데이터를 설정
        ProductServiceObject.findProduct(orderDetail.mainDish.toInt()) { main ->
            Log.d(TAG, "onBindViewHolder: $main")
            holder.mainName.text = main.name
            holder.mainPrice.text = CommonUtils.makeComma(main.price)
            holder.mainType.text = main.type

            val resId = context.resources.getIdentifier(
                main!!.img.replace(".png", ""),
                "drawable", context.packageName
            )

            Glide.with(context)
                .load(resId) // 드로어블에서 이미지 로드
                .into(holder.mainImg)
        }

        ProductServiceObject.findProduct(orderDetail.sideDish1.toInt()) { side1 ->
            holder.side1Name.text = side1.name
            holder.side1Price.text = CommonUtils.makeComma(side1.price)
            holder.side1Type.text = side1.type

            val resId = context.resources.getIdentifier(
                side1!!.img.replace(".png", ""),
                "drawable", context.packageName
            )

            Glide.with(context)
                .load(resId) // 드로어블에서 이미지 로드
                .into(holder.side1Img)
        }

        ProductServiceObject.findProduct(orderDetail.sideDish2.toInt()) { side2 ->
            holder.side2Name.text = side2.name
            holder.side2Price.text = CommonUtils.makeComma(side2.price)
            holder.side2Type.text = side2.type
            val resId = context.resources.getIdentifier(
                side2!!.img.replace(".png", ""),
                "drawable", context.packageName
            )

            Glide.with(context)
                .load(resId) // 드로어블에서 이미지 로드
                .into(holder.side2Img)
        }

        ProductServiceObject.findProduct(orderDetail.soup.toInt()) { soup ->
            holder.soupName.text = soup.name
            holder.soupPrice.text = CommonUtils.makeComma(soup.price)
            holder.soupType.text = soup.type
            val resId = context.resources.getIdentifier(
                soup!!.img.replace(".png", ""),
                "drawable", context.packageName
            )

            Glide.with(context)
                .load(resId) // 드로어블에서 이미지 로드
                .into(holder.soupImg)
        }
    }


}
