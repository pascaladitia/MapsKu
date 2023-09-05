package com.pascal.mapsku.view.user

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.pascal.mapsku.R
import com.pascal.mapsku.databinding.ActivityUserBinding
import com.pascal.mapsku.service.MyBackroundService

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.nav_host_homeUser)
        NavigationUI.setupWithNavController(binding.bottomNavigationUser, navController)

        initView()
    }

    private fun initView() {
        val serviceIntent = Intent(this, MyBackroundService::class.java)
        startService(serviceIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ), 1
            )
        }
    }
}