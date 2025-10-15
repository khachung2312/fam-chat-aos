package com.example.famchat.activity.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.famchat.R
import com.example.famchat.activity.main.adapter.CallsAdapter
import com.example.famchat.activity.main.model.Contact
import com.example.famchat.config.CallType
import com.example.famchat.config.ContactItem
import com.example.famchat.databinding.FcFragmentCallsBinding
import com.example.famchat.databinding.FcFragmentMessageBinding

class CallsFragment : Fragment() {


    private var _binding: FcFragmentCallsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FcFragmentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyData = generateDummyData()

        val adapter = CallsAdapter(dummyData)

        binding.recyclerCalls.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    // Hàm tạo dữ liệu mẫu
    private fun generateDummyData(): List<ContactItem.Person> {
        return listOf(
            ContactItem.Person(
                Contact(
                    name = "Trần Bích Thủy",
                    status = "Online",
                    todayStatus = "Đi làm về rồi",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Hôm nay 10:30",
                    callType = CallType.INCOMING
                )
            ), ContactItem.Person(
                Contact(
                    name = "Lê Văn Cường",
                    status = "Offline",
                    todayStatus = "Bận việc gia đình",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Hôm qua 18:00",
                    callType = CallType.MISSED
                )
            ), ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            ),
            ContactItem.Person(
                Contact(
                    name = "Nguyễn Thu Hà",
                    status = "Online",
                    todayStatus = "Đang đi du lịch",
                    avatarRes = R.drawable.fc_ic_user,
                    lastCallTime = "Vừa gọi 5 phút trước",
                    callType = CallType.MISSED
                )
            )

        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


