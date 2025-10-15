package com.example.famchat.activity.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.famchat.R
import com.example.famchat.activity.main.model.Message
import com.example.famchat.config.ContactItem
import com.example.famchat.databinding.FcItemContactBinding
import com.example.famchat.databinding.FcItemContactHeaderBinding


class MessageAdapter(
    private val items: List<Message>,
    private val onClick: (Message) -> Unit
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(val binding: FcItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message) = with(binding) {
            binding.layoutMessageInfo.visibility = View.VISIBLE
            binding.tvLastMessage.visibility = View.VISIBLE
            tvName.text = item.name
            tvLastMessage.text = item.lastMessage
            tvLastTime.text = item.time
            imgAvatar.setImageResource(item.avatarRes)

            // Hiển thị dot on/off
            val isOnline = item.status.equals("Online", ignoreCase = true)
            if (isOnline) {
                binding.viewStatusDot.visibility = View.VISIBLE
                binding.viewStatusDot.setBackgroundResource(R.drawable.fc_ic_dot_on)
            } else {
                // Có thể chọn ẩn dot khi offline, hoặc đặt dot màu xám
                binding.viewStatusDot.visibility = View.GONE
                // binding.viewStatusDot.setBackgroundResource(R.drawable.fc_ic_dot_off)
            }

            // hiển thị badge số tin chưa đọc
            if (item.unreadCount > 0) {
                tvUnreadCount.text = item.unreadCount.toString()
                tvUnreadCount.visibility = View.VISIBLE
            } else {
                tvUnreadCount.visibility = View.GONE
            }

            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = FcItemContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
