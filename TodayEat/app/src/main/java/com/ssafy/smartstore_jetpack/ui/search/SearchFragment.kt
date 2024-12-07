package com.ssafy.smartstore_jetpack.ui.search

import MenuAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.databinding.FragmentSearchBinding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 검색 탭
private const val TAG = "SearchFragment_싸피"
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::bind, R.layout.fragment_search) {
    private lateinit var mainActivity: MainActivity
    private lateinit var menuAdapter: MenuAdapter
    private val productList = mutableListOf<ProductTodayeat>()
    private val filteredList = mutableListOf<ProductTodayeat>()

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).updateBottomNavigationSelection(R.id.navigation_page_2)
        val view = super.onCreateView(inflater, container, savedInstanceState)


        val recyclerView: RecyclerView = binding.searchList
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        menuAdapter = MenuAdapter(filteredList, requireActivity())
        recyclerView.adapter = menuAdapter

        binding.searchEditText

        getProductList()



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEditText = binding.searchEditText
        val cancelSearchBtn = binding.cancelSearchBtn
        val goSearchBtn = binding.goSearch

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                cancelSearchBtn.visibility = View.VISIBLE
            } else {
                cancelSearchBtn.visibility = View.GONE
            }
        }

        cancelSearchBtn.setOnClickListener {
            hideKeyboard()
            cancelSearchBtn.visibility = View.GONE
            binding.searchEditText.setText("")
            searchEditText.clearFocus() // 키보드를 내리면 포커스도 제거
        }

        goSearchBtn.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                filterProducts(query)
            } else {
                filteredList.clear()
                filteredList.addAll(productList)
                menuAdapter.notifyDataSetChanged()
            }
        }

        initEvent()
    }

    private fun filterProducts(query: String) {
        val cleanedQuery = query.trim().toLowerCase()
        val filtered = productList.filter {
            val cleanedProductName = it.name.trim().toLowerCase()
            cleanedProductName.contains(cleanedQuery)
//            it.name.contains(query, ignoreCase = true)
        }

        Log.d(TAG, "filterProducts: $filtered")
        filteredList.clear()
        filteredList.addAll(filtered)
        menuAdapter.notifyDataSetChanged()
    }


    private fun hideKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity?.currentFocus
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun initEvent(){
        // 예시로 다른 버튼에 이벤트를 연결할 수 있습니다.
        // 예: binding.searchButton.setOnClickListener { ... }
    }



    private fun getProductList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitUtil.productService.getProductList() // List<ProductTodayeat> 응답

                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        productList.clear()
                        productList.addAll(response)
                        menuAdapter.notifyDataSetChanged()
                    } else {
                        // 빈 목록 처리 (Toast 메시지로 사용자에게 알리기)
                        Toast.makeText(requireContext(), "상품 목록이 비어 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "상품 목록을 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
