package com.example.imdb_application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = MovieRepository(MovieDatabase.getDatabase(application))

    private var _movieInDetail = MutableLiveData<Movie>()

    val movieInDetail : LiveData<Movie> get()  = _movieInDetail

    fun setMovieInDetail(id : String) {
        _movieInDetail.value = movieRepository.getMovieDetail(id).value
        _movieInDetail.value?.let { Log.d("Success get Data", it.fullTitle) }
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