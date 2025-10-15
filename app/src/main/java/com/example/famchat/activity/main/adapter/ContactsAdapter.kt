package com.example.famchat.activity.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.famchat.config.ContactItem
import com.example.famchat.databinding.FcItemContactBinding
import com.example.famchat.databinding.FcItemContactHeaderBinding


class ContactsAdapter(private val items: List<ContactItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CONTACT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ContactItem.Header -> TYPE_HEADER
            is ContactItem.Person -> TYPE_CONTACT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val binding = FcItemContactHeaderBinding.inflate(inflater, parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = FcItemContactBinding.inflate(inflater, parent, false)
            ContactViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ContactItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ContactItem.Person -> (holder as ContactViewHolder).bind(item)
        }
    }

    class HeaderViewHolder(private val binding: FcItemContactHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContactItem.Header) {
            binding.tvHeader.text = item.letter
        }
    }

    class ContactViewHolder(private val binding: FcItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContactItem.Person) {
            val contact = item.contact
            binding.tvName.text = contact.name
            binding.tvContactStatusText.text = contact.todayStatus
            binding.imgAvatar.setImageResource(contact.avatarRes)


            // Hiển thị chế độ "Contact" (ẩn các layout khác)
            binding.tvContactStatusText.visibility = View.VISIBLE

        }
    }
}

