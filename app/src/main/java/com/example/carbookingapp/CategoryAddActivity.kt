package com.example.carbookingapp

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carbookingapp.databinding.ActivityCategoryAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class CategoryAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryAddBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.ivBack.setOnClickListener(){
            onBackPressed()
        }
        binding.btSubmit.setOnClickListener{
            addCategoryFirebase()
        }

    }


    private fun addCategoryFirebase() {
        progressDialog.show()

        val timestamp= System.currentTimeMillis()

        val hashMap= HashMap<String,Any>()
        hashMap["category"]= binding.etCategory.text.toString()
        hashMap["carOwnerName"]= binding.etName.text.toString()
        hashMap["mobileNumber"]= binding.etMobileNumber.text.toString()

        hashMap["uid"]= "${firebaseAuth.uid}"


        val ref= FirebaseDatabase.getInstance().getReference("UserCategories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener(){
                progressDialog.dismiss()
                Toast.makeText(this,"Added Successful",Toast.LENGTH_SHORT).show()
                finish()
            }

            .addOnFailureListener(){e->
                progressDialog.dismiss()
                Toast.makeText(this,"Add failed due to ${e.message}",Toast.LENGTH_SHORT)
            }
    }



}