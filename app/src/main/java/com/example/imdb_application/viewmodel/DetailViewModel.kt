package com.example.imdb_application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = MovieRepository(MovieDatabase.getDatabase(application))

    private var _movieInDetail = MutableLiveData<MovieEntity>()

    val movieInDetail : LiveData<MovieEntity> get()  = _movieInDetail

    fun setMovieInDetail(id: String) {
        viewModelScope.launch {
            val temp = movieRepository.getMovieDetail(id)
            _movieInDetail.value = temp.first()
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