package com.ssafy.smartstore_jetpack.ui.ai

class ChatMsg(
    var role: String,
    var content: String
) {
    companion object {
        const val ROLE_ASSISTANT: String = "assistant"
        const val ROLE_USER: String = "user"
    }
}