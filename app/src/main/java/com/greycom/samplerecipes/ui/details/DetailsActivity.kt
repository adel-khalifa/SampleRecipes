package com.greycom.samplerecipes.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.greycom.samplerecipes.R
import com.greycom.samplerecipes.pojo.RecipesResponse.RecipesResponseItem
import com.greycom.samplerecipes.utils.Constants.EXTRA_RECIPE
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var  recipeItem : RecipesResponseItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        recipeItem = intent.getSerializableExtra(EXTRA_RECIPE) as RecipesResponseItem
        loadRecipeDetails(recipeItem)
    }

    private fun loadRecipeDetails(recipeItem: RecipesResponseItem) {
        recipe_details_item_tv_name.text=recipeItem.name
        recipe_details_item_tv_headline.text=recipeItem.headline
        recipe_details_tv_calories_amount.text=recipeItem.calories
        recipe_details_tv_carobs_amount.text=recipeItem.carbos
        recipe_details_tv_country.text=recipeItem.country
        recipe_details_tv_fat_amount.text=recipeItem.fats
        recipe_details_tv_proteins_amount.text=recipeItem.proteins
        recipe_details_tv_description.text=recipeItem.description
        Glide.with(this).load(recipeItem.image).into(recipe_details_item_iv_recipe)    }
}