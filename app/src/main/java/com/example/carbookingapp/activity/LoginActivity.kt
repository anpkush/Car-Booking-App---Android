@file:Suppress("DEPRECATION")

package com.example.carbookingapp.activity

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carbookingapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.tvRegistration.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))


        }

        binding.btLogin.setOnClickListener {

            if (TextUtils.isEmpty(binding.emailEt.text.toString()) && TextUtils.isEmpty(binding.passwordet.text.toString())) {
                Toast.makeText(this, "All fields are Required", Toast.LENGTH_SHORT).show()
            } else {
                validateData()
            }
        }

    }

    private var email = ""
    private var password = ""


    private fun validateData() {
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordet.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "All required fields", Toast.LENGTH_SHORT).show()
        } else {

            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
            val editor = sharedPreferences?.edit()

            editor?.putString(KEY_NAME, binding.emailEt.text.toString())
            editor?.putString(KEY_NUMBER, binding.passwordet.text.toString())
            editor?.apply()

            loginUser()
        }

    }

    private fun loginUser() {
        progressDialog.setMessage("Logging In...")

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            checkUser()
        }.addOnFailureListener { e ->
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }

    private fun checkUser() {
        progressDialog.setMessage("Checking User...")

        firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("User")

        ref.child(firebaseAuth.uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                progressDialog.dismiss()

                val userType = snapshot.child("userType").value

                if (userType == "User") {
                    startActivity(Intent(this@LoginActivity, DashBoardUserActivity::class.java))
                    finish()

                } else if (userType == "Admin") {

                    startActivity(
                        Intent(this@LoginActivity, DashBoardAdminActivity::class.java)
                    )
                    finish()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    companion object {
        private const val SHARED_PREF_NAME = "MyPref"
        private const val KEY_NAME = "name"
        private const val KEY_NUMBER = "number"
    }
}