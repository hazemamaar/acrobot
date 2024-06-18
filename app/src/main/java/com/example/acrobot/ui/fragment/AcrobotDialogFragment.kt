package com.example.acrobot.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.acrobot.R
import com.example.acrobot.common.Constant
import com.example.acrobot.databinding.FragmentAcrobotDialogBinding
import com.example.acrobot.ui.activities.AppActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AcrobotDialogFragment : DialogFragment() {
    private var _binding: FragmentAcrobotDialogBinding? = null
    private val binding get()  = _binding!!
    val args : AcrobotDialogFragmentArgs by navArgs()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAcrobotDialogBinding.inflate(LayoutInflater.from(requireContext()))
        return MaterialAlertDialogBuilder(requireContext())
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .setView(binding.root)
            .setCancelable(true)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReadRead.setOnClickListener {
            val intent =Intent(Intent(requireContext(), AppActivity::class.java))
            val b = Bundle()
            b.putBoolean(Constant.bundleApp,true)
            intent.putExtras(b)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val argsValue=args.acromegaly
        Log.e("arcomegaly", "onCreateView: $argsValue", )
        if(argsValue.toDouble()==0.0){
            binding.title.text="Acromegaly"

        }else{
            binding.title.text="No Acromegaly"
            binding.emoji.setImageResource(R.drawable.done_right)
        }
        return binding.root
    }
}