package com.example.acrobot.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.acrobot.R
import com.example.acrobot.common.Constant
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val bundle = intent.extras

            val title = bundle!!.getBoolean(Constant.bundleApp, false)
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.frag_host) as NavHostFragment
            val navController = navHostFragment.navController
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.welcome_nav)
            Log.e("cccc", "onCreate: $title", )
            if (title){
                navController.graph=graph.apply {
                    setStartDestination(R.id.loginFragment)
                }
            }
        }catch (e:Exception){

        }

    }
}