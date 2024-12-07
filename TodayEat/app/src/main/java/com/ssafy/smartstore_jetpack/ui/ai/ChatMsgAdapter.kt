package com.ssafy.smartstore_jetpack.ui.ai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.R

class ChatMsgAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var chatMessages: MutableList<ChatMsg>? = null

    fun setDataList(dataList: MutableList<ChatMsg>?) {
        this.chatMessages = dataList
        notifyDataSetChanged()
    }

    fun addChatMsg(chatMsg: ChatMsg) {
        chatMessages!!.add(chatMsg)
        notifyItemInserted(chatMessages!!.size)
    }

   override fun getItemViewType(position: Int): Int {
        if (chatMessages!![position].role == ChatMsg.ROLE_USER) return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == 0) {
            return MyChatViewHolder(inflater.inflate(R.layout.item_my_chat, parent, false))
        }
        return BotChatViewHolder(inflater.inflate(R.layout.item_bot_chat, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMsg = chatMessages!![position]
        if (chatMsg.role == ChatMsg.ROLE_USER) {
            (holder as MyChatViewHolder).setMsg(chatMsg)
        } else {
            (holder as BotChatViewHolder).setMsg(chatMsg)
        }
    }

    override fun getItemCount(): Int {
        return if (chatMessages == null) 0 else chatMessages!!.size
    }

    internal inner class MyChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMsg: TextView = itemView.findViewById(R.id.tv_msg)

        fun setMsg(chatMsg: ChatMsg) {
            tvMsg.text = chatMsg.content
        }
    }

    internal inner class BotChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMsg: TextView = itemView.findViewById(R.id.tv_msg)
        fun setMsg(chatMsg: ChatMsg) {
            tvMsg.text = chatMsg.content
        }
    }
}