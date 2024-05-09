package com.example.carbookingapp.adapter

import android.widget.Filter
import com.example.carbookingapp.model.ModelCategory

class FilterCategory : Filter {

    private var filterList: ArrayList<ModelCategory>

    private var adapterCategory: AdapterAdminCategory

    constructor(filterList: ArrayList<ModelCategory>, adapterCategory: AdapterAdminCategory) : super() {
        this.filterList = filterList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
       var constraint= constraint
        val results= FilterResults()
        if (constraint!= null && constraint.isEmpty()){

            constraint= constraint.toString().uppercase()
            val filterModels:ArrayList<ModelCategory> = ArrayList()

            for (i in 0 until filterList.size){
                if (filterList[i].category.uppercase().contains(constraint)){

                    filterModels.add(filterList[i])
                    results.values= filterModels


                }
            }

            results.count= filterModels.size

        }
        else
        {
            results.count= filterList.size
            results.values= filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterCategory.categoryArrayList= results.values as ArrayList<ModelCategory>

        adapterCategory.notifyDataSetChanged()
    }
}