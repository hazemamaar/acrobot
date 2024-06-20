package com.example.acrobot.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.acrobot.R
import com.example.acrobot.databinding.FragmentForgetPasswordBinding


class ForgetPasswordFragment : Fragment() {
    private lateinit var _binding: FragmentForgetPasswordBinding
    val binding get() = _binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_forgetPasswordFragment_to_successPasswordFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}