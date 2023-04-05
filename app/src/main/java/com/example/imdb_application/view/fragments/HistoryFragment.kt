package com.example.imdb_application.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.imdb_application.data.utils.BindUtils
import com.example.imdb_application.databinding.FragmentHistoryBinding
import com.example.imdb_application.view.adapter.MovieListAdapter
import com.example.imdb_application.view.adapter.MovieListener
import com.example.imdb_application.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!

    val viewModel by viewModels<HistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllDetail()
        binding.historyRecyclerView.adapter = MovieListAdapter(
            MovieListener {  }
        )
        viewModel.detailList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty().not()) {
                binding.historyRecyclerView.visibility = View.VISIBLE
                val listReversed = it?.reversed()
                BindUtils.bindRecyclerView(binding.historyRecyclerView, listReversed)
            } else {
                binding.historyRecyclerView.visibility = View.GONE
            }
        }

        viewModel.observeConnectivity()
    }


}