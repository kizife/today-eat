package com.ssafy.smartstore_jetpack.ui.category

import MenuAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat // 수정된 DTO import
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.ProductServiceObject

private const val TAG = "CategoryMain_싸피"

class CategoryMain : Fragment() {

    private var itemType: String? = null

    private lateinit var menuAdapter: MenuAdapter
    private val productList = mutableListOf<ProductTodayeat>()
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
              (activity as MainActivity).opneFragmentBanner("Category")
            }
        })

        arguments?.let {
            itemType = it.getString("type")
            Log.d(TAG, "onCreateView: Received! $itemType")
        }

        (activity as MainActivity).updateBottomNavigationSelection(R.id.blank)
        val view = inflater.inflate(R.layout.fragment_category_menu, container, false)

        val menuTitle: TextView = view.findViewById(R.id.menuTitle)
        menuTitle.text = "메인"

        val recyclerView: RecyclerView = view.findViewById(R.id.menuList)
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 2열 레이아웃

        menuAdapter = MenuAdapter(productList, requireActivity())
        recyclerView.adapter = menuAdapter
        ProductServiceObject.getProductList(productList ,menuAdapter,"main",requireContext())

        val sideBtn: AppCompatButton = view.findViewById(R.id.sidebtn)
        sideBtn.setOnClickListener {
            (activity as MainActivity).opneFragmentBanner("Side")
        }

        val soupBtn: AppCompatButton = view.findViewById(R.id.soupbtn)
        soupBtn.setOnClickListener {
            (activity as MainActivity).opneFragmentBanner("Soup")
        }

        return view
    }

}
