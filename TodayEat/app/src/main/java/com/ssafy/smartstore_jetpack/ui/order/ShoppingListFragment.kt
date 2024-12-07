package com.ssafy.smartstore_jetpack.ui.order

import HomeFragment
import ShoppingCombiAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import com.ssafy.smartstore_jetpack.data.model.dto.ShoppingCartTodayeat
import com.ssafy.smartstore_jetpack.data.model.dto.UserTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.data.remote.UserService
import com.ssafy.smartstore_jetpack.databinding.FragmentShoppingListBinding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "ShoppingListF_싸피"
const val ORDER_ID = "orderId"

//장바구니 Fragment
class ShoppingListFragment : BaseFragment<FragmentShoppingListBinding>(
    FragmentShoppingListBinding::bind,
    R.layout.fragment_shopping_list
) {
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private lateinit var mainActivity: MainActivity

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var shoppingCombiAdapter: ShoppingCombiAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel.updateCartTotalPrice()
        activityViewModel.cartTotalPrice.observe(viewLifecycleOwner, Observer { totalPrice ->
            binding.tvTotalPrice.text = "$totalPrice 원"
        })

        val backStackCount = parentFragmentManager.backStackEntryCount
        Log.d("BackStack", "BackStack Count: $backStackCount")


        super.onViewCreated(view, savedInstanceState)

        val userId = SharedPreferencesUtil(requireContext()).getUser().id
        val useradd = SharedPreferencesUtil(requireContext()).getUser().address
        activityViewModel.address = useradd
        val currentAddress = SharedPreferencesUtil(requireContext()).getUser().address
        getUserInfo(userId)

        binding.editAddBtn.setOnClickListener {
            showEditAddressDialog(currentAddress, userId)
        }

        val addressView = binding.address
        addressView.isSelected = true

        initAdapter()
        initEvent()

        setupTimeButton(binding.time0730, 7, 30)
        setupTimeButton(binding.time0800, 8, 0)
        setupTimeButton(binding.time1130, 11, 30)
        setupTimeButton(binding.time1200, 12, 0)
        setupTimeButton(binding.time1730, 17, 30)
        setupTimeButton(binding.time1800, 18, 0)
        setupTimeButton(binding.time1830, 18, 30)
        refreshList()

        refreshList()


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
                if (newAddress.isNotEmpty()) {
                    updateAddress(userId, newAddress)
                    val currentAddressEditText: TextView = dialogView.findViewById(R.id.currentAdd)
                    currentAddressEditText.text = newAddress
                    binding.address.text = newAddress
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


    private fun updateAddress(userId: String, newAddress: String) {
        val userService = ApplicationClass.retrofit.create(UserService::class.java)

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


    private fun navigateToDetailFragment(combination: Combination) {
        val detailFragment = ShoppingDetailFragment()
        val bundle = Bundle().apply {
            putParcelable("combination", combination)
        }

        detailFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    @SuppressLint("NewApi")
    fun getCurrentDateWithTime(hour:Int, min: Int ): String {
        // 현재 날짜와 시간 가져오기
        val currentDate = LocalDateTime.now()

        // 시간을 07:30으로 설정
        val modifiedDate = currentDate.withHour(hour).withMinute(min).withSecond(0).withNano(0)

        // 원하는 형식으로 날짜 포맷하기
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return modifiedDate.format(formatter)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setupTimeButton(button: View, hour: Int, minute: Int) {
        button.setOnClickListener(null)
        // 현재 시간 가져오기
        val currentDateTime = LocalDateTime.now().plusMinutes(30)
        Log.d(TAG, "setupTimeButton: $currentDateTime")

        // getCurrentDateWithTime 호출 및 변환
        val targetTimeString = getCurrentDateWithTime(hour, minute)
        Log.d(TAG, "setupTimeButton: $targetTimeString")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        try {
            val targetDateTime = LocalDateTime.parse(targetTimeString, formatter)
            Log.d(TAG, "Target DateTime: $targetDateTime, Current DateTime: $currentDateTime")
                // 비교 논리
                if (currentDateTime.isAfter(targetDateTime)) {
                    button.background = ContextCompat.getDrawable(button.context, R.drawable.button_no_use)
                    button.setOnClickListener {
                        button.isEnabled = false
                        Toast.makeText(requireContext(), "현재시간으로부터 30분 이전에만 주문이 가능합니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    button.setOnClickListener {
                    // 클릭 이벤트 처리
                    clickedbtn()
                    button.isSelected = true
                    activityViewModel.diriveryTime = targetTimeString
                    }
                }
        } catch (e: Exception) { Log.e(TAG, "Error parsing target time string: $targetTimeString", e) }
    }


    // 버튼 초기화
    private fun clickedbtn(){
        binding.time0730.isSelected = false
        binding.time0800.isSelected = false
        binding.time1130.isSelected = false
        binding.time1200.isSelected = false
        binding.time1730.isSelected = false
        binding.time1800.isSelected = false
        binding.time1830.isSelected = false
    }

    fun getUserInfo(userId: String) {
        lifecycleScope.launch {
            try {
                val userInfo = ApplicationClass.sharedPreferencesUtil.getUser()
                // UI 업데이트
                updateUIWithUserInfo(userInfo)
                userInfo.address?.let {
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching user info: ${e.message}")
            }
        }
    }

    fun updateUIWithUserInfo(userInfo: UserTodayeat) {
        binding.address.text = userInfo.address  // 주소 표시
    }

    private fun initAdapter() {
        shoppingListAdapter = ShoppingListAdapter(mutableListOf())

        binding.recyclerViewShoppingList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shoppingListAdapter
        }
    }

    private fun refreshList() {

        shoppingCombiAdapter = ShoppingCombiAdapter(activityViewModel) { combination ->
            Log.d(TAG, "아이템 클릭됨: $combination")
            navigateToDetailFragment(combination)
        }
        binding.recyclerViewShoppingList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shoppingCombiAdapter
        }
        shoppingCombiAdapter.notifyDataSetChanged()
    }

    private fun initEvent() {
        binding.btnOrder.setOnClickListener {

            if (activityViewModel.diriveryTime != ""){
                activityViewModel.userId = SharedPreferencesUtil(requireContext()).getUser().id
                if (activityViewModel.completedCombi.isEmpty()) {

                    Toast.makeText(requireContext(), "장바구니가 비어 있습니다.", Toast.LENGTH_SHORT).show()

                } else {
                    //추후 주문 로직 구현
                    activityViewModel.getOrder()

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout_main, HomeFragment())
                    transaction.commit()

                }
            }else{
                Toast.makeText(requireContext(), "시간을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }

    private fun showDialogForOrderInShop() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("알림")
        builder.setMessage(
            "Table NFC를 먼저 찍어주세요.\n"
        )
        builder.setCancelable(true)
        builder.setNegativeButton(
            "취소"
        ) { dialog, _ ->
            dialog.cancel()
            dialog.cancel()
            showToast("주문이 취소되었습니다.")
        }
        builder.create().show()
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

}