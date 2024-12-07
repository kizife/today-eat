package com.ssafy.smartstore_jetpack.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.data.model.dto.ShoppingCartTodayeat
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.MainActivityViewModel

class ProductDetail : Fragment() {

    private var product: ProductTodayeat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable(ARG_PRODUCT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
              if(product!!.type == "main"){
                  (activity as MainActivity).opneFragmentBanner("Main")
              }else if(product!!.type == "side"){
                  (activity as MainActivity).opneFragmentBanner("Side")
              }else{
                  (activity as MainActivity).opneFragmentBanner("Soup")
              }
            }
        })

        val view =  inflater.inflate(R.layout.fragment_product_dtail, container, false)
        val productImg: ImageView = view.findViewById(R.id.product_detail_img)
        val productName: TextView = view.findViewById(R.id.product_detail_name)
        val productPrice: TextView = view.findViewById(R.id.product_detail_price)
        val productDescription: TextView = view.findViewById(R.id.product_detail_content)

        product?.let { product ->
            // 이미지 로딩
            val imageName = product.img
            val resId = context?.resources?.getIdentifier(
                imageName.replace(".png", ""),
                "drawable", context?.packageName
            )

            Glide.with(this)
                .load(resId)
                .into(productImg)

            productName.text = product.name
            productPrice.text = "${product.price}원"
            productDescription.text = product.description
        }

        return view
    }

    companion object {
        const val ARG_PRODUCT = "product"
    }
}
