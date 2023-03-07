package com.example.imdb_application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.repository.MovieRepository
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = MovieRepository(MovieDatabase.getDatabase(application))

//    val movies = movieRepository.movies

    init {
        refreshDataFromRepo()
    }

    private fun refreshDataFromRepo() = viewModelScope.launch {
        try {
            movieRepository.refreshMovies()
        } catch (error : IOException) {
            Log.w("Error", "Error Detected in Home View Model Refresh fun")
        }
    }

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