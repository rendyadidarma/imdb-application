package com.example.imdb_application.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.imdb_application.databinding.FragmentSearchBinding
import com.example.imdb_application.view.adapter.MovieListAdapter
import com.example.imdb_application.view.adapter.MovieListener
import com.example.imdb_application.viewmodel.SearchViewModel
import java.util.*


class SearchFragment : Fragment(com.example.imdb_application.R.layout.fragment_search) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val viewModel: SearchViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can access the vmodel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            SearchViewModel.Factory(activity.application)
        ).get(SearchViewModel::class.java)
    }

    private lateinit var binding : FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.searchRecyclerView.adapter = MovieListAdapter(
            MovieListener {
                Log.d("Search Item", "Clicked!")
            }
        )

        return binding.root
    }


}