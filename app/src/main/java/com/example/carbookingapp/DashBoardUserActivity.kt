package com.example.carbookingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carbookingapp.databinding.ActivityDashBoardUserBinding
import com.google.firebase.auth.FirebaseAuth

class DashBoardUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashBoardUserBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding= ActivityDashBoardUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()

        binding.ivLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun checkUser() {
        val firebaseUser= firebaseAuth.currentUser

        if (firebaseUser== null){
            binding.tvSubTitle.text= "Not Logged In..."
        }else{

            val email= firebaseUser.email

            binding.tvSubTitle.text= email


        }
    }
}