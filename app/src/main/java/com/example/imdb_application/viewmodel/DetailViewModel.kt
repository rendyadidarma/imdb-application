package com.example.imdb_application.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.view.fragments.DetailFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieRepository : MovieRepository
) : ViewModel() {

    private val args = DetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val isDetailEmpty = movieRepository.isDetailEmpty(args.movieId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            true
        )

    suspend fun movieInDetail(detailEmpty: Boolean): Flow<MovieDetail?> {
        return movieRepository.getDetail(detailEmpty, args.movieId).map {
            it.value
        }
    }

//    val movieInDetail = movieRepository.getDetail(args.movieId).map { it.value }.
//        stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(),
//            null
//        )

}