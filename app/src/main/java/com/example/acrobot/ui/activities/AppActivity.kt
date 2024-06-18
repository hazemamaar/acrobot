package com.example.acrobot.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.acrobot.R
import com.example.acrobot.common.Constant
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        val bundle = intent.extras

        val title = bundle!!.getBoolean(Constant.bundleApp, false)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.app_nav)
        Log.e("cccc", "onCreate: $title", )
        if (title){
            navController.graph=graph.apply {
                setStartDestination(R.id.timeLineFragment)
            }
        }
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        bottomNavigationView.background = null
    }
}