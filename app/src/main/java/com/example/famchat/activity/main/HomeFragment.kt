package com.example.famchat.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.famchat.databinding.CaFragmentSimpleBinding

class HomeFragment : Fragment() {
    private var _binding: CaFragmentSimpleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CaFragmentSimpleBinding.inflate(inflater, container, false)
        binding.tvTitle.text = "Home"
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

