package com.example.acrobot.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.acrobot.ui.activities.ProfileActivity
import com.example.acrobot.common.Constant
import com.example.acrobot.databinding.FragmentHomeBinding
import com.example.acrobot.ui.activities.UploadImageActivity


class HomeFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences(Constant.user, Context.MODE_PRIVATE)
        binding.profileImage.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
            requireActivity().finish()
        }
        binding.btnNext.setOnClickListener{
            startActivity(Intent(requireContext(), UploadImageActivity::class.java))
            requireActivity().finish()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}