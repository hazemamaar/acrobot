package com.example.acrobot.ui.fragment

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

    private lateinit var _binding : FragmentWelcomeBinding
    val binding get() = _binding

    @Inject
    lateinit var onBoardAdapter : OnBoardAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initIndicator()
        onBoardAdapter.onBoardList =welcomeData()
        binding.viewpagerOnboard.adapter= onBoardAdapter

        binding.finishBtn.setOnClickListener {
            if (binding.finishBtn.text.equals("متابعة")){
                binding.viewpagerOnboard.setCurrentItem(binding.viewpagerOnboard.currentItem+1)
            }else{
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }
        }
        binding.viewpagerOnboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) {
                   binding.skipBtn.visibility= View.GONE
                    binding.finishBtn.text="ابدأ الان"
                } else {
                    binding.skipBtn.visibility= View.VISIBLE
                    binding.finishBtn.text="متابعة"
                }
                binding.indicator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    fun initIndicator() {
        binding.indicator.setSliderColor(R.color.blue, R.color.blue)
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
                "تعرف على حالتك الصحيه",
                R.drawable.photo1,
                "بالنسبه لك ولاشخاص المقربين لك"
            ),
            WelcomeModel(
                "من خلال الذكاء الاصطناعي ",
                R.drawable.photo2,
                "تعرف ان كنت مصاب  ام لا  "
            ),)
    }
}