package com.example.acrobot.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acrobot.R
import com.example.acrobot.common.Constant
import com.example.acrobot.common.Resource
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.databinding.FragmentChangePasswordBinding
import com.example.acrobot.databinding.FragmentChatBinding
import com.example.acrobot.ui.activities.AppActivity
import com.example.acrobot.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var _binding: FragmentChangePasswordBinding
    private lateinit var sharedPreferences: SharedPreferences
    val binding get() = _binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences(Constant.user, Context.MODE_PRIVATE)
        binding.back.setOnClickListener {
            findNavController().popBackStack()

        }
        binding.btnSubmit.setOnClickListener {
            val password=binding.inputTextPassword.text
            val confirmPassword=binding.inputTextConfirmPassword.text

            if(password!!.isNotEmpty()
                && confirmPassword!!.isNotEmpty()){
                if (password.toString() == confirmPassword.toString()
                ){
                     updateProfile(RegisterModel(null,null,password.toString(),null))
                }else{
                    Toast.makeText(requireContext(),"Password does Not match",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(requireContext(),"Enter Password!!",Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    private fun updateProfile(registrationModel: RegisterModel) {
        profileViewModel.updateProfileData(sharedPreferences.getString(Constant.userId, null)!!,registrationModel)
        profileViewModel.updateProfileFlow.onEach {
            when (it) {
                is Resource.Success -> {
                    binding.spinKit.visibility=View.GONE
                    Toast.makeText(requireContext(),it.data!!.message,Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    binding.spinKit.visibility=View.VISIBLE
                }

                is Resource.Error -> {
                    binding.spinKit.visibility=View.GONE
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()

                }
            }


        }.launchIn(lifecycleScope)
    }
}