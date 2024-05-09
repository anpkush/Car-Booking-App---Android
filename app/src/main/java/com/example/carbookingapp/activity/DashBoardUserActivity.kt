package com.example.carbookingapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carbookingapp.adapter.AdapterAdminCategory
import com.example.carbookingapp.databinding.ActivityDashBoardUserBinding
import com.example.carbookingapp.model.ModelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashBoardUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashBoardUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var adapterCategory: AdapterAdminCategory
    private lateinit var categoryArrayList: ArrayList<ModelCategory>
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)

        firebaseAuth = FirebaseAuth.getInstance()
        loadCategories()
        loging()
        logout()


    }

    private fun logout() {
        binding.ivLogout.setOnClickListener {
            val editor = sharedPreferences?.edit()
            editor?.clear()
            editor?.apply()

            val intent = Intent(this@DashBoardUserActivity, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@DashBoardUserActivity, "logout", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loging() {
        binding.ivLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
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

                adapterCategory = AdapterAdminCategory(this@DashBoardUserActivity, categoryArrayList)
                binding.rvCategory.layoutManager = LinearLayoutManager(this@DashBoardUserActivity)

                binding.rvCategory.adapter = adapterCategory

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    companion object {
       private const val SHARED_PREF_NAME = "MyPref"
    }

}