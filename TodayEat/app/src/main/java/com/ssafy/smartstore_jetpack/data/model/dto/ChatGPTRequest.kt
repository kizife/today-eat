package com.ssafy.smartstore_jetpack.data.model.dto

import com.ssafy.smartstore_jetpack.ui.ai.ChatMsg

class ChatGPTRequest(
    val model: String,  // public으로 변경
    val messages: List<ChatMsg>
)
