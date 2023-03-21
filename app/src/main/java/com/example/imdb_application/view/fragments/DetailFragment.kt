package com.example.imdb_application.view.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.utils.BindUtils
import com.example.imdb_application.databinding.FragmentDetailBinding
import com.example.imdb_application.view.MainActivity
import com.example.imdb_application.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    val viewModel by viewModels<DetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        getCurrentActivity()?.setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            getCurrentActivity()?.onSupportNavigateUp()
        }

        viewModel.setMovieInDetail(args.movieId)
        bindObservables()
    }

    private var _binding : FragmentDetailBinding? = null

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

    private fun bindObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.movieInDetail.collectLatest {
                        if(it != null) {
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
            BindUtils.bindImage(binding.detailImageView, image)
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