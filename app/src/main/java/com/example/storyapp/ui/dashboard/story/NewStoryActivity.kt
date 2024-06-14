package com.example.storyapp.ui.dashboard.story

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.data.repository.remote.ResultState
import com.example.storyapp.data.viewmodel.ViewModelFactory
import com.example.storyapp.databinding.ActivityNewStoryBinding
import com.example.storyapp.ui.dashboard.home.HomeFragment
import com.example.storyapp.utils.Helper
import java.io.File

class NewStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewStoryBinding
//    private lateinit var newStoryViewModel: NewStoryViewModel
//    private var userToken: String? = null
    private lateinit var progressDialog: ProgressDialog

    private val viewModel by viewModels<NewStoryViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val factory = ViewModelFactory.getInstance(this)
//        newStoryViewModel = ViewModelProvider(this, factory)[NewStoryViewModel::class.java]

        progressDialog = ProgressDialog(this).apply {
            setMessage("Uploading story...")
            setCancelable(false)
        }

        val myFile = intent?.getSerializableExtra(EXTRA_PHOTO_RESULT) as File
        val isBackCamera = intent?.getBooleanExtra(EXTRA_CAMERA_MODE, true) as Boolean
        val rotatedBitmap = Helper.rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
        binding.storyImage.setImageBitmap(rotatedBitmap)
        binding.btnUpload.setOnClickListener {
            if (binding.storyDescription.text.isNotEmpty()) {
                uploadImage(myFile, binding.storyDescription.text.toString())
            } else {
                Helper.showDialogInfo(this, getString(R.string.UI_validation_empty_story_description))
            }
        }

//        lifecycleScope.launch(Dispatchers.Main) {
//            newStoryViewModel.getSession().observe(this@NewStoryActivity) { user ->
//                user?.let {
//                    userToken = "Bearer ${it.token}"
//                    Log.d("NewStoryActivity", "Token: $userToken")
//                }
//            }
//        }
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadImage(image: File, description: String) {
        viewModel.getSession().observe(this) { user ->
            val token = user.token
//            if (token != null) {
                viewModel.uploadNewStory(image, description, token).observe(this) { result ->
                if(result != null) {
                    when (result) {
                        is ResultState.Loading -> progressDialog.show()
                        is ResultState.Success -> {
                            progressDialog.dismiss()
//                            Helper.showDialogInfo(this, getString(R.string.API_success_upload_image))
                            navigateToHomeFragment()
                        }
                        is ResultState.Error -> {
                            progressDialog.dismiss()
                            Helper.showDialogInfo(this, result.error)
                        }
                        else -> {}
                    }

                }
            }
        }
    }

    companion object {
        const val EXTRA_PHOTO_RESULT = "PHOTO_RESULT"
        const val EXTRA_CAMERA_MODE = "CAMERA_MODE"
    }

    private fun navigateToHomeFragment() {
        val intent = Intent(this, HomeFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
