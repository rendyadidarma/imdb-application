package com.example.imdb_application.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private var _movieList = MutableStateFlow<List<Movie>>(listOf())

    val movieList: StateFlow<List<Movie>> get() = _movieList

    private var _alreadyHasData = MutableStateFlow(false)

    val alreadyHasData: StateFlow<Boolean> get() = _alreadyHasData

    private var _dbEmpty = MutableLiveData<Boolean>()

    val dbEmpty: LiveData<Boolean> get() = _dbEmpty

    init {
        refreshDataFromRepo()
    }

    fun insertDetailToRoom(id: String) {
        viewModelScope.launch {
            try {
                movieRepository.refreshDetail(id)
            } catch (err: IOException) {
                Log.w("detailRefreshData", "Error Detected")
            }
        }
    }

    private fun refreshDataFromRepoLocal() {
        viewModelScope.launch {
            _alreadyHasData.value = false
            try {
                val list = movieRepository.getMovies()
                if (list != null) {
                    _movieList.value = list.first()
                    _dbEmpty.value = false
                } else {
                    _dbEmpty.value = true
                }
                _alreadyHasData.value = true
            } catch (error: IOException) {
                Log.w("ErrorInHomeViewModel", "Error Detected in Home View Model Refresh fun")
            }
        }
    }

    fun refreshDataFromRepo() = refreshDataFromRepoLocal()

//    class Factory(val app: Application) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return HomeViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewModel")
//        }
//    }
}