package com.example.imdb_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.sealed.StateOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class STATUS {
    NO_DATA, HAS_DATA, ON_LOAD, NO_INET
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository : MovieRepository
) : ViewModel() {

    private var _keyword = MutableStateFlow<String?>(null)

    val keyword: StateFlow<String?> get() = _keyword

    private var _searchData = MutableStateFlow<List<Movie>>(listOf())

    val searchData: StateFlow<List<Movie>> get() = _searchData

    private var _searchStatus = MutableStateFlow(STATUS.NO_DATA)

    val searchStatus : StateFlow<STATUS> get() = _searchStatus

    fun setStatusFromUI(value : STATUS) {
        _searchStatus.value = value
    }

    fun fetchSearchData(keyword : String) {
        viewModelScope.launch {
            _searchStatus.value = STATUS.ON_LOAD
            try {
                val searchResult = movieRepository.searchMovies(keyword)

                if(searchResult.isInternetAvailable == StateOnline.NetworkUnavailable) {
                    _searchStatus.value = STATUS.NO_INET
                } else {
                    if(searchResult.value != null) {
                        _searchData.value = searchResult.value
                        _searchStatus.value = STATUS.HAS_DATA
                    } else {
                        _searchStatus.value = STATUS.NO_DATA
                    }
                }
            } catch (e : java.lang.Exception) {
                _searchData.value = listOf()
                _searchStatus.value = STATUS.NO_INET
            }
        }
    }

}