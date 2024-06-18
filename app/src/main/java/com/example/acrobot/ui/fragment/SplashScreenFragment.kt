package com.example.acrobot.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.acrobot.R
import com.example.acrobot.common.Constant
import com.example.acrobot.databinding.FragmentSplashScreenBinding
import com.example.acrobot.ui.activities.AppActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences


    private lateinit var _binding: FragmentSplashScreenBinding
    val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences(Constant.user, Context.MODE_PRIVATE)

        val keepSginin = sharedPreferences.getString(Constant.keepSignIn, null)
        val isVisited = sharedPreferences.getBoolean("isVisited", false)
        Log.e("token", "onViewCreated: $keepSginin")
        Log.e("isVisited", "onViewCreated: $isVisited")

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                delay(3000).apply {

                    if (!isVisited && keepSginin == null) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.splashScreenFragment, true)
                            .build()
                        findNavController().navigate(
                            R.id.action_splashScreenFragment_to_welcomeFragment,
                            savedInstanceState,
                            navOptions
                        )

                    } else if (keepSginin == null) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.splashScreenFragment, true)
                            .build()
                        findNavController().navigate(
                            R.id.action_splashScreenFragment_to_loginFragment,
                            savedInstanceState,
                            navOptions
                        )

                    } else {
                        val intent =Intent(Intent(requireContext(), AppActivity::class.java))
                        val b = Bundle()
                        b.putBoolean(Constant.bundleApp,false)
                        intent.putExtras(b)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }
    }
}

