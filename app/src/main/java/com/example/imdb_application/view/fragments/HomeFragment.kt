package com.example.imdb_application.view.fragments

import android.app.Activity
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imdb_application.R
import com.example.imdb_application.databinding.FragmentHomeBinding
import com.example.imdb_application.view.adapter.MovieListAdapter
import com.example.imdb_application.view.adapter.MovieListener
import com.example.imdb_application.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can access the vmodel after onActivityCreated()"
        }
        ViewModelProvider(this, HomeViewModel.Factory(activity.application)).get(HomeViewModel::class.java)
    }

//    private var viewModelAdapter: MovieListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var binding : FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        binding!!.lifecycleOwner = viewLifecycleOwner
        binding!!.viewModel = viewModel


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
                viewModel.onMovieClicked(movie)
            }
        )

        return binding!!.root
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