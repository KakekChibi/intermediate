package com.example.storyapp.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.data.repository.remote.UserModel
import com.example.storyapp.data.viewmodel.ViewModelFactory
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.dashboard.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnAction.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password,
                    onSuccess = { user ->
                        saveSession(user)
                        showSuccessDialog()
                    },
                    onError = { errorMessage ->
                        showErrorDialog(errorMessage)
                    }
                )
            } else {
                showErrorDialog(getString(R.string.login_failed_message))
            }
        }

        binding.btnRegister.setOnClickListener {
            // Assuming RegisterActivity is another Activity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveSession(user: UserModel) {
        lifecycleScope.launch {
            viewModel.saveSession(user)
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.login_successful))
            .setMessage(getString(R.string.login_successful_message))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.login_failed))
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
