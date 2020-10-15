package com.greycom.samplerecipes.network
import com.greycom.samplerecipes.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    // singleton object
    companion object {
        // using lazy to init it just when needed
        private val retrofit by lazy {

            // add logging interceptor to help in debugging
            val client = OkHttpClient
                .Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            // create and return an object of Retrofit immediately by lazy
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }


        // using lazy to init it just when needed
        // interface object to access APIs methods
        val mApi: NetworkInterface by lazy { retrofit.create(NetworkInterface::class.java) }


    }
}