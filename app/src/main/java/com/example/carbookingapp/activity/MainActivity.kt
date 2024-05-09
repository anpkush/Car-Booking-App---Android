package com.example.carbookingapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carbookingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btSkip.setOnClickListener{
            startActivity(Intent(this, DashBoardUserActivity::class.java))
        }


    }
}