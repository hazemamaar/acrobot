package com.example.acrobot.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.acrobot.databinding.ActivityUploadImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadImageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadImageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}

