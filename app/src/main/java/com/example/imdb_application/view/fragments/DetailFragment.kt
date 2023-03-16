package com.example.imdb_application.view.fragments

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.imdb_application.databinding.FragmentDetailBinding
import com.example.imdb_application.view.MainActivity
import com.example.imdb_application.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.setMovieInDetail(args.movieId, getCurrentActivity())
    }

    private lateinit var binding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("OnDetail", "Enter On Detail")

        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    private fun getCurrentActivity(): MainActivity? {
        return (activity as? MainActivity)
    }
}