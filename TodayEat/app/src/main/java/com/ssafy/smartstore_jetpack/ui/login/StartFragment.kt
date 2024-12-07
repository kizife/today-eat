package com.ssafy.smartstore_jetpack.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.databinding.FragmentStartBinding
import com.ssafy.smartstore_jetpack.ui.StartActivity

private const val TAG = "StartFragment_싸피"

class StartFragment: BaseFragment<FragmentStartBinding>(FragmentStartBinding::bind, R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "StartFragment onViewCreated 호출됨")

        val styledText = "서비스 사용을 위해\n<b><font color=\"#ECA31C\">로그인</font></b>이 필요합니다."
        binding.startment.text = android.text.Html.fromHtml(styledText)

        binding.startLogin.setOnClickListener {

            (requireActivity() as StartActivity).openFragment(4)
        }

        binding.startJoin.setOnClickListener {
            (requireActivity() as StartActivity).openFragment(2)
        }
    }
}
