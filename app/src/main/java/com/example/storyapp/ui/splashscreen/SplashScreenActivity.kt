package com.example.storyapp.ui.splashscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.data.repository.remote.UserPreference
import com.example.storyapp.databinding.ActivitySplashScreenBinding
import com.example.storyapp.ui.auth.LoginActivity
import com.example.storyapp.ui.dashboard.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        userPreference = UserPreference.getInstance(dataStore)


        lifecycleScope.launch {
            val user = userPreference.getUser().first()
            val targetActivity = if (user.token.isEmpty()) {
                LoginActivity::class.java
            } else {
                MainActivity::class.java
            }

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashScreenActivity, targetActivity))
                finish()
            }, 2000)
        }
    }
}
