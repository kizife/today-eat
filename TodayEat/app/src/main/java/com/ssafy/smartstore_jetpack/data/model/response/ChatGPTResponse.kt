package com.ssafy.smartstore_jetpack.data.model.response

import com.ssafy.smartstore_jetpack.ui.ai.ChatMsg


class ChatGPTResponse {
    val choices: List<Choice>? = null

    class Choice {
        val message: ChatMsg? = null
    }
}