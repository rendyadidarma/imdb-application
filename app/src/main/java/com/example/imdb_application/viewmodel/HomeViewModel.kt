package com.example.imdb_application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.repository.MovieRepositoryImpl
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = MovieRepositoryImpl(MovieDatabase.getDatabase(application))

    val movieList = movieRepository.getMovieFromDatabase()

    private var _alreadyHasData = MutableLiveData<Boolean>(false)

    val alreadyHasData : LiveData<Boolean> get() = _alreadyHasData

    init {
        refreshDataFromRepo()
    }

    private fun _refreshDataFromRepo() {
        Log.d("refresh Data", "Refreshing...")
        viewModelScope.launch {
            _alreadyHasData.value = false
            try {
                movieRepository.refreshMovies()
                _alreadyHasData.value = true
            } catch (error: IOException) {
                Log.w("Error", "Error Detected in Home View Model Refresh fun")
            }
        }
    }

    fun refreshDataFromRepo() = _refreshDataFromRepo()

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}