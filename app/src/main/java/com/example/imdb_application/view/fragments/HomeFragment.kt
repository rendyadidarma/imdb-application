package com.example.imdb_application.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.imdb_application.R
import com.example.imdb_application.data.sealed.StateLoad
import com.example.imdb_application.data.utils.BindUtils
import com.example.imdb_application.data.utils.Router
import com.example.imdb_application.databinding.FragmentHomeBinding
import com.example.imdb_application.view.MainActivity
import com.example.imdb_application.view.adapter.MovieListAdapter
import com.example.imdb_application.view.adapter.MovieListener
import com.example.imdb_application.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    val viewModel by viewModels<HomeViewModel>()

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val verticalDecorator = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val horizontalDecorator = DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL)

        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider_recycler, null)

        if (drawable != null) {
            verticalDecorator.setDrawable(drawable)
            horizontalDecorator.setDrawable(drawable)
            binding.homeRecyclerView.addItemDecoration(verticalDecorator)
            binding.homeRecyclerView.addItemDecoration(horizontalDecorator)
        }

        binding.homeRecyclerView.adapter = MovieListAdapter(
            MovieListener { movie ->
                Router.routeHomeFragmentToDetailFragment(movie, findNavController())
                getCurrentActivity()?.getBottomNavView()?.visibility = View.GONE
            }
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshDataListMovie()
            swipe_refresh.isRefreshing = false
        }
        bindObservables()

        viewModel.refreshDataListMovie()
    }

    private fun bindObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.movieList.collectLatest {
                        val data = it.value

                        when(it.state) {
                            StateLoad.Error -> {
                                binding.emptyState.visibility = View.VISIBLE
                                binding.homeRecyclerView.visibility = View.GONE
                                binding.shimmerFrameLayout.visibility = View.GONE
                                binding.shimmerFrameLayout.stopShimmer()
                            }

                            StateLoad.Loading -> {
                                binding.emptyState.visibility = View.GONE
                                binding.homeRecyclerView.visibility = View.GONE
                                binding.shimmerFrameLayout.visibility = View.VISIBLE
                                binding.shimmerFrameLayout.startShimmer()
                            }

                            StateLoad.Success -> {
                                BindUtils.bindRecyclerView(binding.homeRecyclerView, data)
                                binding.emptyState.visibility = View.GONE
                                binding.homeRecyclerView.visibility = View.VISIBLE
                                binding.shimmerFrameLayout.visibility = View.GONE
                                binding.shimmerFrameLayout.stopShimmer()
                            }
                        }

                    }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Log.d("FLAG", "onCreateView")
        getCurrentActivity()?.getBottomNavView()?.visibility = View.VISIBLE

        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }



    private fun getCurrentActivity(): MainActivity? {
        return (activity as? MainActivity)
    }

    override fun onStart() {
        super.onStart()
        binding.shimmerFrameLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerFrameLayout.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}