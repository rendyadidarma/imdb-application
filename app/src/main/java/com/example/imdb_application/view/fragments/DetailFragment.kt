package com.example.imdb_application.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.imdb_application.R
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.utils.ImageLoader
import com.example.imdb_application.databinding.FragmentDetailBinding
import com.example.imdb_application.view.MainActivity
import com.example.imdb_application.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment: Fragment(R.layout.fragment_detail) {

    val viewModel by viewModels<DetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentActivity()?.getBottomNavView()?.visibility = View.GONE
        getCurrentActivity()?.setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            getCurrentActivity()?.onSupportNavigateUp()
        }
        viewModel.getDetailMovie()
        bindObservables()
    }

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("OnDetail", "Enter On Detail")

        _binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun bindObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.detailMovie.collect {
                        if (it != null) {
                            binding.nestedScrollView.visibility = View.VISIBLE
                            binding.foundAProblem.visibility = View.GONE
                            bindLayout(it)
                        } else {
                            binding.nestedScrollView.visibility = View.GONE
                            binding.foundAProblem.visibility = View.VISIBLE
                        }
                    }
                }

            }
        }
    }

    private fun bindLayout(movieDetail: MovieDetail) {
        with(movieDetail) {
            binding.detailTitleView.title = title
            context?.let {safeContext ->
                ImageLoader.loadImage(safeContext, image, binding.detailImageView)
            }
            binding.detailPlotView.text = plot
            binding.detailReleaseView.text = releaseState
            binding.detailRuntimeView.text = runtimeStr
            binding.detailContentRatingView.text = contentRating
            binding.detailImdbRatingView.text = imDbRating
            binding.detailImdbCountView.text = String.format("(%s)", imDbRatingCount)
            binding.detailGenresView.text = genres
            binding.detailDirectorsView.text = directors
            binding.detailStarsView.text = stars
        }
    }

    private fun getCurrentActivity(): MainActivity? {
        return (activity as? MainActivity)
    }
}