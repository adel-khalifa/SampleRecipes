package com.greycom.samplerecipes.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.greycom.samplerecipes.R
import com.greycom.samplerecipes.ui.list.ListActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hideSplashAndGoToNextActivity()
    }


    private fun hideSplashAndGoToNextActivity() {
        Handler().postDelayed({

            startActivity(Intent(this, ListActivity::class.java));
            finish()

        }, 3000L)


    }

}