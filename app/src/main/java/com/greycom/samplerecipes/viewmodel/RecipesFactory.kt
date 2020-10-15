package com.greycom.samplerecipes.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipesFactory(val app: Application, private val mRepo: MainRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (RecipesViewModel(app, mRepo) as T)
    }
}