package com.example.imdb_application.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.imdb_application.R
import com.example.imdb_application.data.utils.Router
import com.example.imdb_application.data.utils.onKeywordValueChange
import com.example.imdb_application.databinding.FragmentSearchBinding
import com.example.imdb_application.view.adapter.MovieListAdapter
import com.example.imdb_application.view.adapter.MovieListener
import com.example.imdb_application.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        private const val SEARCH_DEBOUNCE_TIME : Long = 1500
    }

    val viewModel by viewModels<SearchViewModel>()

    private lateinit var binding : FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val verticalDecorator = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val horizontalDecorator = DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL)

        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider_recycler, null)

        if (drawable != null) {
            verticalDecorator.setDrawable(drawable)
            horizontalDecorator.setDrawable(drawable)
            binding.searchRecyclerView.addItemDecoration(verticalDecorator)
            binding.searchRecyclerView.addItemDecoration(horizontalDecorator)
        }

        onKeywordValueChange(binding.searchView, SEARCH_DEBOUNCE_TIME) { keyword ->
            viewModel.fetchSearchData(keyword)
        }

        binding.searchRecyclerView.adapter = MovieListAdapter(
            MovieListener {
                Router.routeSearchFragmentToDetailFragment(movie = it, findNavController())
            }
        )

        return binding.root
    }


}

