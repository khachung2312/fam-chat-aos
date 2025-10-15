package com.example.famchat.activity.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.famchat.R
import com.example.famchat.activity.main.adapter.FriendAdapter
import com.example.famchat.activity.main.adapter.MessageAdapter
import com.example.famchat.activity.main.model.Friend
import com.example.famchat.activity.main.model.Message
import com.example.famchat.databinding.FcFragmentMessageBinding

class MessageFragment : Fragment() {
    private var _binding: FcFragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FcFragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Danh sách bạn bè
        val friends = listOf(
            Friend("An", R.drawable.user_img, hasUnseenStory = true),
            Friend("Bình", R.drawable.user_img, hasUnseenStory = false),
            Friend("Chi", R.drawable.user_img, hasUnseenStory = true),
            Friend("Dũng", R.drawable.user_img, hasUnseenStory = false),
        )
        val friendAdapter = FriendAdapter(friends) { friend -> openVideoStatus(friend) }

        binding.recyclerFriends.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = friendAdapter
        }

        // ✅ Danh sách tin nhắn
        val messages = listOf(
            Message("Alice", "Hey, how are you?", "10:23", R.drawable.user_img, 3, "Online"),
            Message("Bob", "Let's meet tomorrow!", "09:45", R.drawable.user_img, 0, "Online"),
            Message("Charlie", "Ok, see you!", "Yesterday", R.drawable.user_img, 1, "Offline"),
            Message("Alice", "Hey, how are you?", "10:23", R.drawable.user_img, 3),
            Message("Bob", "Let's meet tomorrow!", "09:45", R.drawable.user_img, 0),
            Message("Charlie", "Ok, see you!", "Yesterday", R.drawable.user_img, 1),
            Message("Alice", "Hey, how are you?", "10:23", R.drawable.user_img, 3),
            Message("Bob", "Let's meet tomorrow!", "09:45", R.drawable.user_img, 0),
            Message("Charlie", "Ok, see you!", "Yesterday", R.drawable.user_img, 1),
            Message("Alice", "Hey, how are you?", "10:23", R.drawable.user_img, 3),
            Message("Bob", "Let's meet tomorrow!", "09:45", R.drawable.user_img, 0),
            Message("Charlie", "Ok, see you!", "Yesterday", R.drawable.user_img, 1),
            Message("Alice", "Hey, how are you?", "10:23", R.drawable.user_img, 3),
            Message("Bob", "Let's meet tomorrow!", "09:45", R.drawable.user_img, 0),
            Message("Charlie", "Ok, see you!", "Yesterday", R.drawable.user_img, 1),
            Message("Alice", "Hey, how are you?", "10:23", R.drawable.user_img, 3),
            Message("Bob", "Let's meet tomorrow!", "09:45", R.drawable.user_img, 0),
            Message("Charlie", "Ok, see you!", "Yesterday", R.drawable.user_img, 1),
            Message("Alice", "Hey, how are you?", "10:23", R.drawable.user_img, 3),
            Message("Bob", "Let's meet tomorrow!", "09:45", R.drawable.user_img, 0),
            Message("Charlie", "Ok, see you!", "Yesterday", R.drawable.user_img, 1)
        )

        val messageAdapter = MessageAdapter(messages) { message ->
            Toast.makeText(requireContext(), "Clicked: ${message.name}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }

        binding.recyclerMessages.isNestedScrollingEnabled = false


    }


    private fun openVideoStatus(friend: Friend) {
        Toast.makeText(requireContext(), "Xem story của ${friend.name}", Toast.LENGTH_SHORT).show()
        // Tại đây bạn có thể chuyển sang Activity hoặc Fragment hiển thị video
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


