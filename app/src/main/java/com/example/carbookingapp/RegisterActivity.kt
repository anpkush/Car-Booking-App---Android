@file:Suppress("DEPRECATION")

package com.example.carbookingapp


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carbookingapp.databinding.ActivityRegisterBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase




class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(
            this
        )

        firebaseAuth= FirebaseAuth.getInstance()


        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btBack.setOnClickListener{
            onBackPressed()

        }

        binding.btRegister.setOnClickListener{
            if(TextUtils.isEmpty(binding.etName.text.toString()) && TextUtils.isEmpty(binding.etEmail.text.toString())
                && TextUtils.isEmpty(binding.etPassword.text.toString()) && TextUtils.isEmpty(binding.etRePassword.text.toString())) {
                makeText(this, "All fields required", LENGTH_SHORT).show()

            }else{
                createUserAccount()
            }
        }
// Open Login Screen

        binding.tvLogin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }

    private fun createUserAccount() {
        progressDialog.setMessage("Creating Message...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString()).addOnSuccessListener {
            updateUserInfo()

        }.addOnFailureListener{e->
            progressDialog.dismiss()
            makeText(this,e.message, LENGTH_SHORT).show()

        }
    }

    private fun updateUserInfo() {
        progressDialog.setMessage("Saving user Info...")

        val timestamp= System.currentTimeMillis()
        val uid= firebaseAuth.uid
        val hashMap: HashMap<String,Any?> = HashMap()
        hashMap["uid"]= uid
        hashMap["email"]= binding.etEmail.text.toString()
        hashMap["name"]= binding.etName.text.toString()
        /*hashMap["profile image"]= " "*/
        hashMap["userType"]=binding.etUserType.text.toString()
        hashMap["timestamp"]= timestamp

        val ref= FirebaseDatabase.getInstance().getReference("User")

        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener{
                progressDialog.dismiss()
                makeText(this,"Account Created...", LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }

            .addOnFailureListener{ e->
                progressDialog.dismiss()
                makeText(this,"Failed creating account Due to ${e.message}", LENGTH_SHORT).show()

            }

    }



}