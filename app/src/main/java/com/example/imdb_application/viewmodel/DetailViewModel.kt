package com.example.imdb_application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.api.APINetwork
import com.example.imdb_application.data.repository.MovieRepositoryImpl
import com.example.imdb_application.data.utils.NetworkChecker
import com.example.imdb_application.view.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository =
        MovieRepositoryImpl(MovieDatabase.getDatabase(application), APINetwork.movies)

    private var _movieInDetail = MutableStateFlow<MovieDetail?>(null)

    val movieInDetail: MutableStateFlow<MovieDetail?> get() = _movieInDetail

    fun setMovieInDetail(id: String, currentActivity: MainActivity?) {
        viewModelScope.launch {
            try {
                val detail = movieRepository.getDetailFromDatabase(id)?.first()
                if(detail == null) {
                    _movieInDetail.value = movieRepository.getDetailFromNetwork(id)
                } else {
                    _movieInDetail.value = detail
                }
            } catch (exception : IOException) {
                if(!NetworkChecker.isOnline(getApplication())) {
                    currentActivity?.onSupportNavigateUp()
                }
                Log.w("setMovieInDetail", "Error Detected")
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}