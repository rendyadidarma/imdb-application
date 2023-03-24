package com.example.imdb_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.sealed.StateLoad
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

//    private var _movieList = MutableStateFlow<UIState<List<Movie>>>(UIState(StateLoad.Loading, listOf()))

//    val movieList: StateFlow<UIState<List<Movie>>> get() = _movieList

    private var _movieList = MutableSharedFlow<UIState<List<Movie>>>()

    val movieList: SharedFlow<UIState<List<Movie>>> get() = _movieList

//    init {
//        refreshDataListMovie()
//    }

    var fragmentState : Boolean = false

//    val isDatabaseEmpty = movieRepository
//        .isDatabaseEmpty()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)
//
//    suspend fun refreshDataFromRepoLocals(isDbEmpty: Boolean)
//    : Flow<EmptyResponseWrapper<List<Movie>>> {
//        _alreadyHasData.value = false
//        _dbEmpty.value = isDbEmpty
//        val something = movieRepository.getMovies(isDbEmpty)
//            .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
//
//        return movieRepository.getMovies(isDbEmpty)
//    }



    fun refreshDataListMovie() {
        viewModelScope.launch {
            try {
                val databaseEmptyStatus = movieRepository.isDatabaseEmpty().first()
                _movieList.emit(UIState(StateLoad.Loading, null))
                val response = movieRepository.getMovies(databaseEmptyStatus).first()
                if (databaseEmptyStatus.not()) {

                    _movieList.emit(UIState(StateLoad.Success, response.value))
                } else if(databaseEmptyStatus || response.isInternetAvailable == StateOnline.NetworkUnavailable){
                    _movieList.emit(UIState(StateLoad.Error, null))
                }
            } catch (e: java.lang.Exception) {
                _movieList.emit(UIState(StateLoad.Error, null))
            }
        }
    }


//    private fun refreshDataFromRepoLocal(isDbEmpty : Boolean) {
//        viewModelScope.launch {
//            _alreadyHasData.value = false
//            try {
//                val list = movieRepository.getMovies(isDbEmpty)
//
//                _dbEmpty.value = isDbEmpty
//                _movieList.value = list.
//
//                _alreadyHasData.value = true
//            } catch (error: IOException) {
//                Log.w("ErrorInHomeViewModel", "Error Detected in Home View Model Refresh fun")
//            }
//        }
//    }
//
//    fun refreshDataFromRepo() = refreshDataFromRepoLocal()

}