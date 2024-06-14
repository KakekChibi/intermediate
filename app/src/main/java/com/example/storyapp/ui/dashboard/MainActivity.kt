package com.example.storyapp.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.data.viewmodel.ViewModelFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.dashboard.home.HomeAdapter
import com.example.storyapp.ui.dashboard.home.HomeFragment
import com.example.storyapp.ui.dashboard.profile.ProfileFragment
import com.example.storyapp.ui.dashboard.story.CameraActivity
import com.example.storyapp.utils.Helper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fragmentProfile = ProfileFragment()
    private val fragmentHome = HomeFragment()
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: HomeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.background = null

        switchFragment(fragmentHome)

//        setupRecyclerView()
        adapter = HomeAdapter()
        observeUserSession()
        observeStories()


        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> switchFragment(fragmentHome)
                R.id.navigation_profile -> switchFragment(fragmentProfile)
            }
            true
        }

        binding.fab.setOnClickListener {
            routeToStory()
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun routeToStory() {
        if (!allPermissionsGranted()) {
            requestPermission()
        } else {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

//    private fun setupRecyclerView() {
//        adapter = HomeAdapter()
//        adapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: Story, sharedImageView: View) {
//            }
//        })
//    }


    private fun observeUserSession() {
        lifecycleScope.launch {
            viewModel.fetchUserSession()
        }
    }

    private fun observeStories() {
        viewModel.stories.observe(this) { pagingData ->
            lifecycleScope.launch {
                adapter.submitData(pagingData)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeStories()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Helper.showDialogInfo(
                    this,
                    getString(R.string.UI_error_permission_denied),
                    Gravity.LEFT
                )
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
