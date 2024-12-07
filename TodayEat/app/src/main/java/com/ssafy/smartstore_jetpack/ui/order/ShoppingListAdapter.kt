package com.ssafy.smartstore_jetpack.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import com.ssafy.smartstore_jetpack.databinding.ListItemShoppingListBinding
import com.ssafy.smartstore_jetpack.data.model.dto.ShoppingCartTodayeat


class ShoppingListAdapter(var list:MutableList<Combination>) :RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder>(){

    inner class ShoppingListHolder(private val binding: ListItemShoppingListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindInfo(position: Int){

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListHolder {
        return  ShoppingListHolder ( ListItemShoppingListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ShoppingListHolder, position: Int) {
        holder.bindInfo(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}