package com.example.acrobot.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acrobot.R
import com.example.acrobot.common.Resource
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.databinding.FragmentRegistrationBinding
import com.example.acrobot.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private lateinit var _binding: FragmentRegistrationBinding
    val binding get() = _binding
    private val registrationViewModel: AuthViewModel by viewModels()
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.register.setOnClickListener {
            val username = binding.inputTextUserName.text.toString()
            val email = binding.inputTextEmail.text.toString()
            val password = binding.inputTextPassword.text.toString()
            val confirmPassword = binding.inputTextConfirmPassword.text.toString()
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "please fill all data", Toast.LENGTH_LONG).show()
            } else if (password != confirmPassword) {
                Toast.makeText(context, "Password does not match", Toast.LENGTH_LONG).show()
                binding.inputTextConfirmPassword.error = "password does not match"
            } else if (
                !email.matches(emailPattern.toRegex())
            ) {
                Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                binding.inputTextEmail.error = "Invalid email address"
            } else if (username.length < 6) {
                binding.inputTextUserName.error = "Username must be more than 5 char"
            } else {
                registrationViewModel.registration(
                    RegisterModel(
                        email,
                        username = username,
                        password = password
                    )
                )

                registrationViewModel.registerFlow.onEach {
                    when (it) {
                        is Resource.Success -> {
                            Toast.makeText(context, "Registration Done", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Error -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        }

                        else -> {}
                    }


                }.launchIn(lifecycleScope)
            }


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}