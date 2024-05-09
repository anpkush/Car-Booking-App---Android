package com.example.carbookingapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
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

    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardAdminBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        loadCategories()
        logout()

        sharedPreferences = getSharedPreferences(SHARED_PREF_ADMIN, MODE_PRIVATE)

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


        binding.btCategory.setOnClickListener {
            startActivity(Intent(this, CategoryAddActivity::class.java))
        }


    }

    private fun logout() {

        binding.ivLogout.setOnClickListener {
            val editor = sharedPreferences?.edit()
            editor?.clear()
            editor?.apply()

            val intent = Intent(this@DashBoardAdminActivity, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@DashBoardAdminActivity, "logout", Toast.LENGTH_SHORT).show()
            finish()
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

    companion object {
        private const val SHARED_PREF_ADMIN = "MyPref"
    }


}