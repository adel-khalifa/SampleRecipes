package com.greycom.samplerecipes.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.greycom.samplerecipes.R
import com.greycom.samplerecipes.application.RecipeApplication
import com.greycom.samplerecipes.network.NetworkState
import com.greycom.samplerecipes.network.NetworkState.OnFailure
import com.greycom.samplerecipes.network.NetworkState.OnSuccess
import com.greycom.samplerecipes.pojo.RecipesResponse
import kotlinx.coroutines.launch
import java.io.IOException

class RecipesViewModel(val app: Application, private val mRepo: MainRepo) : AndroidViewModel(app) {
    init {
        requestHistoryData()
    }

    var recipesListLiveData: MutableLiveData<NetworkState<RecipesResponse>> = MutableLiveData()

    private fun requestHistoryData() = viewModelScope.launch {
        if (isConnected()) {

            recipesListLiveData.value = NetworkState.OnLoading()
            try {


                val responseResult = MainRepo.recipesRequest()
                if (responseResult.isSuccessful) {
                    recipesListLiveData.postValue(OnSuccess(responseResult.body()!!))
                } else {
                    recipesListLiveData.postValue(OnFailure(responseResult.message()))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> handleConnectionFailed()
                }

            }

        }// if no internet connection
        else handleNoInternetConnection()

    }

    private fun handleConnectionFailed() {
        val connectionFailedMessage =
            getApplication<RecipeApplication>().getString(R.string.connection_failed)
        recipesListLiveData.postValue(OnFailure(connectionFailedMessage))
    }

    private fun handleNoInternetConnection() {
        val noInternetConnectionMessage =
            getApplication<RecipeApplication>().getString(R.string.no_internet_connection)
        recipesListLiveData.postValue(OnFailure(noInternetConnectionMessage))
    }


    private fun isConnected(): Boolean {
        val connectivityManager = getApplication<RecipeApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}