package com.example.acrobot.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acrobot.R
import com.example.acrobot.common.Constant
import com.example.acrobot.common.Resource
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.databinding.FragmentLoginBinding
import com.example.acrobot.databinding.FragmentProfileBinding
import com.example.acrobot.ui.activities.AppActivity
import com.example.acrobot.ui.activities.MainActivity
import com.example.acrobot.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private val binding get() = _binding
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences(Constant.user, Context.MODE_PRIVATE)
        editor=sharedPreferences.edit()
        binding.changePasswordContainer.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
        binding.back.setOnClickListener {
            val intent =Intent(Intent(requireContext(), AppActivity::class.java))
            val b = Bundle()
            b.putBoolean(Constant.bundleApp,false)
            intent.putExtras(b)
            startActivity(intent)
            requireActivity().finish()
        }
        getProfile()
        binding.btnSubmit.setOnClickListener {
            updateProfile(RegisterModel(binding.inputTextName.text.toString(), email = null, password = null, mobile = binding.inputTextMobilePhone.text.toString()))
        }
        binding.logoutContainer.setOnClickListener {
            editor.remove(Constant.token)
            editor.remove(Constant.userId)
            editor.remove(Constant.keepSignIn)
            editor.remove(Constant.email)
            editor.commit()
            val intent =Intent(Intent(requireContext(), MainActivity::class.java))
            val b = Bundle()
            b.putBoolean(Constant.bundleApp,true)
            intent.putExtras(b)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    private fun getProfile() {
        profileViewModel.getProfileData(sharedPreferences.getString(Constant.userId, null)!!)
        profileViewModel.profileDataFlow.onEach {
            when (it) {
                is Resource.Success -> {
                    binding.spinKit.visibility=View.GONE
                    binding.inputTextLayoutName.hint=it.data!!.name.toString()
                    binding.inputTextLayoutMobilePhone.hint=it.data!!.name.toString()
                }

                is Resource.Loading -> {
                    binding.spinKit.visibility=View.VISIBLE

                }

                is Resource.Error -> {
                    binding.spinKit.visibility=View.GONE
                    Log.e("hazemmmm", "onCreate: ${it.message}")
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun updateProfile(registrationModel:RegisterModel) {
        profileViewModel.updateProfileData(sharedPreferences.getString(Constant.userId, null)!!,registrationModel)
        profileViewModel.updateProfileFlow.onEach {
            when (it) {
                is Resource.Success -> {
                    binding.spinKit.visibility=View.GONE
                    Log.e("hazemmmm", "onCreate: ${it.data!!.message}")
                }

                is Resource.Loading -> {
                    binding.spinKit.visibility=View.VISIBLE
                }

                is Resource.Error -> {
                    binding.spinKit.visibility=View.GONE
                    Log.e("hazemmmm", "onCreate: ${it.message}")
                }
            }


        }.launchIn(lifecycleScope)
    }
}