package com.greycom.samplerecipes.pojo


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RecipesResponse : ArrayList<RecipesResponse.RecipesResponseItem>(){
    data class RecipesResponseItem(
        val calories: String,
        val carbos: String,
        val country: String,
        val description: String,
        val difficulty: Int,
        val fats: String,
        val headline: String,
        val id: String,
        val image: String,
        val name: String,
        val proteins: String,
        val thumb: String,
        val time: String
    ) :Serializable

}