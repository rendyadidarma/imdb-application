package com.example.imdb_application.view.fragments

import android.os.Bundle
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
import com.example.imdb_application.data.utils.BindUtils
import com.example.imdb_application.data.utils.Router
import com.example.imdb_application.databinding.FragmentHomeBinding
import com.example.imdb_application.view.MainActivity
import com.example.imdb_application.view.adapter.MovieListAdapter
import com.example.imdb_application.view.adapter.MovieListener
import com.example.imdb_application.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    val viewModel by viewModels<HomeViewModel>()


    private var binding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservables()
    }

    private fun bindObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.movieList.collect {
                        BindUtils.bindRecyclerView(binding!!.homeRecyclerView, it)
                    }
                }

                launch {
                    viewModel.alreadyHasData.collect{
                        BindUtils.bindShimmer(binding!!.shimmerFrameLayout, it, null, null, binding!!.emptyState, viewModel.dbEmpty.first())
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
        getCurrentActivity()?.getBottomNavView()?.visibility = View.VISIBLE
        binding = FragmentHomeBinding.inflate(inflater)


        val verticalDecorator = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val horizontalDecorator = DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL)

        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider_recycler, null)

        if (drawable != null) {
            verticalDecorator.setDrawable(drawable)
            horizontalDecorator.setDrawable(drawable)
            binding!!.homeRecyclerView.addItemDecoration(verticalDecorator)
            binding!!.homeRecyclerView.addItemDecoration(horizontalDecorator)
        }

        binding!!.homeRecyclerView.adapter = MovieListAdapter(
            MovieListener { movie ->

                viewModel.insertDetailToRoom(movie.id)
                Router.routeHomeFragmentToDetailFragment(movie, findNavController())
                getCurrentActivity()?.getBottomNavView()?.visibility = View.GONE

            }
        )

        binding!!.swipeRefresh.setOnRefreshListener {
            viewModel.refreshDataFromRepo()
            swipe_refresh.isRefreshing = false
        }

        return binding!!.root
    }

    private fun getCurrentActivity(): MainActivity? {
        return (activity as? MainActivity)
    }

    override fun onStart() {
        super.onStart()
        binding!!.shimmerFrameLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding!!.shimmerFrameLayout.stopShimmer()
    }

}