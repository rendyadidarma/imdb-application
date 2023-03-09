package com.example.imdb_application.view.fragments

import android.app.Notification.Action
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.imdb_application.R
import com.example.imdb_application.databinding.FragmentDetailBinding
import com.example.imdb_application.viewmodel.DetailViewModel
import com.example.imdb_application.viewmodel.HomeViewModel

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            ""
        }

        ViewModelProvider(this, DetailViewModel.Factory(activity.application)).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("OnDetail", "Enter On Detail")
        val binding = FragmentDetailBinding.inflate(inflater)

        Log.d("inDetail", args.movieId)
        viewModel.setMovieInDetail(args.movieId)

        return binding.root
    }


}