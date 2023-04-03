package com.example.imdb_application.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.view.fragments.DetailFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieRepository : MovieRepository
) : ViewModel() {

    private val args = DetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _detailMovie = MutableStateFlow<MovieDetail?>(MovieDetail())
    val detailMovie = _detailMovie.asStateFlow()
    fun getDetailMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.isDetailEmpty(args.movieId).collect { emptyOrNot ->
                movieRepository.getDetailNotReturnNetworkState(emptyOrNot, args.movieId).collect {
                    _detailMovie.value = it
                }
            }
        }
    }

}