package com.internshala.flash.data
import com.internshala.flash.R

object DataSource {
    fun loadCategories(): List<Categories> {
        return listOf(
            Categories(R.string.fruits, R.drawable.fruitsmain),
            Categories(R.string.vegetables, R.drawable.veggiesmain),
            Categories(R.string.snacks, R.drawable.snacksmain),
            Categories(R.string.spices, R.drawable.spicesmain),
            Categories(R.string.bread_bakery, R.drawable.breadbakerymain),
            Categories(R.string.beverages, R.drawable.beverages),
        )
    }

}