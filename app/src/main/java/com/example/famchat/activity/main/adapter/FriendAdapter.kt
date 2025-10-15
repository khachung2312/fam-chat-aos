package com.example.famchat.activity.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.famchat.R
import com.example.famchat.activity.main.model.Friend
import com.example.famchat.config.CallType
import com.example.famchat.config.ContactItem
import com.example.famchat.databinding.FcItemContactBinding
import com.example.famchat.databinding.FcItemFriendBinding

class FriendAdapter(
    private val items: List<Friend>,
    private val onClick: (Friend) -> Unit
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    // Định nghĩa 2 loại View Type
    companion object {
        private const val TYPE_YOUR_STORY = 0 // Item đầu tiên
        private const val TYPE_FRIEND_STORY = 1 // Các item còn lại
    }

    inner class FriendViewHolder(val binding: FcItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Thiết lập sự kiện click cho toàn bộ item
            binding.root.setOnClickListener {
                // Đảm bảo item hợp lệ trước khi gọi onClick
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClick(items[adapterPosition])
                }
            }
        }

        fun bind(friend: Friend, viewType: Int) {
            binding.imgAvatar.setImageResource(friend.avatarRes)
            binding.name.text = friend.name

            // --- Xử lý logic cho Your Story và Friend Story ---

            if (viewType == TYPE_YOUR_STORY) {
                // ITEM BẢN THÂN

                // 1. Ẩn viền Story (để Your Story đơn giản hơn, hoặc tùy chỉnh riêng)
                // Hoặc bạn có thể đặt một viền đơn giản cho Your Story
                binding.frameBorder.setBackgroundResource(R.drawable.fc_bg_icon_radius_circle)

                // 2. Hiển thị nút (+)
                binding.imgAddIcon.visibility = View.VISIBLE

                // 3. Đặt tên là "Bạn" hoặc "Your Story"
                binding.name.text = "My status" // Ví dụ: "Bạn"

            } else {
                // ITEM BẠN BÈ (FRIEND STORY)

                // 1. Thiết lập viền Story (Seen/Unseen)
                val borderRes = if (friend.hasUnseenStory) {
                    R.drawable.fc_bg_friend_border_unseen
                } else {
                    R.drawable.fc_bg_friend_border_seen
                }
                binding.frameBorder.setBackgroundResource(borderRes)

                // 2. Ẩn nút (+)
                binding.imgAddIcon.visibility = View.GONE
            }
        }
    }

    // 💡 Xác định View Type cho item đầu tiên
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_YOUR_STORY else TYPE_FRIEND_STORY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FriendViewHolder(FcItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    // 💡 Truyền View Type vào hàm bind
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) =
        holder.bind(items[position], getItemViewType(position))
}