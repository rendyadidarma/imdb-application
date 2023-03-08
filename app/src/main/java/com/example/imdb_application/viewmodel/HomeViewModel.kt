package com.example.imdb_application.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = MovieRepository(MovieDatabase.getDatabase(application))

    val movieList = movieRepository.movies

    private var _alreadyHasData = MutableLiveData<Boolean>(false)

    val alreadyHasData : LiveData<Boolean> get() = _alreadyHasData

    private var _movieInDetail = MutableLiveData<Movie>()

    val movieInDetail : LiveData<Movie> get() = _movieInDetail

    init {
        refreshDataFromRepo()
    }

    private fun refreshDataFromRepo() =
        viewModelScope.launch {
            _alreadyHasData.value = false
        try {
            movieRepository.refreshMovies()
            _alreadyHasData.value = true
        } catch (error : IOException) {
            Log.w("Error", "Error Detected in Home View Model Refresh fun")
        }
    }

    fun onMovieClicked(movie : Movie) {
        _movieInDetail.value = movie
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