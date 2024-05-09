package com.example.carbookingapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carbookingapp.R

class SplashActivity : AppCompatActivity() {

    var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)

        val stringName = sharedPreferences?.getString(KEY_NAME, null)
        val stringNumber = sharedPreferences?.getString(KEY_NUMBER, null)
        val stringUser = sharedPreferences?.getString(USER_TYPE, null)

        Handler(Looper.getMainLooper()).postDelayed({
            if (stringName != null  && stringNumber != null) {
                val intent = Intent(this, DashBoardUserActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000) // 1 sec

    }

    companion object {
        private const val SHARED_PREF_NAME = "MyPref"
        private const val KEY_NAME = "name"
        private const val KEY_NUMBER = "number"
        private const val USER_TYPE = "user_type"
    }


}