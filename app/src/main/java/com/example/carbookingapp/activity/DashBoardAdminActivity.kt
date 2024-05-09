package com.example.carbookingapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carbookingapp.model.ModelCategory
import com.example.carbookingapp.adapter.AdapterAdminCategory
import com.example.carbookingapp.databinding.ActivityDashBoardAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashBoardAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashBoardAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var adapterCategory: AdapterAdminCategory

    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardAdminBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadCategories()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterCategory.filter.filter(s)
                } catch (e: Exception) {

                }


            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }


        binding.btCategory.setOnClickListener {
            startActivity(Intent(this, CategoryAddActivity::class.java))
        }


    }

    private fun loadCategories() {
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("UserCategories")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCategory::class.java)
                    categoryArrayList.add(model!!)
                }

                adapterCategory = AdapterAdminCategory(this@DashBoardAdminActivity, categoryArrayList)
                binding.rvCategory.layoutManager = LinearLayoutManager(this@DashBoardAdminActivity)

                binding.rvCategory.adapter = adapterCategory

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun checkUser() {
        /*val email= firebaseUser.email

        binding.tvSubTitle.text= email*/


    }
}