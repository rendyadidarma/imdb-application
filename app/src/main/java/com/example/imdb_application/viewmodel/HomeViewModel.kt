package com.example.imdb_application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.remote.api.APINetwork
import com.example.imdb_application.data.repository.MovieRepositoryImpl
import com.example.imdb_application.data.utils.NetworkChecker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = MovieRepositoryImpl(MovieDatabase.getDatabase(application), APINetwork.movies)

    private var _movieList = MutableLiveData<List<Movie>>()

    val movieList : LiveData<List<Movie>> get() = _movieList

    private var _alreadyHasData = MutableLiveData<Boolean>(false)

    val alreadyHasData : LiveData<Boolean> get() = _alreadyHasData

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

    private var _detailEmptyStatus = MutableStateFlow<Boolean>(false)

    val detailEmptyStatus : StateFlow<Boolean> get() = _detailEmptyStatus

    fun checkDetailIsNullOrNot(id: String) {
        viewModelScope.launch {
           movieRepository.isDetailEmpty(id).collect {
               _detailEmptyStatus.value = it
               Log.d("checkDetailItemClick", it.toString())
           }
        }
    }

    private fun _refreshDataFromRepo() {
        viewModelScope.launch {
            _alreadyHasData.value = false
                try {
                    val list = movieRepository.getMovies(getApplication())
                    if(list != null) {
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