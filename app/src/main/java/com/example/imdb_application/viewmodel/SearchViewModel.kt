package com.example.imdb_application.viewmodel

import android.app.Application
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.*
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.remote.api.APINetwork
import com.example.imdb_application.data.remote.dto.MovieDtoSearch
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.repository.MovieRepositoryImpl
import com.example.imdb_application.data.utils.MovieObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class STATUS {
    NODATA, HASDATA, ONLOAD
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository : MovieRepository
) : ViewModel() {

    private var _keyword = MutableStateFlow<String?>(null)

    val keyword: StateFlow<String?> get() = _keyword

    private var _searchData = MutableStateFlow<List<Movie>>(listOf())

    val searchData: StateFlow<List<Movie>> get() = _searchData

    private var _searchStatus = MutableStateFlow<STATUS>(STATUS.NODATA)

    val searchStatus : StateFlow<STATUS> get() = _searchStatus

    val fetchSearchData = fun(keyword : String) : Job {
        return viewModelScope.launch() {
            _searchStatus.value = STATUS.ONLOAD
            delay(1000)
            try {
                val searchResult = movieRepository.searchMovies(keyword)
                _searchData.value = searchResult
                if(_searchData.value.isNotEmpty()) {
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
//
//    class Factory(val app: Application) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return SearchViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }
}