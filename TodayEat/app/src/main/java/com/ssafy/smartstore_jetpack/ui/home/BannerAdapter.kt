package com.ssafy.smartstore_jetpack.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.R

class BannerAdapter(private val bannerImages: List<Int>, private val onBannerClick: (Int) -> Unit) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bannerImage: ImageView = itemView.findViewById(R.id.banner_image)

        init {
            itemView.setOnClickListener{
                onBannerClick(adapterPosition%bannerImages.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val imageRes = bannerImages[position % bannerImages.size]
        holder.bannerImage.setImageResource(imageRes)
    }

    override fun getItemCount(): Int {
        return bannerImages.size * 100 // 무한 롤링을 위해 큰 값 사용
    }
}