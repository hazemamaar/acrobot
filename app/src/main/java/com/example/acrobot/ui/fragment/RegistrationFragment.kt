package com.example.acrobot.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
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
    private val loginViewModel: AuthViewModel by viewModels()

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            val username = binding.inputTextName.text.toString()
            val email = binding.inputTextEmail.text.toString()
            val password = binding.inputTextPassword.text.toString()
            val confirmPassword = binding.inputTextPassword.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "please fill all data", Toast.LENGTH_LONG).show()
            } else if (password != confirmPassword) {
                Toast.makeText(context, "Password does Not match", Toast.LENGTH_LONG).show()
                binding.inputTextPassword.error = "password does Not match"
            } else if (
                !email.matches(emailPattern.toRegex())
            ) {
                Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                binding.inputTextEmail.error = "Invalid email address"
            } else if (username.length < 6) {
                binding.inputTextName.error = "Username must be more than 5 char"
            } else {
                loginViewModel.registration(
                    RegisterModel(
                        email = email,
                        name=username,
                        password = password,
                        mobile = binding.inputTextMobilePhone.text.toString()
                    )
                )
                loginViewModel.registerFlow.onEach {
                    when (it) {
                        is Resource.Success -> {
                            binding.spinKit.visibility = View.GONE

                            Toast.makeText(
                                context,
                                it.data!!.message ,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.spinKit.visibility = View.GONE
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.registrationFragment, true)
                                .build()
                            findNavController().navigate(
                                R.id.action_registrationFragment_to_loginFragment,
                                savedInstanceState,
                                navOptions
                            )
                        }
                        is Resource.Error -> {
                            binding.spinKit.visibility = View.GONE
                            Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Resource.Loading -> {
                            binding.spinKit.visibility = View.VISIBLE
                        }
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