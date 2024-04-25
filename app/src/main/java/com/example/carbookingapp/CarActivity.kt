package com.example.carbookingapp

import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carbookingapp.databinding.ActivityCarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    private var carUri : Uri?= null

    private val TAG= "CAR_ADD_TAG"

    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        loadCarCategories()

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.tvCategory.setOnClickListener{
            categoryPickDialog()
        }

        //When we design a attach button
        //binding.attachpdfbtn{ carPickIntent}

    }

    private fun loadCarCategories() {
        Log.d(TAG, "loadCarCategories: Loading Car Categories")

        categoryArrayList= ArrayList()

        var ref= FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                categoryArrayList.clear()
                for (ds in snapshot.children){
                    val model= ds.getValue(ModelCategory::class.java)

                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.category}")
                }
            }

            override fun onCancelled(error: DatabaseError){

            }
        })
    }
    private var selectedCategoryTitle=""
    private var selectedCategoryId=""

    private fun categoryPickDialog(){
        Log.d(TAG, "categoryPickDialog: Showing car category pic dialog")

        val categoriesArray= arrayOfNulls<String>(categoryArrayList.size)

        for (i in categoryArrayList.indices){
            categoriesArray[i]= categoryArrayList[i].category
        }

        val builder= AlertDialog.Builder(this)
        builder.setTitle("Pic Category")
            .setItems(categoriesArray){dialog, which->
               selectedCategoryTitle= categoryArrayList[which].category
               selectedCategoryId= categoryArrayList[which].id

                binding.tvCategory.text= selectedCategoryTitle
                Log.d(TAG, "categoryPickDialog: Selected Category ID:$selectedCategoryId")
                Log.d(TAG, "categoryPickDialog: Selected Category Title:$selectedCategoryTitle")

            }
            .show()
    }
    private fun carPickIntent(){
        Log.d(TAG, "carPickIntent: starting car pick intent")

        val intent= Intent()
        intent.type= "application/car"
        intent.action= Intent.ACTION_GET_CONTENT
        carActivityResultLauncher.launch(intent)
    }

    val carActivityResultLauncher= registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode== RESULT_OK){
                Log.d(TAG, "Car Picked: ")
                carUri = result.data!!.data
            }else{
                Log.d(TAG, "Car Pick Cancelled: ")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }

        }
    )
}