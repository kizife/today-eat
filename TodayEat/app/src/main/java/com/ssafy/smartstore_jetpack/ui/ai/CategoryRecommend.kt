package com.ssafy.smartstore_jetpack.ui.ai

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.key
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.data.model.dto.ChatGPTRequest
import com.ssafy.smartstore_jetpack.data.model.dto.JsonReader
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.data.model.response.ChatGPTResponse
import com.ssafy.smartstore_jetpack.data.remote.ChatGPTApi
import com.ssafy.smartstore_jetpack.data.remote.JsonParser
import com.ssafy.smartstore_jetpack.ui.category.Category
import com.ssafy.smartstore_jetpack.ui.category.CategorySide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

private const val TAG = "CategoryRecommend_싸피"
class CategoryRecommend : Fragment() {

    var recyclerView: RecyclerView? = null
    var adapter: ChatMsgAdapter? = null
    var btnSend: Button? = null
    var etMsg: EditText? = null
    var progressBar: ProgressBar? = null
    var chatMsgList: MutableList<ChatMsg>? = null
    private lateinit var chatGPTApi: ChatGPTApi

    private val jsonReader = JsonReader()
    private val jsonParser = JsonParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category_recommend, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_layout_main, Category())
                transaction.commit()
            }
        })

        chatGPTApi = RetrofitUtil.gptService

        //뷰 객체 연결
        recyclerView = view.findViewById(R.id.chatBot)
        btnSend = view.findViewById(R.id.btn_send)
        etMsg = view.findViewById(R.id.et_msg)
        progressBar = view.findViewById(R.id.progressBar)

        //채팅 메시지 데이터를 담을 list 생성
        chatMsgList = ArrayList()
        //리사이클러뷰 초기화
        recyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChatMsgAdapter()
        adapter!!.setDataList(chatMsgList)
        recyclerView?.adapter = adapter

        // 메시지 전송버튼 클릭 리스너 설정 (람다식으로 작성)
        btnSend?.setOnClickListener {
            val msg = etMsg?.text.toString()
            adapter!!.addChatMsg(ChatMsg(ChatMsg.ROLE_USER, msg))
            sendMsgToChatGPT(msg)
            etMsg?.setText(null)
            //키보드를 내립니다.
            val manager = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        // EditText 객체에 text가 변경될 때 실행될 리스너 설정
        etMsg?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                //입력창에 메시지가 입력되었을 때만 버튼이 클릭 가능하도록 설정
                btnSend?.isEnabled = s.length > 0
            }
        })

        return view
    }


    private fun sendMsgToChatGPT(userMessage: String) {

        val json = jsonReader.readJsonFromAsset(requireContext(), "menu_data.json")
        val productList: List<ProductTodayeat> = jsonParser.parseProducts(json)
        val menuContext = productList.joinToString("\n") { //모델에게 제공할 텍스트 형식
            "메뉴: ${it.name}, 종류: ${it.type}, 설명: ${it.description}"
        }

        progressBar?.visibility = View.VISIBLE
        activity?.window?.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )


        val request = ChatGPTRequest(
            model = "gpt-4o-mini",  // 모델 선택
            messages = listOf(
                ChatMsg(role = "system", content = "다음은 메뉴 데이터입니다. 이를 바탕으로 유연하게 응답하세요. 추천 메뉴는 3개 이하로, 설명은 간결하게 답변하세요. 답변에 이모지를 포함시키면 귀여워져요. 그리고, ** 등을 활용한 서체 변경은 불가합니다."),
                ChatMsg(role = "system", content = menuContext),
                ChatMsg(role = "user", content = userMessage) 
            )
        )

        chatGPTApi.getChatResponse(request)?.enqueue(object : Callback<ChatGPTResponse?> {
            override fun onResponse(call: Call<ChatGPTResponse?>, response: Response<ChatGPTResponse?>) {
                if (response.isSuccessful ) {
                    val chatResponse = response.body()?.choices?.get(0)?.message?.content

                    chatMsgList!!.add(ChatMsg(ChatMsg.ROLE_ASSISTANT, chatResponse ?: ""))
                    adapter?.notifyDataSetChanged()
                    recyclerView?.scrollToPosition(chatMsgList!!.size -1)

                    progressBar?.visibility = View.GONE
                    activity?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                } else {
                    Log.e(TAG, "onResponse: ${response.code()}, ${response.message()}", )
                    Log.e(TAG, "onResponse: ${response.errorBody()?.string()}", )
                }


            }

            override fun onFailure(call: Call<ChatGPTResponse?>, t: Throwable) {
                Log.e(TAG, "API failure: ${t.message}")
            }
        })

    }


    override fun onStart() {
        super.onStart()

        chatMsgList!!.add(ChatMsg(ChatMsg.ROLE_ASSISTANT, "반가워요! 어떻게 도와드릴까요? 🍳"))
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
