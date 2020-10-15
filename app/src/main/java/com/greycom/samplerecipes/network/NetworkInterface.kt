package com.greycom.samplerecipes.network

import com.greycom.samplerecipes.pojo.RecipesResponse
import retrofit2.Response
import retrofit2.http.*

     interface NetworkInterface {
//  https://hf-android-app.s3-eu-west-1.amazonaws.com/androidtest/recipes.json

@GET("/androidtest/recipes.json")
   suspend  fun getRecipesRequest(): Response<RecipesResponse>

}