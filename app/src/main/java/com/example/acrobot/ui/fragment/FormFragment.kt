package com.example.acrobot.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.acrobot.R
import com.example.acrobot.common.Resource
import com.example.acrobot.databinding.FragmentFormBinding
import com.example.acrobot.ui.viewmodel.FormViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class FormFragment : Fragment() {

    private val formViewModel: FormViewModel by viewModels()

    private lateinit var _binding: FragmentFormBinding
    private val binding get() = _binding
    private var q1: String = "yes"
    private var q2: String = "yes"
    private var q3: String = "yes"
    private var q4: String = "yes"
    private var q5: String = "yes"
    private var q6: String = "yes"
    private var q7: String = "yes"
    private var q8: String = "yes"
    private var q9: String = "yes"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hashMap: HashMap<String, String> = hashMapOf()
        binding.radiosQ1.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ1.id) {
                q1 = "yes"
            } else if (radioBtnID == binding.NoQ1.id) {
                q1 = "No"
            }
        }
        binding.radiosQ2.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ2.id) {
                q2 = "yes"
            } else if (radioBtnID == binding.NoQ2.id) {
                q2 = "No"
            }
        }
        binding.radiosQ3.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ3.id) {
                q3 = "yes"
            } else if (radioBtnID == binding.NoQ3.id) {
                q3 = "No"
            }
        }
        binding.radiosQ4.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ4.id) {
                q4 = "yes"
            } else if (radioBtnID == binding.NoQ4.id) {
                q4 = "No"
            }
        }
        binding.radiosQ5.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ5.id) {
                q5 = "yes"
            } else if (radioBtnID == binding.NoQ5.id) {
                q5 = "No"
            }
        }
        binding.radiosQ6.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ6.id) {
                q6 = "yes"
            } else if (radioBtnID == binding.NoQ6.id) {
                q6 = "No"
            }
        }
        binding.radiosQ7.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ7.id) {
                q7 = "yes"
            } else if (radioBtnID == binding.NoQ7.id) {
                q7 = "No"
            }
        }
        binding.radiosQ8.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ8.id) {
                q8 = "yes"
            } else if (radioBtnID == binding.NoQ8.id) {
                q8 = "No"
            }
        }
        binding.radiosQ9.setOnCheckedChangeListener { rGroup, _ ->
            val radioBtnID = rGroup.checkedRadioButtonId
            if (radioBtnID == binding.yesQ9.id) {
                q9 = "yes"
            } else if (radioBtnID == binding.NoQ9.id) {
                q9 = "No"
            }
        }

        binding.btnLogin.setOnClickListener {
            hashMap[binding.q1.text.toString()] = q1
            hashMap[binding.q2.text.toString()] = q2
            hashMap[binding.q3.text.toString()] = q3
            hashMap[binding.q4.text.toString()] = q4
            hashMap[binding.q5.text.toString()] = q5
            hashMap[binding.q6.text.toString()] = q6
            hashMap[binding.q7.text.toString()] = q7
            hashMap[binding.q8.text.toString()] = q8
            hashMap[binding.q9.text.toString()] = q9
            Log.e("hashmabdone", "onViewCreated: $hashMap", )
            formViewModel.form(hashMap)
            formViewModel.formFlow.onEach {
                when(it){
                    is Resource.Success ->{

                        val nav=NavOptions.Builder()
                            .setPopUpTo(R.id.formFragment, true)
                            .build()

                        findNavController().navigate(FormFragmentDirections.actionFormFragmentToResultDialogFragment(it.data!!.percentage.toFloat()), navOptions = nav)

                    }
                    is Resource.Loading ->{

                    }
                    is Resource.Error ->{

                    }
                }
            }.launchIn(lifecycleScope)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFormBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}