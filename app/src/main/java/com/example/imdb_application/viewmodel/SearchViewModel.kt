package com.example.imdb_application.viewmodel

import android.app.Application
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.*
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.remote.api.APINetwork
import com.example.imdb_application.data.remote.dto.MovieDtoSearch
import com.example.imdb_application.data.repository.MovieRepositoryImpl
import com.example.imdb_application.data.utils.MovieObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class STATUS {
    NODATA, HASDATA, ONLOAD
}

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    init {

    }

    private val movieRepository = MovieRepositoryImpl(MovieDatabase.getDatabase(application))

    private var _keyword = MutableLiveData<String>("")

    val keyword: LiveData<String> get() = _keyword

    private var _searchData = MutableLiveData<List<Movie>>()

    val searchData: LiveData<List<Movie>> get() = _searchData

    private var _searchStatus = MutableLiveData<STATUS>(STATUS.NODATA)

    val searchStatus : LiveData<STATUS> get() = _searchStatus

    fun getMovieFromNetwork(keyword : String) : Job {
        return viewModelScope.launch() {
            _searchStatus.value = STATUS.ONLOAD
            delay(1000)
            try {
                val movieDtoSearch = APINetwork.movies.searchMovie(keyword).moviesForSearch
                val itemSearch = MovieObjectMapper.mapMovieDtoSearchToMovie(movieDtoSearch)
                _searchData.value = itemSearch
                if(!_searchData.value.isNullOrEmpty()) {
                    _searchStatus.value = STATUS.HASDATA
                } else {
                    _searchStatus.value = STATUS.NODATA
                }
            } catch (e : java.lang.Exception) {
                _searchData.value = listOf()
                _searchStatus.value = STATUS.NODATA
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}