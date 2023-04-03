package com.example.imdb_application.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.imdb_application.R
import com.example.imdb_application.data.utils.BindUtils
import com.example.imdb_application.data.utils.Router
import com.example.imdb_application.data.utils.hideKeyboard
import com.example.imdb_application.data.utils.onKeywordValueChange
import com.example.imdb_application.databinding.FragmentSearchBinding
import com.example.imdb_application.view.MainActivity
import com.example.imdb_application.view.adapter.MovieListAdapter
import com.example.imdb_application.view.adapter.MovieListener
import com.example.imdb_application.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search) {

    companion object {
        private const val SEARCH_DEBOUNCE_TIME : Long = 1500
    }

    val viewModel by viewModels<SearchViewModel>()

    private var _binding : FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchRecyclerView.adapter = MovieListAdapter(
            MovieListener {
                Router.routeSearchFragmentToDetailFragment(movie = it, findNavController())
            }
        )
        searchViewBind(binding.searchView)
        bindObservables()
    }

    override fun onResume() {
        super.onResume()
        Log.w("OnResume", "onResumeCalled")
    }

    private fun searchViewBind(binding : EditText) {
        onKeywordValueChange(binding, SEARCH_DEBOUNCE_TIME) { keyword ->
            if(keyword.isEmpty().not()) {
                viewModel.fetchSearchData(keyword)
            }
//            else {
//                viewModel.setStatusFromUI(STATUS.NO_DATA)
//            }
        }
        binding.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if(hasFocus.not()) {
                hideKeyboard()
            }
        }
    }

    private fun bindObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.searchData.collect {
                        BindUtils.bindRecyclerView(binding.searchRecyclerView, it)
                    }
                }

                launch {
                    viewModel.searchStatus.collect {
                        BindUtils.bindShimmer(binding.shimmerFrameLayoutSearch, it, binding.searchRecyclerView, binding.noDataPlaceHolder)
                    }
                }

                launch {
                    viewModel.keyword.collect {
                        binding.searchView.setText(it)
                    }
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getCurrentActivity()?.getBottomNavView()?.visibility = View.VISIBLE
        _binding = FragmentSearchBinding.inflate(inflater)

        return binding.root
    }

    private fun getCurrentActivity(): MainActivity? {
        return (activity as? MainActivity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

