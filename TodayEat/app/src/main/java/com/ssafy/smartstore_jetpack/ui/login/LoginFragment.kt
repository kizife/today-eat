package com.ssafy.smartstore_jetpack.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.Toast

import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.model.dto.User
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.databinding.FragmentLoginBinding
import com.ssafy.smartstore_jetpack.ui.StartActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "LoginFragment_싸피"

class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::bind,
    R.layout.fragment_login
) {
    private lateinit var startActivity: StartActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        startActivity = context as StartActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gotoJoin.setOnClickListener {
            Log.d(TAG, "onViewCreated: 조인으로 클릭됨")
            (requireActivity() as StartActivity).openFragment(2)
        }

        binding.btnLogin.setOnClickListener {
            val id = binding.loginId.text.toString()
            Log.d(TAG, "onViewCreated: $id 입력된아이디")
            val pass = binding.loginPass.text.toString()
            val sharedPreferencesUtil = SharedPreferencesUtil(requireContext())


            CoroutineScope(Dispatchers.Main).launch {
                runCatching {
                    RetrofitUtil.userService.login(User(id, pass))
                }.onSuccess {
                    if (it.name != "") {
                        (requireActivity() as StartActivity).openFragment(1)
                        Log.d(TAG, "onViewCreated:아이디를 확인합니다 $id")
                        initUser(id)
                        val user = sharedPreferencesUtil.getUser()
                        Log.d(TAG, "onViewCreated:이름을 확인합니다 ${user.name}")
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "로그인에 실패하였습니다. 아이디 비밀번호를 확인하세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }.onFailure { exception ->

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "서버연결실패",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d(TAG, "onViewCreated: ${exception.message}")
                    }
                }
            }
        }
    }

    private fun initUser(id: String) {

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                RetrofitUtil.userService.getUserInfo(id)

            }.onSuccess {
                Log.d(TAG, "initUser: $it")
                sharedPreferencesUtil.addUser(it)


            }.onFailure {
                Log.d(TAG, "initUser: ${it.message}")
                Toast.makeText(requireContext(), "정보없음.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
