package com.ssafy.smartstore_jetpack.ui.login

import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.model.dto.UserTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.data.remote.UserService
import com.ssafy.smartstore_jetpack.databinding.FragmentJoinBinding
import com.ssafy.smartstore_jetpack.ui.StartActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.platform.Jdk9Platform.Companion.isAvailable
import kotlinx.coroutines.coroutineScope as coroutineScope

private const val TAG = "JoinFragment_싸피"
class JoinFragment : BaseFragment<FragmentJoinBinding>(
    FragmentJoinBinding::bind,
    R.layout.fragment_join
) {
    private var checkedId = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gotoLogin.setOnClickListener {
            (requireActivity() as StartActivity).openFragment(4)
        }
        binding.btnConfirm.setOnClickListener {
            val id = binding.loginId.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    RetrofitUtil.userService.isUsedId(id)
                }.onSuccess {
                    withContext(Dispatchers.Main) { // UI 작업은 Main 스레드에서 실행
                        if (!it && id!="" ) {
                            checkedId = true
                            Log.d(TAG, "onViewCreated: 아이디 확인 성공")
                            Toast.makeText(requireContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_SHORT).show()
                            binding.btnConfirm.setBackgroundColor(Color.parseColor("#708954"))
                        }else if(id==""){
                            Toast.makeText(requireContext(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(requireContext(), "이미 사용중인 아이디 입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.onFailure { throwable ->
                    Log.e(TAG, "오류 발생: ${throwable.message}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "오류 발생: ${throwable.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 회원가입 버튼
        binding.btnJoin.setOnClickListener {

            val id = binding.loginId.text.toString()
            val pass = binding.joinPw.text.toString()
            val name = binding.joinName.text.toString()
            val address = binding.joinAd.text.toString()
            val phone = binding.joinNum.text.toString()
            val user = UserTodayeat(id,name, pass, phone,address)

            CoroutineScope(Dispatchers.Main).launch {

                runCatching {
                    RetrofitUtil.userService.insert(user)
                }.onSuccess {
                    if(it && checkedId  ){
                        if (id!=""&& pass!=""&& name!=""&& address!=""&& phone!=""){
                            Toast.makeText(requireContext(), "회원가입 성공", Toast.LENGTH_SHORT).show()
                            (requireActivity() as StartActivity).openFragment(3)
                        }else{
                            Toast.makeText(requireContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        Toast.makeText(requireContext(), "아이디 중복확인이 필요합니다.", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure {

                }
            }
        }
    }
}