package com.example.imdb_application.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository : MovieRepository
) : ViewModel() {

    private var _movieInDetail = MutableStateFlow<MovieDetail?>(MovieDetail())

    val movieInDetail: StateFlow<MovieDetail?> get() = _movieInDetail

    fun setMovieInDetail(id: String) {
        viewModelScope.launch {
            try {
                val state = movieRepository.getDetail(id)
                state.collect {
                    _movieInDetail.value = it.value
                }
            } catch (exception : IOException) {
                Log.w("setMovieInDetail", "Error Detected")
            }
        }
    }

}