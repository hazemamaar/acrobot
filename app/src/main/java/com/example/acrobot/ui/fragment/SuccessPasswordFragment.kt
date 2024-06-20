package com.example.acrobot.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.acrobot.R
import com.example.acrobot.databinding.FragmentChatBinding
import com.example.acrobot.databinding.FragmentSuccussfulPasswordBinding


class SuccessPasswordFragment : Fragment() {


    private lateinit var _binding: FragmentSuccussfulPasswordBinding
    val binding get() = _binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_successPasswordFragment_to_loginFragment)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSuccussfulPasswordBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
}