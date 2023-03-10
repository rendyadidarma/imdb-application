package com.example.imdb_application.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.imdb_application.databinding.FragmentDetailBinding
import com.example.imdb_application.view.MainActivity
import com.example.imdb_application.viewmodel.DetailViewModel


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
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.setMovieInDetail(args.movieId)
        getCurrentActivity()?.setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            getCurrentActivity()?.onSupportNavigateUp()
        }

//        if(getCurrentActivity()?.supportActionBar != null) {
//            getCurrentActivity()?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            getCurrentActivity()?.supportActionBar?.setDisplayShowHomeEnabled(true)
//        }

        return binding.root
    }

    private fun getCurrentActivity(): MainActivity? {
        return (activity as? MainActivity)
    }
}