package com.example.famchat.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.famchat.databinding.FcFragmentSimpleBinding

class ChatFragment : Fragment() {
    private var _binding: FcFragmentSimpleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FcFragmentSimpleBinding.inflate(inflater, container, false)
        binding.tvTitle.text = "Chat"
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


