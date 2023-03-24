package com.example.imdb_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.sealed.StateLoad
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private var _movieList = MutableSharedFlow<UIState<List<Movie>>>()

    val movieList: SharedFlow<UIState<List<Movie>>> get() = _movieList
    fun refreshDataListMovie() {
        viewModelScope.launch {
            try {
                val databaseEmptyStatus = movieRepository.isDatabaseEmpty().first()
                _movieList.emit(UIState(StateLoad.Loading, null))
                val response = movieRepository.getMovies(databaseEmptyStatus).first()
                if (databaseEmptyStatus.not()) {

                    _movieList.emit(UIState(StateLoad.Success, response.value))
                } else if(databaseEmptyStatus || response.isInternetAvailable == StateOnline.NetworkUnavailable){
                    _movieList.emit(UIState(StateLoad.Error, null))
                }
            } catch (e: Exception) {
                _movieList.emit(UIState(StateLoad.Error, null))
            }
        }
    }
}