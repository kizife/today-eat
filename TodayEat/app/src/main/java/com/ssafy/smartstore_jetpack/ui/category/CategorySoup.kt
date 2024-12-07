package com.ssafy.smartstore_jetpack.ui.category

import MenuAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat // 수정된 DTO import
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.ProductServiceObject

private const val TAG = "CategoryMain_싸피"

class CategorySoup : Fragment() {

    private lateinit var menuAdapter: MenuAdapter
    private val productList = mutableListOf<ProductTodayeat>() // 수정된 DTO 타입

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).opneFragmentBanner("Category")
            }
        })

        val view = inflater.inflate(R.layout.fragment_category_menu, container, false)

        val menuTitle: TextView = view.findViewById(R.id.menuTitle)
        menuTitle.text = "국"

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.menuList)
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 2열 레이아웃

        menuAdapter = MenuAdapter(productList, requireActivity())
        recyclerView.adapter = menuAdapter

        ProductServiceObject.getProductList(productList ,menuAdapter,"soup",requireContext())


        val mainBtn: AppCompatButton = view.findViewById(R.id.mainbtn)
        val sideBtn: AppCompatButton = view.findViewById(R.id.sidebtn)
        val soupBtn: AppCompatButton = view.findViewById(R.id.soupbtn)

        // 화면 로드 시 버튼 색 초기화
        mainBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
        mainBtn.setBackgroundResource(R.drawable.button_regular_gray)

        sideBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
        sideBtn.setBackgroundResource(R.drawable.button_regular_gray)

        soupBtn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        soupBtn.setBackgroundResource(R.drawable.button_yellow_round)

        mainBtn.setOnClickListener {
            (activity as MainActivity).opneFragmentBanner("Main")
        }

        sideBtn.setOnClickListener {
            (activity as MainActivity).opneFragmentBanner("Side")
        }

        return view
    }


}
