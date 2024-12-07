//package com.ssafy.smartstore_jetpack.ui
//
//import android.R
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.View
//import android.view.inputmethod.InputMethodManager
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ProgressBar
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.ssafy.smartstore_jetpack.ui.ai.ChatMsg
//import com.ssafy.smartstore_jetpack.ui.ai.ChatMsgAdapter
//
//
//class ChatbotActivity : AppCompatActivity() {
//    var recyclerView: RecyclerView? = null
//    var adapter: ChatMsgAdapter? = null
//    var btnSend: Button? = null
//    var etMsg: EditText? = null
//    var progressBar: ProgressBar? = null
//    var chatMsgList: MutableList<ChatMsg>? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.frame_layout_main)
//
//        //뷰 객체 연결
////        recyclerView = findViewById<RecyclerView>(R.id.)
////        btnSend = findViewById<Button>(R.id.btn_send)
////        etMsg = findViewById<EditText>(R.id.et_msg)
////        progressBar = findViewById<ProgressBar>(R.id.progressBar)
//
//        //채팅 메시지 데이터를 담을 list 생성
//        chatMsgList = ArrayList()
//        //리사이클러뷰 초기화
//        recyclerView.setLayoutManager(
//            LinearLayoutManager(
//                this,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//        )
//        adapter = ChatMsgAdapter()
//        adapter!!.setDataList(chatMsgList)
//        recyclerView.setAdapter(adapter)
//
//        //메시지 전송버튼 클릭 리스너 설정 (람다식으로 작성함)
//        btnSend.setOnClickListener(View.OnClickListener { v: View? ->
//            //etMsg에 쓰여있는 텍스트를 가져옵니다.
//            val msg = etMsg.getText().toString()
//            //새로운 ChatMsg 객체를 생성하여 어댑터에 추가합니다.
//            adapter!!.addChatMsg(ChatMsg(ChatMsg.TYPE_MY_CHAT, msg))
//            //etMsg의 텍스트를 초기화합니다.
//            etMsg.setText(null)
//            //키보드를 내립니다.
//            val manager =
//                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            manager.hideSoftInputFromWindow(
//                currentFocus!!.windowToken,
//                InputMethodManager.HIDE_NOT_ALWAYS
//            )
//        })
//
//        //EditText 객체에 text가 변경될 때 실행될 리스너 설정
//        etMsg.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                //입력창에 메시지가 입력되었을 때만 버튼이 클릭 가능하도록 설정
//                btnSend.setEnabled(s.length > 0)
//            }
//        })
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        // 테스트를 위한 더미 데이터 생성
//
//        // i가 짝수일 경우 내 메시지, 홀수일 경우 챗봇의 메시지로 생성되도록 10개의 채팅메시지 객체를 만들어 리스트에 넣습니다.
//        for (i in 0..9) {
//            chatMsgList!!.add(ChatMsg(i % 2, "메시지 $i"))
//        }
//    }
//}