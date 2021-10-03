package com.app.cementfactory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.cementfactory.R
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        loadSplash()

    }

    private fun loadSplash() {
        //load splash screen
        CoroutineScope(Dispatchers.IO).launch {
            delay(5000)
            val spf = getSharedPreferences(LoginActivity.PREF_NAME, MODE_PRIVATE)
            val hasLoggedIn: Boolean = spf.getBoolean("hasLoggedIn", false)

            withContext(Dispatchers.Main) {
                if (hasLoggedIn) {
                    startActivity(Intent(this@SplashActivity, WorkerDashboardActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()

                }
            }

        }
    }
}