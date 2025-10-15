package com.example.famchat.activity.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.famchat.R
import com.example.famchat.config.CallType
import com.example.famchat.config.ContactItem
import com.example.famchat.databinding.FcItemContactBinding


class CallsAdapter(private val items: List<ContactItem.Person>) :
    RecyclerView.Adapter<CallsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FcItemContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class ContactViewHolder(private val binding: FcItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun getCallIconResource(callType: CallType): Int {
            return when (callType) {
                CallType.INCOMING -> R.drawable.fc_ic_incoming_call // Icon gọi đến
                CallType.OUTGOING -> R.drawable.fc_ic_outgoing_call // Icon gọi đi
                CallType.MISSED -> R.drawable.fc_ic_missed_calls     // Icon cuộc gọi nhỡ
            }
        }

        fun bind(item: ContactItem.Person, isCallsScreen: Boolean = true) {
            val contact = item.contact

            binding.tvName.text = contact.name
            binding.imgAvatar.setImageResource(contact.avatarRes)

            binding.layoutCallStatus.visibility = View.VISIBLE
            binding.layoutCallActions.visibility = View.VISIBLE
            binding.lineContact.visibility = View.VISIBLE

            if (isCallsScreen) {
                binding.layoutCallStatus.visibility = View.VISIBLE
                binding.layoutCallActions.visibility = View.VISIBLE

                // 1. Set Icon dựa trên CallType (Logic theo yêu cầu của bạn)
                val callIconResId = getCallIconResource(contact.callType)
                binding.imgCallIcon.setImageResource(callIconResId)

                binding.tvCallStatusText.text = contact.lastCallTime

            }


        }
    }
}