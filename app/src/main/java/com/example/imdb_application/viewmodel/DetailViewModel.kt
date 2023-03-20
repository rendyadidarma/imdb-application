package com.example.imdb_application.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.sealed.StateOnline
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

    private var _statusInternet = MutableStateFlow<StateOnline?>(null)

    val statusInternet : StateFlow<StateOnline?> get() = _statusInternet

    private var _statusNoData = MutableStateFlow(true)

    val statusNoData: StateFlow<Boolean> get() = _statusNoData

    fun setMovieInDetail(id: String) {
        viewModelScope.launch {
            try {
                val state = movieRepository.getDetail(id)
                state.collect {
                    _movieInDetail.value = it.value
                    _statusInternet.value = it.isInternetAvailable
                }
            } catch (exception : IOException) {
                Log.w("setMovieInDetail", "Error Detected")
            }
        }
    }

//    class Factory(val app: Application) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return DetailViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewModel")
//        }
//    }

}