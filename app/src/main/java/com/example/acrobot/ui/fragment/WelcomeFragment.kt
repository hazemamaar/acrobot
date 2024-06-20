package com.example.acrobot.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.acrobot.R
import com.example.acrobot.data.WelcomeModel
import com.example.acrobot.databinding.FragmentWelcomeBinding
import com.example.acrobot.ui.OnBoardAdapter
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private lateinit var _binding : FragmentWelcomeBinding
    val binding get() = _binding

    @Inject
    lateinit var onBoardAdapter : OnBoardAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        sharedPreferencesEditor=sharedPreferences.edit()
        initIndicator()
        onBoardAdapter.onBoardList =welcomeData()
        binding.viewpagerOnboard.adapter= onBoardAdapter

        binding.finishBtn.setOnClickListener {
            if (binding.viewpagerOnboard.currentItem<1){
                binding.viewpagerOnboard.currentItem = binding.viewpagerOnboard.currentItem+1
            }else{
                sharedPreferencesEditor.putBoolean("isVisited",true)
                    .apply()
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }
        }
        binding.viewpagerOnboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positioNoffset: Float,
                positioNoffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positioNoffset, positioNoffsetPixels)
                binding.indicator.onPageScrolled(position, positioNoffset, positioNoffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.indicator.onPageSelected(position)
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    fun initIndicator() {
        binding.indicator.setSliderColor(R.color.green, R.color.green)
        binding.indicator.setSliderWidth(40f)
        binding.indicator.setSliderHeight(10f)
        binding.indicator.setSlideMode(IndicatorSlideMode.WORM)
        binding.indicator.setIndicatorStyle(IndicatorStyle.ROUND_RECT)
        binding.indicator.setPageSize(2)
        binding.indicator.notifyDataChanged()
    }

    private fun welcomeData():List<WelcomeModel>{
        return listOf(
            WelcomeModel(
                "Know your health condition",
                R.drawable.photo1,
                "For you and the people close to you"
            ),
            WelcomeModel(
                "Through artificial intelligence ",
                R.drawable.photo2,
                "KNow whether you are infected or Not through detection using this application"
            ),)
    }
}