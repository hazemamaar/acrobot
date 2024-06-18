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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.acrobot.R
import com.example.acrobot.common.Constant
import com.example.acrobot.common.Resource
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.databinding.FragmentLoginBinding
import com.example.acrobot.ui.activities.AppActivity
import com.example.acrobot.ui.activities.UploadImageActivity
import com.example.acrobot.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private val loginViewModel: AuthViewModel by viewModels()
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences(Constant.user, Context.MODE_PRIVATE)
        sharedPreferencesEditor=sharedPreferences.edit()
        binding.goToRegistration.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_loginFragment_to_registrationFragment,
                savedInstanceState,
                navOptions
            )
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.inputTextEmail.text.toString()
            val password = binding.inputTextPassword.text.toString()
            if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                binding.inputTextEmail.error = "Invalid email address"
            } else {
                loginViewModel.login(
                    RegisterModel(
                        email=email,
                        password = password
                    )
                )
                loginViewModel.loginFlow.onEach {
                    when (it) {
                        is Resource.Success -> {
                            binding.spinKit.visibility=View.GONE

                            Log.e("email", "onViewCreated: $email", )
                            if (binding.keepSignInCheckbox.isChecked) {
                                sharedPreferencesEditor.putString(Constant.keepSignIn,"yes").apply()
                            }
                            sharedPreferencesEditor.putString(Constant.email,email).apply()
                            sharedPreferencesEditor.putString(Constant.token,it.data!!.token).apply()
                            sharedPreferencesEditor.putString(Constant.userId,it.data.userId)
                            sharedPreferencesEditor.commit()
                            Toast.makeText(context, it.data.toString(), Toast.LENGTH_SHORT).show()
                            val intent =Intent(Intent(requireContext(), AppActivity::class.java))
                            val b = Bundle()
                            b.putBoolean(Constant.bundleApp,false)
                            intent.putExtras(b)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        is Resource.Error -> {
                            binding.spinKit.visibility=View.GONE
                            Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Resource.Loading -> {
                              binding.spinKit.visibility=View.VISIBLE
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
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}