package com.ssafy.smartstore_jetpack.ui.my

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.remote.OrderService
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.databinding.FragmentMypageBinding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.MainActivityViewModel
import com.ssafy.smartstore_jetpack.util.CommonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MyPageFragment_싸피"

class MyPageFragment : BaseFragment<FragmentMypageBinding>(
    FragmentMypageBinding::bind,
    R.layout.fragment_mypage
) {
    private lateinit var mainActivity: MainActivity
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

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun navigateToOrderedListFragment(orderId: Int) {
        val orderedListFragment = OrderedItemListFragment().apply {
            arguments = Bundle().apply {
                putInt("orderId", orderId)
            }
        }
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.blank
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, orderedListFragment)
            .commit()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = SharedPreferencesUtil(requireContext()).getUser().id
        initOrderData(userId)
        populateComingMenu(userId)

        Log.d(TAG, "onViewCreated: 사용자정보 ${SharedPreferencesUtil(requireContext()).getUser()}")
        binding.mypageTitle.text = "${SharedPreferencesUtil(requireContext()).getUser().name}님,"
        binding.goingTItle.text =
            "지금 ${SharedPreferencesUtil(requireContext()).getUser().name}님에게 가고 있어요!\uD83E\uDD55"



        binding.editAddBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_edit_address, null)
            builder.setView(dialogView)

            val dialog = builder.create()
            dialog.window?.apply {
                setBackgroundDrawableResource(R.drawable.rounded_dialog_background) // 배경 둥글게 설정
                val params = attributes
                params.width =
                    (resources.displayMetrics.widthPixels * 0.6).toInt() // 화면 너비의 60%로 설정
                params.height = WindowManager.LayoutParams.WRAP_CONTENT // 높이는 콘텐츠에 맞게 설정
                attributes = params
            }
            dialog.show()

            val saveBtn: Button = dialogView.findViewById(R.id.saveBtn)
            saveBtn.setOnClickListener {
                val addressText = dialogView.findViewById<EditText>(R.id.newAdd).text.toString()
                // 주소 업데이트 로직 작성
            }

            val cancelBtn: Button = dialogView.findViewById(R.id.cancelBtn)
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        // 로그아웃 버튼 클릭 시 처리
        binding.logoutTextBtn.setOnClickListener {
            Log.d(TAG, "onViewCreated: 로그아웃 클릭")
            ApplicationClass.sharedPreferencesUtil.deleteUser()

            // 모든 백스택의 프래그먼트 제거
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            // 백스택에 있는 모든 프래그먼트 제거
            for (i in 0 until fragmentManager.backStackEntryCount) {
                fragmentManager.popBackStack()
            }

            val intent = Intent(
                requireContext(),
                com.ssafy.smartstore_jetpack.ui.StartActivity::class.java
            )  // 로그인 화면이 있는 액티비티로 이동
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK // 모든 이전 액티비티 제거
            startActivity(intent)  // 로그인 화면으로 이동

            // 현재 액티비티 종료
            requireActivity().finish()

        }
    }

    private fun initOrderData(userId: String) {
        lifecycleScope.launch {
            try {
                Log.d(TAG, "initOrderData: $userId")
                val orderList = ApplicationClass.retrofit.create(OrderService::class.java)
                    .getLastMonthOrder(userId)

                val userInfo = SharedPreferencesUtil(requireContext()).getUser()

                Log.d(TAG, "initOrderData: $userInfo")

                userInfo.address?.let {
                    showEditAddressDialog(it, userId) // 주소를 다이얼로그에 전달
                }

                if (orderList != null) {
                    Log.d("API Response", "데이터 수신 성공: ${orderList.size}개의 주문")
                    Log.d("API Response", orderList.toString())
                    withContext(Dispatchers.Main) {
                        binding.recyclerViewOrder.adapter = OrderListAdapter(orderList){ orderId ->
                            navigateToOrderedListFragment(orderId)
                        }
                        binding.recyclerViewOrder.layoutManager =
                            LinearLayoutManager(requireContext())
                    }
                } else {
                    Log.d("API Response", "응답이 null입니다.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching order data: ${e.message}")
            }
        }
    }

    private fun populateComingMenu(userId: String) {
        lifecycleScope.launch {
            try {
                val orderList = ApplicationClass.retrofit.create(OrderService::class.java)
                    .getLastMonthOrder(userId)

                val comingOrder = orderList.firstOrNull {it.completed == "N"}
                if (comingOrder != null) {

                    binding.goingList.visibility = View.VISIBLE

                    val orderDetails = RetrofitUtil.orderService.getOrderDetail(comingOrder.orderId)
                    val mainDishDetail = orderDetails.firstOrNull()

                    if(mainDishDetail != null) {
                        val productResponse = RetrofitUtil.productService.getProductList()[mainDishDetail.mainDish.toInt()]

                        withContext(Dispatchers.Main) {
                            binding.comingMenuName.text = productResponse.name
                            binding.comingMenuPrice.text = CommonUtils.makeComma(mainDishDetail.totalPrice)
                            binding.comingMenuDate.text =
                                comingOrder.deliveryTime?.take(10)

                            val imageResId = resources.getIdentifier(
                                productResponse.img.replace(".png", ""),
                                "drawable",
                                requireContext().packageName
                            )
                            Glide.with(this@MyPageFragment)
                                .load(imageResId)
                                .placeholder(R.drawable.loading_animation)
                                .into(binding.comingMenuImg)
                        }
                    }
                } else {
                    binding.goingList.visibility = View.GONE
                    binding.goingTItle.text = "지금 배달 중인 도시락이 없습니다."
                    Log.d(TAG, "populateComingMenu: 조건에 맞는 주문이 없습니다.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "populateComingMenu: 오류 발생 ${e.message}", )
            }
        }
    }

    fun showEditAddressDialog(currentAddress: String? = null, userId: String) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_edit_address, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.rounded_dialog_background) // 배경 둥글게 설정

            val params = attributes
            params.width =
                (resources.displayMetrics.widthPixels * 0.6).toInt() // 화면 너비의 60%로 설정 (반영안된듯)
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
        }

        currentAddress?.let {
            val currentAddressEditText: TextView = dialogView.findViewById(R.id.currentAdd)
            currentAddressEditText.setText(it)
        }

        binding.editAddBtn.setOnClickListener {
            dialog.show()

            val saveBtn: Button = dialogView.findViewById(R.id.saveBtn)
            saveBtn.setOnClickListener {
                val newAddress = dialogView.findViewById<EditText>(R.id.newAdd).text.toString()
                Log.d(TAG, "showEditAddressDialog: 새주소출력 $newAddress")
                if (newAddress.isNotEmpty()) {
                    updateAddress(userId, newAddress)
                    val currentAddressEditText: TextView = dialogView.findViewById(R.id.currentAdd)
                    currentAddressEditText.text = newAddress
                    dialogView.findViewById<EditText>(R.id.newAdd).setText("")
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "주소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            val cancelBtn: Button = dialogView.findViewById(R.id.cancelBtn)
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }


    }

    fun updateAddress(userId: String, newAddress: String) {
        Log.d(TAG, "updateAddress: $userId, $newAddress")

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                RetrofitUtil.userService.updateUserInfo(userId, newAddress)
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "주소를 변경하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "주소 변경 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}