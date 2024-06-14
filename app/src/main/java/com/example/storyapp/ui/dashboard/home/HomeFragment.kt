package com.example.storyapp.ui.dashboard.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.storyapp.R
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.viewmodel.ViewModelFactory
import com.example.storyapp.databinding.FragmentHomeBinding
import com.example.storyapp.ui.dashboard.MainViewModel
import com.example.storyapp.ui.maps.MapsActivity
import com.example.storyapp.utils.Constanta
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentHomeBinding
    private val rvAdapter = HomeAdapter()
    private lateinit var viewModel: MainViewModel
    private lateinit var userRepository: UserRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()

        userRepository = Constanta.provideUserRepository(context)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
            isNestedScrollingEnabled = false
        }

        binding.swipeRefresh.setOnRefreshListener(this)

        observeData()
    }

    private fun observeData() {
        viewModel.stories.observe(viewLifecycleOwner) { pagingData ->
            Log.d("HomeFragment", "PagingData received: $pagingData")
            lifecycleScope.launch {
                rvAdapter.submitData(pagingData)
                Log.d("HomeFragment", "Data submitted to adapter")
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = true
        viewModel.stories.observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                rvAdapter.submitData(pagingData)
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_main_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_map -> {
                navigateToMapsActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToMapsActivity() {
        val intent = Intent(requireContext(), MapsActivity::class.java)
        startActivity(intent)
    }

}
