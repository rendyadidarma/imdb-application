package com.example.imdb_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.sealed.StateLoad
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private var _movieList = MutableSharedFlow<UIState<List<Movie>>>()

    val movieList = _movieList.asSharedFlow()

    fun refreshDataListMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieRepository.isDatabaseEmpty().collect { databaseEmptyStatus ->
                    _movieList.emit(UIState(StateLoad.Loading, null))

                    movieRepository.getMovies(databaseEmptyStatus).collect { response ->
                        if (databaseEmptyStatus.not()) {
                            _movieList.emit(UIState(StateLoad.Success, response.value))
                        } else if (databaseEmptyStatus || response.isInternetAvailable == StateOnline.NetworkUnavailable) {
                            _movieList.emit(UIState(StateLoad.Error, null))
                        }
                    }
                }
            } catch (e: Exception) {
                _movieList.emit(UIState(StateLoad.Error, null))
            }
        }
    }


}