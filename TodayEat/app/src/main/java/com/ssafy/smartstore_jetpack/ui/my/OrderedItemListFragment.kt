package com.ssafy.smartstore_jetpack.ui.my

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.model.response.OrderDetailCombi
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.databinding.FragmentOrderedItemListBinding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "OrderedItemListFragment_싸피"
class OrderedItemListFragment : BaseFragment<FragmentOrderedItemListBinding>(
    FragmentOrderedItemListBinding::bind, R.layout.fragment_ordered_item_list
) {
    private lateinit var mainActivity: MainActivity
    private var isActivityAttached = false
    private var orderId: Int = 0
    private lateinit var adapter: OrderedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getInt("orderId", 0)
            Log.d(TAG, "onCreate: $orderId")
        // Bundle에서 orderId를 가져옴
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        isActivityAttached = true
    }

    override fun onDetach() {
        super.onDetach()
        isActivityAttached = false  // Activity와의 연결이 끊어졌음을 표시
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.navigation_page_4
            }
        })


        // 어댑터 초기화
        loadOrderDetails(orderId)
        binding.orderedList.layoutManager = LinearLayoutManager(requireContext())

        // 어댑터에 데이터를 전달 (필요 시 데이터를 설정하거나 API 호출 등)

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.category_fab)?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.category_fab)?.visibility = View.VISIBLE
    }


    fun loadOrderDetails(orderId: Int){
        CoroutineScope(Dispatchers.Main).launch{
            runCatching {
                RetrofitUtil.orderService.getOrderDetail(orderId)
            }.onSuccess {
                adapter = OrderedListAdapter(requireContext(), orderId, it)
                binding.orderedList.adapter = adapter  // RecyclerView에 어댑터 설정
                Log.d(TAG, "loadOrderDetails: $it")
                binding.totalTv.text = "총합: ${adapter.totalPrice}원"
                Log.d(TAG, "loadOrderDetails: ${adapter.totalPrice} 토탈프라이스 가보자고")
            }.onFailure {
                Log.d(TAG, "loadOrderDetails: ${it.message}")
            }
        }


    }
}
