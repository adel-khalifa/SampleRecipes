package com.greycom.samplerecipes.viewmodel

import com.greycom.samplerecipes.network.RetrofitInstance

object MainRepo {

    suspend
    fun recipesRequest() = RetrofitInstance.mApi.getRecipesRequest()
}
