package com.phinion.planthelix.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.protobuf.MessageLite
import com.phinion.planthelix.databinding.ChatItemBinding
import com.phinion.planthelix.models.Message
import com.phinion.planthelix.models.SENT_BY_ME

class MessageAdapter(private val messageList: List<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(private val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            if (message.sendBy == SENT_BY_ME){
                binding.leftChatView.visibility = View.GONE
                binding.rightChatView.visibility = View.VISIBLE
                binding.rightChatTextView.text = message.message
            }else{
                binding.rightChatView.visibility = View.GONE
                binding.leftChatView.visibility = View.VISIBLE
                binding.leftChatTextView.text = message.message
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChatItemBinding.inflate(inflater)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return messageList.size

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val messageItem = messageList[position]
        holder.bind(messageItem)
    }


}
