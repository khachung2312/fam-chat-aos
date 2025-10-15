package com.example.famchat.activity.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.famchat.R
import com.example.famchat.databinding.FcFragmentSettingsBinding
import com.example.famchat.databinding.FcItemSettingBinding

class SettingsFragment : Fragment() {
    private var _binding: FcFragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FcFragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserInfo()
        setupMenuItems()
    }

    private fun setupUserInfo() {
        binding.tvName.text = "Nazrul Islam"
        binding.tvStatus.text = "Never give up 💪"
        binding.btnQr.setOnClickListener {
            // TODO: mở màn hình QR hoặc hiển thị dialog
        }
    }
    private fun setupMenuItems() {
        val items = listOf(
            Triple(binding.itemAccount, R.drawable.fc_ic_keys, Pair(getString(R.string.fc_settings_account_title), getString(R.string.fc_settings_account_sub))),
            Triple(binding.itemChat, R.drawable.fc_ic_chat, Pair(getString(R.string.fc_settings_chat_title), getString(R.string.fc_settings_chat_sub))),
            Triple(binding.itemNotifications, R.drawable.fc_ic_notification, Pair(getString(R.string.fc_settings_notifications_title), getString(R.string.fc_settings_notifications_sub))),
            Triple(binding.itemHelp, R.drawable.fc_ic_help, Pair(getString(R.string.fc_settings_help_title), getString(R.string.fc_settings_help_sub))),
            Triple(binding.itemStorage, R.drawable.fc_ic_data, Pair(getString(R.string.fc_settings_storage_title), getString(R.string.fc_settings_storage_sub))),
            Triple(binding.itemInvite, R.drawable.fc_ic_users, Pair(getString(R.string.fc_settings_invite_title), ""))
        )

        items.forEach { (itemBinding, icon, textPair) ->
            itemBinding.imgIcon.setImageResource(icon)
            itemBinding.tvTitle.text = textPair.first
            itemBinding.tvSub.text = textPair.second
        }

        // Click listener
        binding.itemAccount.root.setOnClickListener {
            // TODO: open Account screen
        }
        binding.itemChat.root.setOnClickListener {
            // TODO: open Chat settings
        }
        binding.itemNotifications.root.setOnClickListener {
            // TODO: open Notifications
        }
        binding.itemHelp.root.setOnClickListener {
            // TODO: open Help
        }
        binding.itemStorage.root.setOnClickListener {
            // TODO: open Storage settings
        }
        binding.itemInvite.root.setOnClickListener {
            // TODO: share app
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
