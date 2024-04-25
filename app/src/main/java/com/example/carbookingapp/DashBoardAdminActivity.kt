package com.example.carbookingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ColorSpace.Model
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.carbookingapp.databinding.ActivityDashBoardAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class DashBoardAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashBoardAdminBinding
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var adapterCategory: AdapterCategory

    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashBoardAdminBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()
        loadCategories()

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterCategory.filter.filter(s)
                }catch (e: Exception){

                }


            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btLogout.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()

        }


            binding.btCategory.setOnClickListener{
                startActivity(Intent(this,CategoryAddActivity::class.java))
            }


    }

    private fun loadCategories() {
        categoryArrayList= ArrayList()

        val ref= FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (ds in snapshot.children){
                    val model= ds.getValue(ModelCategory::class.java)

                    categoryArrayList.add(model!!)
                }

                adapterCategory = AdapterCategory(this@DashBoardAdminActivity,categoryArrayList)
                binding.rvCategory.layoutManager= LinearLayoutManager(this@DashBoardAdminActivity)

                binding.rvCategory.adapter= adapterCategory

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