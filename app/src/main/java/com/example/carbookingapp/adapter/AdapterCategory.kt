package com.example.carbookingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.carbookingapp.model.ModelCategory
import com.example.carbookingapp.databinding.RowCategoryBinding
import com.google.firebase.database.FirebaseDatabase

class AdapterCategory : RecyclerView.Adapter<AdapterCategory.HolderCategory>, Filterable {

    private val context: Context
    var categoryArrayList: ArrayList<ModelCategory>
    private var filterList: ArrayList<ModelCategory>
    private var filter: FilterCategory? = null

    private lateinit var binding: RowCategoryBinding

    constructor(context: Context, categoryArrayList: ArrayList<ModelCategory>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategory(binding.root)

    }


    override fun onBindViewHolder(holder: HolderCategory, position: Int) {

        val model = categoryArrayList[position]


//        Change here Comment Out
        holder.tvcCategory.text = model.category
        holder.tvMobile.text = model.mobileNumber


//        holder.btDelete.setOnClickListener() {
//            val builder = AlertDialog.Builder(context)
//            builder.setTitle("Delete")
//                .setMessage("Are sure you want to delete this category?")
//                .setPositiveButton("Confirm") { a, d ->
//                    Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
//                    deleteCategory(model, holder)
//                }
//                .setNegativeButton("Cancel") { a, d ->
//                    a.dismiss()
//
//                }
//                .show()
//        }

    }

    private fun deleteCategory(model: ModelCategory, holder: HolderCategory) {
        val id = model.id
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener() {
                Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener() { e ->
                Toast.makeText(
                    context,
                    "Unable due to delete due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }


    inner class HolderCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvcCategory: TextView = binding.tvCarName
        var tvMobile: TextView = binding.tvMobile

//        var btDelete: ImageButton = binding.btDelete
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterCategory(filterList, this)
        }
        return filter as FilterCategory
    }


}
